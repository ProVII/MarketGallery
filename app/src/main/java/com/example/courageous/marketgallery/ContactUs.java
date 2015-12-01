package com.example.courageous.marketgallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ContactUs extends AppCompatActivity {

    static final String TAG = "Google Play Services";
    String address1, address2, city, province, country, postalCode, phoneNumber, URL;
    LocationManager locationManager;
    LocationListener locationListener;
    Geocoder geocoder;
    List<Address> addresses;
    MapFragment mapFragment;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            // Called when a new location is found by the gps location provider.
            public void onLocationChanged(Location location) {
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    // Gets the addresses off the geocoder's list and from the list to string variables.
                    getAddresses();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Converts the map fragment to a GoogleMap.
                map = mapFragment.getMap();
                // Obtains the current user's GPS location.
                LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());

                // Clears the map on every new GPS returned location.
                map.clear();
                map.setMyLocationEnabled(true);
                // Moves the camera to the user's current GPS reported location.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 13));

                // Adds a marker notifying the user of his location.
                map.addMarker(new MarkerOptions()
                        .title("You")
                        .snippet(address1 + address2)
                        .position(newLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                // Sets up the rest of the markers for each Snackies Market branch.
                setMapMarkers();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        // Requests the GPS for location updates through the LocationManager.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        /* Checks that the current Google Play Services version matches the one used by the app, if
           not, a dialogue is displayed asking the user to update to the latest Google Play Services. */
        if (checkGooglePlayServices()) {
            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stops requesting GPS updates.
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_us, menu);
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

    public void logoutMenu(MenuItem item) {
        // Logs out by ending current session.
        Intent resultIntent = new Intent(Intent.ACTION_PICK);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void getAddresses() {
        /* If any additional address line present then only check with max available
           address lines by getMaxAddressLineIndex() */
        address1 = addresses.get(0).getAddressLine(0);
        address2 = addresses.get(0).getAddressLine(1);
        city = addresses.get(0).getLocality();
        province = addresses.get(0).getAdminArea();
        country = addresses.get(0).getCountryName();
        postalCode = addresses.get(0).getPostalCode();
        phoneNumber = addresses.get(0).getPhone();
        URL = addresses.get(0).getUrl();
    }

    public void setMapMarkers() {
        // Customized marker locations for the Snackies Market branches.
        LatLng snackiesMarketplace1 = new LatLng(54.509078, -123.630116);
        LatLng snackiesMarketplace2 = new LatLng(43.676289, -79.439934);
        LatLng snackiesMarketplace3 = new LatLng(47.460641, -71.297026);
        LatLng snackiesMarketplace4 = new LatLng(48.591924, -89.037874);
        LatLng snackiesMarketplace5 = new LatLng(49.870723, -96.912767);
        LatLng snackiesMarketplace6 = new LatLng(53.243930, -105.758076);
        LatLng snackiesMarketplace7 = new LatLng(53.454878, -113.840242);
        LatLng snackiesMarketplace8 = new LatLng(40.627335, -74.966021);

        map.addMarker(new MarkerOptions()
                .title("Snackies Marketplace")
                .snippet("Fraser-Fort George G, BC" + " Phone: 111 222 3333")
                .position(snackiesMarketplace1));
        map.addMarker(new MarkerOptions()
                .title("Snackies Marketplace")
                .snippet("41 Northcliffe Blvd, Toronto" + " Phone: 222 333 4444")
                .position(snackiesMarketplace2));
        map.addMarker(new MarkerOptions()
                .title("Snackies Marketplace")
                .snippet("Lac-Jacques-Cartier, QC" + " Phone: 333 444 5555")
                .position(snackiesMarketplace3));
        map.addMarker(new MarkerOptions()
                .title("Snackies Marketplace")
                .snippet("Shuniah, ON" + " Phone: 444 555 6666")
                .position(snackiesMarketplace4));
        map.addMarker(new MarkerOptions()
                .title("Snackies Marketplace")
                .snippet("Pineridge Road 24E" + " Phone: 555 666 7777")
                .position(snackiesMarketplace5));
        map.addMarker(new MarkerOptions()
                .title("Snackies Marketplace")
                .snippet("Parkland County, AB" + " Phone: 666 777 8888")
                .position(snackiesMarketplace6));
        map.addMarker(new MarkerOptions()
                .title("Snackies Marketplace")
                .snippet("Bulkley-Nechako B, BC" + " Phone: 777 888 9999")
                .position(snackiesMarketplace7));
        map.addMarker(new MarkerOptions()
                .title("Snackies Marketplace")
                .snippet("151 Perryville Road, NJ" + " Phone: 123 456 7890")
                .position(snackiesMarketplace8));
    }

    private boolean checkGooglePlayServices() {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            Log.e(TAG, GooglePlayServicesUtil.getErrorString(status));
            // Asks user to update google play services.
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, 1);
            dialog.show();
            return false;
        } else {
            Log.i(TAG, GooglePlayServicesUtil.getErrorString(status));
            // Google play services is updated.
            return true;
        }
    }
}
