package com.example.courageous.marketgallery;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Converter extends AsyncTask<Void, Void, Double> {

    URL baseURL;
    String urlOutput;
    double result;
    double price;
    String currency;

    Converter (double price, String currency) {
        this.price = price;
        this.currency = currency;
    }

    @Override
    protected Double doInBackground(Void... params) {
        /* Takes in a currency parameter sent through the CurrencySwitch activity, and then passes
           it to the URL below to receive the most updated exchange rate from CAD to that currency. */
        try {
            baseURL = new URL("http://rate-exchange-1.appspot.com/currency?from=CAD&to=" + currency);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        BufferedReader br;
        StringBuffer stringBuffer;
        try {
            // Opens the URL and store the output of the URL into a string variable.
            br = new BufferedReader(new InputStreamReader(baseURL.openStream()));
            stringBuffer = new StringBuffer();
            String temp;
            while ((temp = br.readLine()) != null) {
                stringBuffer.append(temp);
                Thread.sleep(1);
                if (isCancelled())
                    break;
            }
            // Replaces all characters except numeric characters and dots.
            urlOutput = stringBuffer.toString().replaceAll("[^\\d.]", "");
            /* Multiplies the neweset & current exchange rate obtained through the URL above by the
               price passed from the SwitchCurrency activity. */
            result = Double.parseDouble(urlOutput) * price;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}