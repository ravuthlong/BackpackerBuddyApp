package ravtrix.backpackerbuddy.activities.signup1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.UserLocation;
import ravtrix.backpackerbuddy.activities.startingpage.WelcomeActivity;
import ravtrix.backpackerbuddy.activities.signup2.SignUpPart3Activity;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSendBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.interfacescom.UserLocationInterface;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart1Activity extends OptionMenuSendBaseActivity implements ISignUpPart1View {

    @BindView(R.id.etEmail) protected EditText etEmail;
    @BindView(R.id.etUsername) protected EditText etUsername;
    @BindView(R.id.etPassword) protected EditText etPassword;
    @BindView(R.id.etConfirmPass) protected  EditText etConfirmPassword;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private SignUpPart1Presenter signUpPart1Presenter;
    private ProgressDialog progressDialog;
    private UserLocation userLocation;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Sign Up Part 1");
        signUpPart1Presenter = new SignUpPart1Presenter(this);
        userLocation = new UserLocation(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Prevents double clicking
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (item.getItemId()) {
            case R.id.submitSend:
                // Regular sign up
                inputValidationAndNextStep();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Validate that user input is in correct format
    private void inputValidationAndNextStep() {

        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPass = etConfirmPassword.getText().toString();
        signUpPart1Presenter.inputValidation(username, password, email, confirmPass);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    @Override
    public void startSignUpPart2Activity(final String email, final String username, final String password) {
        progressDialog = Helpers.showProgressDialog(this, "Signing Up. Please wait...");

        userLocation.startLocationService(new UserLocationInterface() {
            @Override
            public void onReceivedLocation(double latitude, double longitude) {
                Helpers.hideProgressDialog(progressDialog);

                Intent intent = new Intent(SignUpPart1Activity.this, SignUpPart3Activity.class);
                intent.putExtra("email", email);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });
    }

    @Override
    public void displayUsernameTakenDialog() {
        Helpers.showAlertDialog(this, "Username has been taken...");
    }

    @Override
    public void displayEmailTakenDialog() {
        Helpers.showAlertDialog(this, "Email has been taken...");
    }

    @Override
    public void displayUsernameAndEmailTakenDialog() {
        Helpers.showAlertDialog(this, "Username and Email have been taken...");
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayToast(this, "Error. Try again...");
    }

    // Case for users with grant access needed. Location access granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    userLocation.configureButton();
                }
                break;
        }
    }
}
