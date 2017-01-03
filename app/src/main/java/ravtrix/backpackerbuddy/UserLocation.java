package ravtrix.backpackerbuddy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import ravtrix.backpackerbuddy.interfacescom.UserLocationInterface;

/**
 * Created by Ravinder on 9/13/16.
 */
public class UserLocation {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Activity context;
    private UserLocationInterface userLocationInterface;

    public UserLocation(Activity context) {
        this.context = context;
    }

    /**
     * Start location service to collect latitude and longitude
     * @param userLocationInterface         Interface to detect when location is received
     */
    public void startLocationService(UserLocationInterface userLocationInterface) {
        this.userLocationInterface = userLocationInterface;
        setLocationListener();
        checkPermission();
    }


    /**
     * Check if permission to use GPS is given, if not display option to enable
     */
    private void checkPermission() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Check for user's SDK Version. SDK version >= Marshmallow need permission access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {

                context.requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 1);
            } else {
                configureButton();
            }
        } else {
            configureButton();
        }
    }


    /**
     * Listener for location
     */
    private void setLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                final double longitude = location.getLongitude();
                final double latitude = location.getLatitude();
                // Pass values over to sign up activity part 1
                userLocationInterface.onReceivedLocation(latitude, longitude);
                stopListener();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                // When GPS setting is turned off
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        };
    }

    private void stopListener() {
        try {
            // Turn off location listeners after successful location retrieval
            if (locationManager != null) {
                locationManager.removeUpdates(locationListener);
                locationManager = null;
                locationListener = null;
            }
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cause location listener to start listening
     */
    public void configureButton() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }
}