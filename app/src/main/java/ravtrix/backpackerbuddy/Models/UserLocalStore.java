package ravtrix.backpackerbuddy.models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ravinder on 3/28/16.
 */
public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    private SharedPreferences userLocalDataStore;
    private static User storedUser;
    public static boolean isUserLoggedIn = false;
    public static int visitCounter = 0;
    public static boolean allowRefresh = false;

    public UserLocalStore(Context context) {
        userLocalDataStore = context.getSharedPreferences(SP_NAME, 0);
    }
    public void storeUserData(LoggedInUser loggedInUser) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putInt("userID", loggedInUser.getUserID());
        spEditor.putString("email", loggedInUser.getEmail());
        spEditor.putString("username", loggedInUser.getUsername());
        spEditor.apply();
    }

    // Return current logged in user if exist
    public User getLoggedInUser() {
        int userID = userLocalDataStore.getInt("userID", 0);
        String email = userLocalDataStore.getString("email", "");
        String username = userLocalDataStore.getString("username", "");

        storedUser = new User(userID, email, username);
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
