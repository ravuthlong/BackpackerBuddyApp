package ravtrix.backpackerbuddy.activities.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.forgotpassword.ForgotPasswordActivity;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.activities.startingpage.WelcomeActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 3/29/16.
 */
public class LogInActivity extends AppCompatActivity implements ILogInView, View.OnClickListener {

    @BindView(R.id.etLoggedInUsername) protected EditText etLoggedInUsername;
    @BindView(R.id.etLoggedInPassword) protected EditText etLoggedInPassword;
    @BindView(R.id.activity_login_linear) protected LinearLayout linearLayout;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_login_tvLogin) protected TextView tvLogin;
    @BindView(R.id.activity_login_tvForgotPassword) protected TextView tvForgotPassword;
    private UserLocalStore userLocalStore;
    private LogInPresenter logInPresenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, linearLayout);
        setTitle("Log In");

        tvLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

        logInPresenter = new LogInPresenter(this);
        userLocalStore = new UserLocalStore(this);

        setUsernameIfAvailable();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_login_tvLogin:
                logUserIn();
                break;
            case R.id.activity_login_tvForgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            default:
                break;
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

    // Set username as email if email is not empty is past local store.
    // This is the case if user logged out before and user is not facebook user.
    private void setUsernameIfAvailable() {
        if ((!userLocalStore.getEmail().isEmpty()) && (userLocalStore.isFacebook() == 0)) {
            etLoggedInUsername.setText(userLocalStore.getEmail());
        }
    }
}
