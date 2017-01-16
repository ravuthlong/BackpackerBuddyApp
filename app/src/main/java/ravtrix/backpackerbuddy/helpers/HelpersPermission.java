package ravtrix.backpackerbuddy.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * Created by Ravinder on 1/15/17.
 */

public class HelpersPermission {

    public static final int LOCATION_REQUEST_CODE = 10;

    private HelpersPermission() {}

    public static boolean hasLocationPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public static void showLocationRequest(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION }, LOCATION_REQUEST_CODE);
    }

    public static void showLocationRequestTab(Fragment context) {
        context.requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
    }
}
