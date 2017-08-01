package com.example.tabsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

class GpsClass implements LocationListener {
    Context context;
    Location location;
    LocationListener locationListener;
    LocationManager locationManager;
    Criteria criteria;
    SharedPreferences preferences;

    public static final int RequestPermissionCode = 1;
    private static final String PREF = "PREFERENCE";
    private static final String TRANSFER = "TRANSFER";

    private int NUM = 1;
    private String bestProvider;
    public float speed;
    public double latitude, longitude;

    public String getAllCoordinates() {
        return latitude + " " + longitude;
    }

    GpsClass(Context context) {
        this.context = context;
        GpsUpdate();
    }

    void GpsUpdate() {
        preferences = context.getSharedPreferences(PREF, MODE_PRIVATE);
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        locationListener = this;
        bestProvider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = locationManager.getLastKnownLocation(bestProvider);
        NUM = preferences.getInt(TRANSFER, 1);
        if (NUM == 1){
            locationManager.requestLocationUpdates(bestProvider, 1000, 10, locationListener);
        }
        else if (NUM == 2){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        speed = location.getSpeed();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}