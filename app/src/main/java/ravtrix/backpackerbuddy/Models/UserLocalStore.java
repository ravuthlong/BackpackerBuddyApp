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
        spEditor.putInt("bucketStatus", loggedInUser.getBucketStatus());
        spEditor.putString("userImageURL", loggedInUser.getUserImageURL());

        if (loggedInUser.getLatitude() > 0) { // Double parsing on 0 might crash
            spEditor.putLong("latitude", Double.doubleToRawLongBits(loggedInUser.getLatitude()));
            spEditor.putLong("longitude", Double.doubleToRawLongBits(loggedInUser.getLongitude()));
        }

        spEditor.putLong("time", loggedInUser.getTime());
        spEditor.putInt("isFacebook", loggedInUser.getIsFacebook());
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

    public void changeUsername(String newUsername) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putString("username", newUsername);
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

    public void changeBucketStat(int status) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putInt("bucketStatus", status);
        spEditor.apply();
    }

    // Return current logged in user if exist
    public LoggedInUser getLoggedInUser() {
        int userID = userLocalDataStore.getInt("userID", 0);
        int bucketStatus = userLocalDataStore.getInt("bucketStatus", 0);
        String email = userLocalDataStore.getString("email", "");
        String username = userLocalDataStore.getString("username", "");
        String userImageURL = userLocalDataStore.getString("userImageURL", "");
        double latitude = Double.longBitsToDouble(userLocalDataStore.getLong("latitude", 0));
        double longitude = Double.longBitsToDouble(userLocalDataStore.getLong("longitude", 0));
        long time = userLocalDataStore.getLong("time", 0);
        int isFacebook = userLocalDataStore.getInt("isFacebook", 0);

        storedUser = new LoggedInUser(userID, email, username, userImageURL, bucketStatus,
                latitude, longitude, time, isFacebook);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();
    }

    public String getEmail() {
        return userLocalDataStore.getString("email", "");
    }

    public int isFacebook() {
        return userLocalDataStore.getInt("isFacebook", 0);
    }

    public void clearUserData() {
        // Save email before log out. This would make it easier for next log in. No need to retype email.
        String email = userLocalDataStore.getString("email", "");
        int isFacebook = userLocalDataStore.getInt("isFacebook", 0);

        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.clear();
        spEditor.putString("email", email); // save email back
        spEditor.putInt("isFacebook", isFacebook); // save back

        spEditor.apply();
    }
}
