package com.example.courageous.marketgallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class SwitchCurrency extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final double default_value = 404.1;
    TextView cadTextView, convertedPrice;
    Button switchCurrencyButton;
    DecimalFormat decimalFormatter;
    ArrayAdapter adapter;
    Spinner currencySpinner;
    String[] array;
    double cadPrice;
    Converter asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_currency);

        cadTextView = (TextView) findViewById(R.id.cad_textview);
        convertedPrice = (TextView) findViewById(R.id.converted_price);
        switchCurrencyButton = (Button) findViewById(R.id.switch_currency_button);
        currencySpinner = (Spinner) findViewById(R.id.currency_spinner);
        // Obtaining our currency array from the values resources.
        array = getResources().getStringArray(R.array.currencies);

        // Formatting some decimals on screen to 6 decimal points for smoother conversion accuracy.
        decimalFormatter = new DecimalFormat("#0.000000");

        Intent changeCurrency = getIntent();
        // Obtaining and setting the CAD price from the calling activity.
        cadPrice = changeCurrency.getDoubleExtra("productPriceCAD", default_value);
        cadTextView.setText(String.valueOf(decimalFormatter.format(cadPrice)));

        // Creates an ArrayAdapter using the string array and a default spinner layout.
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                array);
        // Specifies the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applies the adapter to the spinner.
        currencySpinner.setAdapter(adapter);

        // Initializes the listener for the spinner.
        currencySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_switch_currency, menu);
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

    @Override
    protected void onStop() {
        super.onStop();
        asyncTask.cancel(true); // Ending the asynctask for Converter.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asyncTask.cancel(true); // Ending the asynctask for Converter.
    }

    public void cancelCurrencyButton(View view) {
        // ends the current session without switching the currency.
        Intent resultIntent = new Intent(Intent.ACTION_PICK);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final double currencyPrice;
        final String currency = parent.getItemAtPosition(position).toString();
        // Gets the output from asynctask on each currency passed to it.
        asyncTask = new Converter(cadPrice, currency);
        try {
            currencyPrice = asyncTask.execute().get();
            // Displays the current price in converted currency obtained through the asynctask.
            convertedPrice.setText(String.valueOf(currencyPrice));
            // On pressing the Switch Currency button, send the new currency and finish the activity.
            switchCurrencyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent resultIntent = new Intent(Intent.ACTION_PICK);
                    resultIntent.putExtra("currency", currency);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        convertedPrice.setText("Please select a currency");
    }
}
