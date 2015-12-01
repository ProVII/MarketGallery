package com.example.courageous.marketgallery;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class BrowseProductsCustomer extends AppCompatActivity {

    private static final int REQUEST_ADD_PRODUCT = 0;
    private static final int REQUEST_DIFFERENT_PRICE = 1;
    private static final int REQUEST_CONTACT_US = 2;
    private static final int REQUEST_USER_PROFILE = 3;
    DatabaseAdapter databaseAdapter;
    Cursor cursor;
    TextView productNameTextView, productDescriptionTextView, productPriceCADTextView,
            productPriceConvertedTextView, productPriceCurrency, obtainUsername;
    ImageView imageView;
    Bitmap bmp;
    String productName, productDescription, enteredUsername, newCurrency,
            checkActivity = "add product";
    int productID;
    double productPriceCAD;
    Button previous_Button, next_Button;
    Converter asyncTask;
    DecimalFormat decimalFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_products_customer);

        previous_Button = (Button) findViewById(R.id.button_previous);
        next_Button = (Button) findViewById(R.id.button_next);
        obtainUsername = (TextView) findViewById(R.id.obtain_username);
        imageView = (ImageView) findViewById(R.id.user_image_view);

        // Formatting all decimals on screen to 3 decimal points.
        decimalFormatter = new DecimalFormat("0.000");

        // Getting the entered username from the login page.
        Intent newCust = getIntent();
        enteredUsername = newCust.getStringExtra("entered_username");
        obtainUsername.setText(enteredUsername);

        // Opening the database and establishing a connection.
        databaseAdapter = new DatabaseAdapter(this);
        try {
            databaseAdapter.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* Checks if we're coming back from the AddProduct activity, and queries the database for
           products accordingly. */
        if (checkActivity.equals("add product")) {
            cursor = databaseAdapter.checkProducts();
            cursor.moveToFirst(); // Starting on the first element of the database
        }
        showProduct(); // Calling showProduct function to start displaying the products
    }

    @Override
    protected void onStart() {
        super.onStart();

        /* Checks if we're coming back from the AddProduct activity, and queries the database for
           products accordingly. */
        if (checkActivity.equals("add product")) {
            cursor = databaseAdapter.checkProducts();
            cursor.moveToFirst(); // Starting on the first element of the database
        }
        showProduct();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Closing the cursor.
        if (cursor != null && cursor.getCount() > 0)
            cursor.close();
        asyncTask.cancel(true); // Ending the asynctask for Converter.
        databaseAdapter.close(); // Ending the connection with the database.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse_products_customer, menu);
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

    public void contactUsMenu(MenuItem item) {
        // Initializes the ContactUs activity.
        Intent contactUsIntent = new Intent(BrowseProductsCustomer.this, ContactUs.class);
        startActivityForResult(contactUsIntent, REQUEST_CONTACT_US);
    }

    public void userProfileMenu(MenuItem item) {
        // Initializes the UserProfile activity.
        Intent userProfileIntent = new Intent(BrowseProductsCustomer.this, UserProfile.class);
        userProfileIntent.putExtra("enteredUsername", enteredUsername);
        if (bmp != null) {
            // Resending the new user image in a byte array if there exists any.
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            userProfileIntent.putExtra("userImage", bytes);
            setResult(RESULT_OK);
        } else {
            /* Notifying the receiving activity that this is a first time login user and there's
               no picture to attach at the moment. */
            setResult(RESULT_FIRST_USER);
        }
        startActivityForResult(userProfileIntent, REQUEST_USER_PROFILE);
    }

    public void logoutMenu(MenuItem item) {
        // Ends the current session.
        Intent resultIntent = new Intent(Intent.ACTION_PICK);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void nextButton(View view) {
        cursor.moveToNext(); // Moves the cursor to the next position.
        showProduct(); // Calling this function displays the next product.
    }

    public void previousButton(View view) {
        cursor.moveToPrevious(); // Moves the cursor to the previous position.
        showProduct(); // Calling this function displays the previous product.
    }

    public void showProduct() {

        // Disabling and Enabling of next and previous buttons for user Error Prevention.
        if (cursor.isFirst()) {
            previous_Button.setClickable(false); // This is a feature meant for validity check.
            previous_Button.setEnabled(false);
            if (!cursor.isLast()) {
                next_Button.setClickable(true);
                next_Button.setEnabled(true);
            }
            if (cursor.isLast()) {
                next_Button.setClickable(false);
                next_Button.setEnabled(false);
            }
        } else if (cursor.isLast()) {
            next_Button.setClickable(false);
            next_Button.setEnabled(false);
            if (!cursor.isFirst()) {
                previous_Button.setClickable(true);
                previous_Button.setEnabled(true);
            }
        } else {
            previous_Button.setClickable(true);
            next_Button.setClickable(true);
            previous_Button.setEnabled(true);
            next_Button.setEnabled(true);
        }

        productNameTextView = (TextView) findViewById(R.id.product_name_field);
        productDescriptionTextView = (TextView) findViewById(R.id.product_description_field);
        productPriceCADTextView = (TextView) findViewById(R.id.price_cad_field);
        productPriceConvertedTextView = (TextView) findViewById(R.id.price_usd_field);
        productPriceCurrency = (TextView) findViewById(R.id.price_usd);

        // Retrieves product values from the database query.
        productID = cursor.getInt(cursor.getColumnIndex(DatabaseAdapter.
                ProductsTable.COLUMN_NAME_PRODUCT_ID));
        productName = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.
                ProductsTable.COLUMN_NAME_PRODUCT_NAME));
        productDescription = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.
                ProductsTable.COLUMN_NAME_DESCRIPTION));
        productPriceCAD = cursor.getDouble(cursor.getColumnIndex(DatabaseAdapter.
                ProductsTable.COLUMN_NAME_PRICE));

        // If no new currency is selected, display the USD currency.
        if (newCurrency == null || newCurrency.equals("")) {
            asyncTask = new Converter(productPriceCAD, "USD"); // Initializes asyncTask for USD.
        } // If a new currency is selected, display that currency instead of USD currency.
        else {
            asyncTask = new Converter(productPriceCAD, newCurrency);
            productPriceCurrency.setText(newCurrency + " Price:");
        }

        // Sets the product name, description, and CAD price.
        productNameTextView.setText(productName);
        productDescriptionTextView.setText(productDescription);
        productPriceCADTextView.setText(String.valueOf(decimalFormatter.format(productPriceCAD)));

        /* Obtains the new currency via the asynctask Converter and displays it in its respective
           textview accordingly. */
        try {
            productPriceConvertedTextView.setText(String.valueOf(decimalFormatter.format(asyncTask.execute().get())));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void userImageView(View view) {
        // Initializes the UserProfile activity.
        Intent userImageIntent = new Intent(BrowseProductsCustomer.this, UserProfile.class);
        userImageIntent.putExtra("enteredUsername", enteredUsername);
        if (bmp != null) {
            // Resending the new user image in a byte array if there exists any.
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            userImageIntent.putExtra("userImage", bytes);
            setResult(RESULT_OK);
        } else {
            /* Notifying the receiving activity that this is a first time login user and there's
               no picture to attach at the moment. */
            setResult(RESULT_FIRST_USER);
        }
        startActivityForResult(userImageIntent, REQUEST_USER_PROFILE);
    }

    public void differentPrice(View view) {
        // Initializes the SwitchCurrency activity and sends the current CAD price value.
        Intent changeCurrency = new Intent(BrowseProductsCustomer.this, SwitchCurrency.class);
        changeCurrency.putExtra("productPriceCAD", productPriceCAD);
        startActivityForResult(changeCurrency, REQUEST_DIFFERENT_PRICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_PRODUCT && resultCode == RESULT_OK) {
            // A variable used for validating the requesting activity.
            checkActivity = "add product";
        }
        if (requestCode == REQUEST_DIFFERENT_PRICE && resultCode == RESULT_OK) {
            // Gets the new currency from the SwitchCurrency activity.
            newCurrency = data.getStringExtra("currency");
            checkActivity = "different price";
        }
        if (requestCode == REQUEST_USER_PROFILE && resultCode == RESULT_OK) {
            /* Gets the new user profile selected image from the UserProfile activity
               via a byte array. */
            byte[] bytes = data.getByteArrayExtra("userImage");
            bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Sets the received image as the current profile image.
            imageView.setImageBitmap(bmp);
            checkActivity = "change image";
        }
        if (requestCode == REQUEST_USER_PROFILE && resultCode == RESULT_CANCELED) {
            // Logs out of the current session by ending it if the result code is to logout.
            setResult(RESULT_CANCELED);
            finish();
        }
        if (requestCode == REQUEST_CONTACT_US && resultCode == RESULT_OK) {
            // Logs out of the current session by ending it if the result code is to logout.
            setResult(RESULT_OK);
            finish();
        }
    }
}
