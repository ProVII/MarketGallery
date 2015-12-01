package com.example.courageous.marketgallery;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import java.sql.SQLException;

public class AddProduct extends AppCompatActivity {

    DatabaseAdapter databaseAdapter;
    EditText productNewName, productNewDescription, productNewPrice;
    Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productNewName = (EditText)findViewById(R.id.product_name_edit_text);
        productNewDescription = (EditText)findViewById(R.id.product_description_edit_text);
        productNewPrice = (EditText)findViewById(R.id.price_cad_edit_text);

        // Opening the database and establishing a connection.
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

        // Ending database connection.
        databaseAdapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
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
        productNewName.setText(""); // Clearing the text entries
        productNewDescription.setText("");
        productNewPrice.setText("");
        Thread.sleep(100); //To make sure the user notices the text fields were cleared.
        resultIntent = new Intent(Intent.ACTION_PICK);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void saveButton(View view) {
        ContentValues values = new ContentValues(); // Adding in the values of the new product

        values.put(DatabaseAdapter.ProductsTable.COLUMN_NAME_PRODUCT_NAME, productNewName.getText().toString());
        values.put(DatabaseAdapter.ProductsTable.COLUMN_NAME_DESCRIPTION, productNewDescription.getText().toString());
        values.put(DatabaseAdapter.ProductsTable.COLUMN_NAME_PRICE, Double.parseDouble(productNewPrice.getText().toString()));

        // Inserting the product into the database.
        databaseAdapter.insertProduct(values);

        resultIntent = new Intent(Intent.ACTION_PICK);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
