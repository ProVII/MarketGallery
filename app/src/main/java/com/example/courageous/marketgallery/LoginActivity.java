package com.example.courageous.marketgallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;
    DatabaseAdapter databaseAdapter;
    Cursor cursorUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.username_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        loginButton = (Button) findViewById(R.id.login_button);

        // Login button is not gonna be clickable unless user enters something in both text fields above.
        loginButton.setClickable(false);
        loginButton.setEnabled(false);

        // Initializes and establishes a database connection.
        databaseAdapter = new DatabaseAdapter(this);
        try {
            databaseAdapter.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Adding a TextWatcher for both the password and username fields.
        disableLoginButton(passwordEditText, usernameEditText);
        disableLoginButton(usernameEditText, passwordEditText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Closes any open cursors.
        if (cursorUsername != null && cursorUsername.getCount() > 0)
            cursorUsername.close();
        databaseAdapter.close(); // Closes the database connection.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void loginButton(View view) {
        // Queries the database for the entered username.
        cursorUsername = databaseAdapter.checkUser(usernameEditText.getText().toString());
        cursorUsername.moveToFirst();

        // Ensures none of the text fields are left blank.
        if (usernameEditText.getText().toString().isEmpty() ||
                passwordEditText.getText().toString().isEmpty()) {
            if (usernameEditText.getText().toString().isEmpty())
                usernameEditText.setError("Username field cannot be blank");
            if (passwordEditText.getText().toString().isEmpty())
                passwordEditText.setError("Password field cannot be blank");
        } // If the username was not found in the database, display an alert.
        else if (cursorUsername.getCount() < 1) {
            alert("Wrong username", "No such username was found");
        } // If the username was found in the database.
        else if (cursorUsername.getCount() >= 1) {
            // Obtain the password for that username.
            String password = cursorUsername.getString(cursorUsername.getColumnIndex(DatabaseAdapter.
                    LoginTable.COLUMN_NAME_PASSWORD));
            // If the username belongs to a developer.
            if (usernameEditText.getText().toString().equals("Admin")) {
                // And the password entered matches the developer's password.
                if (passwordEditText.getText().toString().equals(password)) {
                    // Send the user to the BrowseProductsDevs activity along with the username entered.
                    Intent dev = new Intent(LoginActivity.this, BrowseProductsDevs.class);
                    dev.putExtra("entered_username", usernameEditText.getText().toString());
                    // Clear all text fields.
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    startActivityForResult(dev, 0);
                } else {
                    // If the password entered doesn't match the developer's password, display an alert.
                    alert("Wrong password", "No such password was found");
                }
            } // if the username doesn't belong to a developer.
            else if (passwordEditText.getText().toString().equals(password)) {
                // Send the user to the BrowseProductsCustomer activity along with the username entered.
                Intent customer = new Intent(LoginActivity.this, BrowseProductsCustomer.class);
                customer.putExtra("entered_username", usernameEditText.getText().toString());
                // Clear all text fields.
                usernameEditText.setText("");
                passwordEditText.setText("");
                startActivityForResult(customer, 0);
            } else {
                // If the password entered doesn't match the user's password, display an alert.
                alert("Wrong password", "No such password was found");
            }
        } // If anything unusual is entered or encountered, display an alert.
        else {
            alert("Please Try Again!", "Something went wrong, we apologize for the inconvenience");
        }
    }

    public void registerButton(View view) {
        // Initializes the RegisterCustomer activity for registering a new customer.
        Intent newCustomer = new Intent(LoginActivity.this, RegisterCustomer.class);
        newCustomer.putExtra("entered_username", usernameEditText.getText().toString());
        startActivityForResult(newCustomer, 0);
    }

    public void devsOnly(View view) {
        // Initializes the DevsLoginDemo activity.
        Intent devs = new Intent(LoginActivity.this, DevsLoginDemo.class);
        startActivityForResult(devs, 0);
    }

    // A simple alert dialogue to display feedback to users.
    public AlertDialog alert(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
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

    // TextWatcher function that listens to passed EditTexts.
    public void disableLoginButton(final EditText editText1, final EditText editText2) {
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                loginButton.setClickable(true);
                loginButton.setEnabled(true);

                if (editText1.getText().toString().isEmpty() || editText2.getText().toString().isEmpty()) {
                    loginButton.setClickable(false);
                    loginButton.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }
}
