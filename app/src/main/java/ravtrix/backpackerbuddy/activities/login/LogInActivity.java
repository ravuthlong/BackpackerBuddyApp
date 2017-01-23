package ravtrix.backpackerbuddy.activities.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.activities.startingpage.WelcomeActivity;
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
                Helpers.displayToast(this, "Empty fields");
            } else if (username.isEmpty()) {
                Helpers.displayToast(this, "Empty username");
            } else if (password.isEmpty()) {
                Helpers.displayToast(this, "Empty password");
            } else {
                logInPresenter.logUserIn(username, password, token);
            }
        }
    }

    // Error if the user info is incorrect
    @Override
    public void showNoUserErrorMessage(){
        AlertDialog.Builder dialogBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            dialogBuilder = new AlertDialog.Builder(this);
        }
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
        Intent intent = new Intent(LogInActivity.this, UserMainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
