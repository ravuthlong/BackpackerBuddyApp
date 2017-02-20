package ravtrix.backpackerbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.activities.startingpage.WelcomeActivity;
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class SplashActivity extends AppCompatActivity {
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLocalStore = new UserLocalStore(this);
        if (checkIsUserNotLoggedIn()) {
            // Non logged in user goes to logged in page
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Non logged in user goes to logged in page
            Intent intent = new Intent(this, UserMainPage.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Check to see if the user is already logged in. If not, go to log in page
     */
    private boolean checkIsUserNotLoggedIn() {
        return (userLocalStore.getLoggedInUser().getUserID() == 0);
    }
}
