package ravtrix.backpackerbuddy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.HelpersPermission;
import ravtrix.backpackerbuddy.interfacescom.UserLocationInterface;

/**
 * Created by Ravinder on 9/13/16.
 */
public class UserLocation {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Activity context;
    private UserLocationInterface userLocationInterface;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private static final int LOCATION_REQUEST_CODE = 1;
    private boolean isFacebookSignUpCalling = false;
    private static int ONE_SECOND = 1000;

    public UserLocation(Activity context) {
        this.context = context;
    }

    /**
     * Start location service to collect latitude and longitude
     * @param userLocationInterface         Interface to detect when location is received
     */
    public void startLocationService(UserLocationInterface userLocationInterface) {
        this.userLocationInterface = userLocationInterface;

        if (context != null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            setLocationListener();
            checkPermission();
        }
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
                }, 0);
            } else {
                configureButton();
            }
        } else {
            configureButton();
        }
    }


    /**
     * Listener for location, listens for when location is changed
     */
    private void setLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                final double longitude = location.getLongitude();
                final double latitude = location.getLatitude();
                stopListener();
                // Pass values over to sign up activity part 1
                userLocationInterface.onReceivedLocation(latitude, longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                // Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                //context.startActivity(intent);
            }
        };
    }

    /**
     * Cause location listener to start listening
     */
    public void configureButton() {
        try {

            if (!gps_enabled && !network_enabled && isFacebookSignUpCalling) {

                AlertDialog.Builder builder = Helpers.showAlertDialogWithTwoOptions(context, "Location Service", "Turn on location service so other users can see your current country", "No");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userLocationInterface.onReceivedLocation(0, 0); // permission denied, just use 0,0
                    }
                });
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // requesting permission to access location
                        HelpersPermission.showLocationRequest(context);
                    }
                });
                builder.show();

            } else {
                if (gps_enabled){
                    // One_Second passed because on location changed can be called multiple times in a few users
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UserLocation.ONE_SECOND, 1, locationListener);
                } else if (network_enabled) {
                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UserLocation.ONE_SECOND, 1, locationListener);
                }
            }

        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    private void stopListener() {
        try {
            // Turn off location listeners after successful location retrieval
            if (locationManager != null) {
                locationManager.removeUpdates(locationListener);
                locationManager = null;
                locationListener = null;
                context = null;
            }
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setIsFacebookCalling(boolean isFacebookCalling) {
        this.isFacebookSignUpCalling = isFacebookCalling;
    }
}