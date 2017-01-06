package ravtrix.backpackerbuddy.models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ravinder on 3/28/16.
 */
public class UserLocalStore {
    private static final String SP_NAME = "userDetails";
    private SharedPreferences userLocalDataStore;
    private static LoggedInUser storedUser;

    public UserLocalStore(Context context) {
        userLocalDataStore = context.getSharedPreferences(SP_NAME, 0);

    }
    public void storeUserData(LoggedInUser loggedInUser) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putInt("userID", loggedInUser.getUserID());
        spEditor.putString("email", loggedInUser.getEmail());
        spEditor.putString("username", loggedInUser.getUsername());
        spEditor.putInt("traveling", loggedInUser.getTraveling());
        spEditor.putInt("bucketStatus", loggedInUser.getBucketStatus());
        spEditor.putString("userImageURL", loggedInUser.getUserImageURL());
        spEditor.putLong("latitude", Double.doubleToRawLongBits(loggedInUser.getLatitude()));
        spEditor.putLong("longitude", Double.doubleToRawLongBits(loggedInUser.getLongitude()));
        spEditor.putLong("time", loggedInUser.getTime());
        spEditor.putString("gender", loggedInUser.getGender());

        spEditor.apply();
    }

    public void changelatitude(double latitude) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putLong("latitude", Double.doubleToRawLongBits(latitude));
        spEditor.apply();
    }

    public void changeLongitude(double longitude) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putLong("longitude", Double.doubleToRawLongBits(longitude));
        spEditor.apply();
    }

    public void changeTime(Long newTime) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putLong("time", newTime);
        spEditor.apply();
    }

    public void changeEmail(String newEmail) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putString("email", newEmail);
        spEditor.apply();
    }

    public void changeImageURL(String newImage) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putString("userImageURL", newImage);
        spEditor.apply();
    }

    public void changeTravelStat(int status) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putInt("traveling", status);
        spEditor.apply();
    }

    public void changeBucketStat(int status) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putInt("bucketStatus", status);
        spEditor.apply();
    }

    // Return current logged in user if exist
    public LoggedInUser getLoggedInUser() {
        int userID = userLocalDataStore.getInt("userID", 0);
        int traveling = userLocalDataStore.getInt("traveling", 0);
        int bucketStatus = userLocalDataStore.getInt("bucketStatus", 0);
        String email = userLocalDataStore.getString("email", "");
        String username = userLocalDataStore.getString("username", "");
        String userImageURL = userLocalDataStore.getString("userImageURL", "");
        String gender = userLocalDataStore.getString("gender", "");
        double latitude = Double.longBitsToDouble(userLocalDataStore.getLong("latitude", 0));
        double longitude = Double.longBitsToDouble(userLocalDataStore.getLong("longitude", 0));
        long time = userLocalDataStore.getLong("time", 0);

        storedUser = new LoggedInUser(userID, email, username, userImageURL, gender, traveling, bucketStatus,
                latitude, longitude, time);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();
    }
    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.clear();
        spEditor.apply();
    }
}
