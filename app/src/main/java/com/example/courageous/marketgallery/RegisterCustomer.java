package com.example.courageous.marketgallery;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

public class RegisterCustomer extends AppCompatActivity {

    Cursor cursorUsername, cursorEmail;
    EditText registerName, registerUsername, registerPassword, registerConfirmPassword, registerEmail;
    Intent resultIntent;
    DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);

        registerName = (EditText) findViewById(R.id.register_name_edit_text);
        registerUsername = (EditText) findViewById(R.id.register_username_edit_text);
        registerPassword = (EditText) findViewById(R.id.register_password_edit_text);
        registerConfirmPassword = (EditText) findViewById(R.id.register_confirm_password_edit_text);
        registerEmail = (EditText) findViewById(R.id.register_email_edit_text);

        // Automatically retrieves any usernames entered on the login activity.
        Intent newCustomer = getIntent();
        if (newCustomer.hasExtra("entered_username")) {
            String enteredUsername = newCustomer.getStringExtra("entered_username");
            registerUsername.setText(enteredUsername);
        }

        // Initializes and establishes a database connection.
        databaseAdapter = new DatabaseAdapter(this);
        try {
            databaseAdapter.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Closes any open cursors.
        if (cursorUsername != null && cursorUsername.getCount() > 0)
            cursorUsername.close();
        if (cursorEmail != null && cursorEmail.getCount() > 0)
            cursorEmail.close();
        databaseAdapter.close(); // Closes the database.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_customer, menu);
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

    public void cancelButton(View view) throws InterruptedException {
        registerName.setText(""); // Clearing the text entries
        registerUsername.setText("");
        registerPassword.setText("");
        registerConfirmPassword.setText("");
        registerEmail.setText("");
        Thread.sleep(100); // To make sure the user notices the text fields were cleared.
        resultIntent = new Intent(Intent.ACTION_PICK);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void registerButton(View view) {
        // Ensures no text fields are left blank.
        if (registerName.getText().toString().isEmpty() || registerUsername.getText().toString().isEmpty() ||
                registerPassword.getText().toString().isEmpty() || registerConfirmPassword.getText().toString().isEmpty()
                || registerEmail.getText().toString().isEmpty()) {
            if (registerName.getText().toString().isEmpty())
                registerName.setError("Name field cannot be blank");
            if (registerUsername.getText().toString().isEmpty())
                registerUsername.setError("Username field cannot be blank");
            if (registerPassword.getText().toString().isEmpty())
                registerPassword.setError("Password field cannot be blank");
            if (registerConfirmPassword.getText().toString().isEmpty())
                registerConfirmPassword.setError("Password field must be confirmed");
            if (registerEmail.getText().toString().isEmpty())
                registerEmail.setError("Email field cannot be blank");
        } // Checks that the 2nd time the password was entered matches the 1st time.
        else if (!registerPassword.getText().toString().equals(registerConfirmPassword.getText().toString())) {
            alert("Password Mismatch", "Please make sure that Password and Confirm Password fields match");
            registerPassword.setText(""); // Resets the password fields to blank for password re-entry.
            registerConfirmPassword.setText("");
            registerPassword.setError("Please enter a password");
            registerConfirmPassword.setError("Please re-enter password");
        } // Ensures the email address entered is a valid email address.
        else if (!registerEmail.getText().toString().contains("@") ||
                !registerEmail.getText().toString().contains(".")) {
            alert("Incorrect Email Address", "Please enter a valid email address");
            registerEmail.setError("Invalid email address");
        } // Query the database for the entered username and email.
        else {
            cursorUsername = databaseAdapter.checkUser(registerUsername.getText().toString());
            cursorEmail = databaseAdapter.checkEmail(registerEmail.getText().toString());
            cursorUsername.moveToFirst();
            cursorEmail.moveToFirst();

            /* If the username and email entered don't already exist in the database, add them to
               to the database and register the new user. */
            if (cursorUsername.getCount() < 1 && cursorEmail.getCount() < 1) {
                ContentValues values = new ContentValues();

                values.put(DatabaseAdapter.LoginTable.COLUMN_NAME_USERNAME, registerUsername.getText().toString());
                values.put(DatabaseAdapter.LoginTable.COLUMN_NAME_PASSWORD, registerPassword.getText().toString());
                values.put(DatabaseAdapter.LoginTable.COLUMN_NAME_NAME, registerName.getText().toString());
                values.put(DatabaseAdapter.LoginTable.COLUMN_NAME_EMAIL, registerEmail.getText().toString());

                databaseAdapter.insertCredential(values); // Inserting the credentials into the database.

                // Go back to the login page.
                resultIntent = new Intent(Intent.ACTION_PICK);
                setResult(RESULT_OK, resultIntent);
                finish();
                Toast.makeText(getApplication(), "Successfully Registered!", Toast.LENGTH_SHORT).show();
            } // If the email and username already exist, display an error.
            else if (cursorUsername.getCount() >= 1 && cursorEmail.getCount() >= 1) {
                registerUsername.setError("Username already exists");
                registerEmail.setError("Email already exists");
            } // If the username already exists, display an alert.
            else if (cursorUsername.getCount() >= 1) {
                alert("Username taken", "Username is already taken, please enter a different username");
            } // If the email already exists, display an alert.
            else if (cursorEmail.getCount() >= 1) {
                alert("Email already exists", "Email is already taken, please enter a different email");
            } // If anything unusual is entered or encountered, display an alert.
            else {
                alert("Please Try Again!", "Something went wrong, we apologize for the inconvenience");
            }
        }
    }

    // A simple alert dialogue to display feedback to users.
    public AlertDialog alert(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterCustomer.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        return alertDialog;
    }
}
