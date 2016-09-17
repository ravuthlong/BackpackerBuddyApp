package ravtrix.backpackerbuddy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import ravtrix.backpackerbuddy.interfacescom.UserLocationInterface;

/**
 * Created by Ravinder on 9/13/16.
 */
public class UserLocation extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Context context;
    private UserLocationInterface userLocationInterface;

    public UserLocation() {}

    public UserLocation(Context context, UserLocationInterface userLocationInterface) {
        this.context = context;
        this.userLocationInterface = userLocationInterface;
        startLocationService();
    }

    public void startLocationService() {
        setLocationListener();
        checkPermission();
    }


    private void checkPermission() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Check for user's SDK Version. SDK version >= Marshmallow need permission access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 1);
            }
        } else {
            configureButton();
        }
    }


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

    // Case for users with grant access needed. Location access granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configureButton();
                }
                break;
        }
    }

    public void stopListener() {
        try {
            locationManager.removeUpdates(locationListener);
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    // User with granted access can access the location now
    private void configureButton() {
        try {
            // Refresh every 10 seconds .. moved 0 meters from previous location
            locationManager.requestLocationUpdates("gps", 10000, 0, locationListener);
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }

    }

}