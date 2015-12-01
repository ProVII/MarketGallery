package com.example.courageous.marketgallery;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class UserProfile extends AppCompatActivity {

    private static final int GALLERY = 1;
    DatabaseAdapter databaseAdapter;
    static Cursor imageCursor;
    Cursor cursor;
    TextView usernameTextView, nameTextView, emailTextView;
    String username, name, email;
    private static Bitmap image = null;
    private static Bitmap rotateImage = null;
    private ImageView imageView;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        /* Saving some images from resources to SD card directory for a chance to customize
           profile photo if a user has no stored photos. */
        int[] drawablesArr = {R.drawable.default_user, R.drawable.profile1, R.drawable.profile2, R.drawable.profile3};

        for(int i=0; i<drawablesArr.length; i++){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawablesArr[i]);
            String fileName = "snackiesdefaultimage_"+ String.valueOf(i+1)+".png";

            File sd = Environment.getExternalStorageDirectory();
            File folder = new File(sd + "/Snackies_profile");
            folder.mkdir();

            File dest = new File(folder, fileName);
            try {
                FileOutputStream out = new FileOutputStream(dest);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Making sure the gallery scans each of these files.
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(dest);
            mediaScanIntent.setData(contentUri);
            getApplicationContext().sendBroadcast(mediaScanIntent);
        }

        usernameTextView = (TextView) findViewById(R.id.profile_username);
        nameTextView = (TextView) findViewById(R.id.profile_name);
        emailTextView = (TextView) findViewById(R.id.profile_email);
        imageView = (ImageView) findViewById(R.id.imageview_user);

        // Initializing and establishing a database connection.
        databaseAdapter = new DatabaseAdapter(this);
        try {
            databaseAdapter.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent getUsernameAndImage = getIntent();
        // Obtains any images or usernames from the calling activity if there exists any.
        if (getUsernameAndImage.hasExtra("userImage")) {
            // Obtains the image through a byte array and uncompresses it back to an image.
            byte[] bytes = getUsernameAndImage.getByteArrayExtra("userImage");
            Bitmap bmpFromBrowserActivity = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bmpFromBrowserActivity);
        }

        username = getUsernameAndImage.getStringExtra("enteredUsername");
        cursor = databaseAdapter.checkUser(username); // Queries the database for the obtained username.
        cursor.moveToFirst(); // Starting on the first element of the database

        name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.
                LoginTable.COLUMN_NAME_NAME));
        email = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.
                LoginTable.COLUMN_NAME_EMAIL));

        // Displaying the user's name, username, and email.
        usernameTextView.setText(username);
        nameTextView.setText(name);
        emailTextView.setText(email);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Closing any open cursors.
        if (cursor != null && cursor.getCount() > 0)
            cursor.close();
        databaseAdapter.close(); // Closing the database connection.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // pressing on the image opens the gallery and queries it for pictures.
    public void userImageView(View view) {
        imageView.setImageBitmap(null);
        if (image != null)
            image.recycle();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
    }

    public void backButton(View view) {
        Intent resultIntent = new Intent(Intent.ACTION_PICK);
        // If user decides not to change profile image, go back with a first user result code.
        setResult(RESULT_FIRST_USER, resultIntent);
        if (imageView != null) {
            imageView.buildDrawingCache();
            Bitmap userImage = imageView.getDrawingCache();

            // Sending the new user image in a byte array.
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            userImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            resultIntent.putExtra("userImage", bytes);
            setResult(RESULT_OK, resultIntent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY && resultCode != 0) {
            // Queries the gallery for images.
            imageUri = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                // Ensures the current orientation is fitting.
                if (getOrientation(getApplicationContext(), imageUri) != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(getOrientation(getApplicationContext(), imageUri));
                    if (rotateImage != null)
                        rotateImage.recycle();
                    // Scaling down the size of the image to ensure fitting in.
                    rotateImage = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix,true);
                    rotateImage = scaleDownBitmap(rotateImage, 200, getApplicationContext());
                    imageView.setImageBitmap(rotateImage);
                } else {
                    image = scaleDownBitmap(image, 200, getApplicationContext());
                    imageView.setImageBitmap(image);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // A function for querying the gallery.
    public static int getOrientation(Context context, Uri photoUri) {
        imageCursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },null, null, null);

        if (imageCursor.getCount() != 1) {
            return -1;
        }
        imageCursor.moveToFirst();
        return imageCursor.getInt(0);
    }

    // A function for scaling down the image size.
    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    // Ends the current session.
    public void logoutMenu(MenuItem item) {
        Intent resultIntent = new Intent(Intent.ACTION_PICK);
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }
}
