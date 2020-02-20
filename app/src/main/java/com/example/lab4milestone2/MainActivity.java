package com.example.lab4milestone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.*;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.*;
import android.widget.TextView;

import java.io.IOException;
import java.util.*;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    public void updateLocationInfo(Location location){
        Log.i("LocationInfo", location.toString());
        TextView latTextView = (TextView) findViewById(R.id.latitude);
        TextView lonTextView = (TextView) findViewById(R.id.longitude);
        TextView altTextView = (TextView) findViewById(R.id.altitude);
        TextView accTextView = (TextView) findViewById(R.id.accuracy);

        latTextView.setText("Latitude: " + location.getLatitude());
        lonTextView.setText("Longitude: " + location.getLongitude());
        altTextView.setText("Altitude: " + location.getAltitude());
        accTextView.setText("Accuracy: " + location.getAccuracy());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try{
        String address = "Could not find address";
        List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        if(listAddresses != null && listAddresses.size() > 0){
            Log.i("PlaceInfo", listAddresses.get(0).toString());
            address = "Address: \n";

            if (listAddresses.get(0).getSubThoroughfare() != null){
                address += listAddresses.get(0).getSubThoroughfare() + " ";
            }
            if (listAddresses.get(0).getThoroughfare() != null){
                address += listAddresses.get(0).getThoroughfare() + "\n";
            }


            if (listAddresses.get(0).getLocality() != null){
                address += listAddresses.get(0).getLocality() + "\n";
            }


            if (listAddresses.get(0).getPostalCode() != null){
                address += listAddresses.get(0).getPostalCode() + "\n";
            }

            if (listAddresses.get(0).getCountryName() != null){
                address += listAddresses.get(0).getCountryName() + "\n";
            }
            TextView addressTextView = (TextView) findViewById(R.id.address);
            addressTextView.setText(address);
        }
        }

        catch (IOException e){
            Log.e("ERROR", e.toString());
            e.printStackTrace();
        }

    }

    LocationManager locationManager;

    LocationListener locationListener;

    private void startListening() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        } ;

        if(Build.VERSION.SDK_INT < 23){
            startListening();
        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null){
                    updateLocationInfo(location);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }


}
