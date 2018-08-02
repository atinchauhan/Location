package com.example.atinchauhan.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLoctionInfo(location);
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
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation!=null){
                updateLoctionInfo(lastKnownLocation);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            startListening();

        }
    }
    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

    }

    public void updateLoctionInfo(Location location){

        TextView latTextView=findViewById(R.id.latTextView);
        TextView longTextView=findViewById(R.id.longTextView);
        TextView accTextView=findViewById(R.id.accuracyTextView);
        TextView addressTextView=findViewById(R.id.addressTextView);
        TextView altTextView=findViewById(R.id.altTextView);

        latTextView.setText("Latitude: "+Double.toString(location.getLatitude()));
        longTextView.setText("Longitude: "+ Double.toString(location.getLongitude()));
        accTextView.setText("Accuracy: "+ Double.toString(location.getAccuracy()));
        altTextView.setText("Altitude: "+ Double.toString(location.getAltitude()));


        String address="Could not find address :(";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listAddresss=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(listAddresss!=null && listAddresss.size()>0){
                address="Address: \n";

                if(listAddresss.get(0).getThoroughfare()!=null){
                    address+=listAddresss.get(0).getThoroughfare()+" ";
                }
                if(listAddresss.get(0).getLocality()!=null){
                    address+=listAddresss.get(0).getLocality()+" ";
                }
                if(listAddresss.get(0).getPostalCode()!=null){
                    address+=listAddresss.get(0).getPostalCode()+" ";
                }
                if(listAddresss.get(0).getAdminArea()!=null){
                    address+=listAddresss.get(0).getAdminArea()+" ";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        addressTextView.setText(address);


    }
}