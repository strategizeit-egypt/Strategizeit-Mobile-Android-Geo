package com.xapps.karbala.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class TLocationManager {
    public static final int LOCATION_INTERVAL = 1000;
    public static final float LOCATION_DISTANCE = 0;
    private static final TLocationManager ourInstance = new TLocationManager();
    private static String TAG = "LocationManger:";
    private static Location currentLocation;
    LocationListener[] mLocationListeners = null;
    private LocationManager mLocationManager = null;
    private Location gpslocation = null;
    private Location networkLocation = null;
    private OnTLocationChanged onTLocationChanged;

    private TLocationManager() {
        mLocationListeners = new LocationListener[]{
                new LocationListener(LocationManager.GPS_PROVIDER),
                new LocationListener(LocationManager.NETWORK_PROVIDER)
        };
    }

    public static TLocationManager getInstance() {
        return ourInstance;
    }

    public static void requestPermissions(Activity context) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, 89);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 2000);
        }
    }

    public Location getBestLocation(Context context, OnTLocationChanged onTLocationChanged) {


        if (onTLocationChanged != null)
            this.onTLocationChanged = onTLocationChanged;

        if (gpslocation == null && networkLocation == null) {
            start(context);
            return null;
        }

        if (gpslocation != null && networkLocation != null) {
            if (gpslocation.getTime() < networkLocation.getTime()) {
                gpslocation = null;
                return networkLocation;
            } else {
                networkLocation = null;
                return gpslocation;
            }
        }
        if (gpslocation == null) {
            return networkLocation;
        }
        if (networkLocation == null) {
            return gpslocation;
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    public void start(Context context) {

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, 1, mLocationListeners[0]);// here you can set the 2nd argument time interval also that after how much time it will get the gps location
                gpslocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }
            if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, 1, mLocationListeners[1]);
                networkLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

    }

    @SuppressLint("MissingPermission")
    public void stop() {

        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }

    }

    public interface OnTLocationChanged {
        void onLocationChanged(Location location);
    }

    private class LocationListener implements android.location.LocationListener {

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            if (location.getProvider().equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
                gpslocation = location;

            } else {
                networkLocation = location;
            }


            TLocationManager.currentLocation = location;
//            updateDriverLocation(location,mLastLocation,GpsService.this);
//            mLastLocation.set(location);

            if (onTLocationChanged != null)
                onTLocationChanged.onLocationChanged(location);

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    public  boolean checkGPSEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
