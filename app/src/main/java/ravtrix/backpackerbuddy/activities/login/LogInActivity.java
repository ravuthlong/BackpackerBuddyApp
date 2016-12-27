package ravtrix.backpackerbuddy.activities.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.WelcomeActivity;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSendBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 3/29/16.
 */
public class LogInActivity extends OptionMenuSendBaseActivity implements ILogInView {

    @BindView(R.id.etLoggedInUsername) protected EditText etLoggedInUsername;
    @BindView(R.id.etLoggedInPassword) protected EditText etLoggedInPassword;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private UserLocalStore userLocalStore;
    private LogInPresenter logInPresenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Log In");
        logInPresenter = new LogInPresenter(this);
        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSend:
                logUserIn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Log user in if right credentials given by the user
     * Animate bouncing effect if the field is empty
     */
    private void logUserIn() {
        String username = etLoggedInUsername.getText().toString();
        String password = etLoggedInPassword.getText().toString();
        String token = FirebaseInstanceId.getInstance().getToken();

        // Only continue if google play services is available
        if (Helpers.checkPlayServices(this)) {
            if (username.isEmpty() && password.isEmpty()) {
                // Animation bounce if username AND password fields entered are empty
                YoYo.with(Techniques.Bounce).duration(500).playOn(findViewById(R.id.etLoggedInUsername));
                YoYo.with(Techniques.Bounce).duration(500).playOn(findViewById(R.id.etLoggedInPassword));

            } else if (username.isEmpty()) {
                // Animation bounce if username field entered is empty
                YoYo.with(Techniques.Bounce).duration(500).playOn(findViewById(R.id.etLoggedInUsername));
            } else if (password.isEmpty()) {
                // Animation bounce if password field entered is empty
                YoYo.with(Techniques.Bounce).duration(500).playOn(findViewById(R.id.etLoggedInPassword));
            } else {
                logInPresenter.logUserIn(username, password, token);
            }
        }
    }

    // Error if the user info is incorrect
    @Override
    public void showNoUserErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    @Override
    public void showProgressDialog() {
        this.progressDialog = Helpers.showProgressDialog(this, "Logging In...");
    }

    @Override
    public void hideProgressDialog() {
        Helpers.hideProgressDialog(progressDialog);
    }

    @Override
    public void saveNewUser(LoggedInUser loggedInUser) {
        userLocalStore.clearUserData();
        userLocalStore.storeUserData(loggedInUser);
    }

    @Override
    public void userCanLogIn() {
        startActivity(new Intent(LogInActivity.this, UserMainPage.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logInPresenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, WelcomeActivity.class));
    }
}
