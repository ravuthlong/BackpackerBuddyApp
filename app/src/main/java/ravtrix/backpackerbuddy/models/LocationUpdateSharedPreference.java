package ravtrix.backpackerbuddy.models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ravinder on 1/18/17.
 */

public class LocationUpdateSharedPreference {
    private static final String SP_NAME = "LOCATION_UPDATE";
    private SharedPreferences updatePref;

    public LocationUpdateSharedPreference(Context context) {
        updatePref = context.getSharedPreferences(SP_NAME, 0);
    }

    public void setUpValue() {
        SharedPreferences.Editor spEditor = updatePref.edit();
        spEditor.putBoolean("isUpdating", false);
        spEditor.apply();
    }

    public void setLocationUpdating() {
        SharedPreferences.Editor spEditor = updatePref.edit();
        spEditor.putBoolean("isUpdating", true);
        spEditor.apply();
    }

    public void setLocationNotUpdating() {
        SharedPreferences.Editor spEditor = updatePref.edit();
        spEditor.putBoolean("isUpdating", false);
        spEditor.apply();
    }

    public boolean isLocationUpdating() {
        return updatePref.getBoolean("isUpdating", false);
    }
}
