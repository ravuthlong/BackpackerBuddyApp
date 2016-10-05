package ravtrix.backpackerbuddy.activities.signup1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.squareup.leakcanary.LeakCanary;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.UserLocation;
import ravtrix.backpackerbuddy.activities.WelcomeActivity;
import ravtrix.backpackerbuddy.activities.signup2.SignUpPart2Activity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
        LeakCanary.install(getApplication());
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        signUpPart1Presenter = new SignUpPart1Presenter(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSend:
                inputValidationAndNextStep();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Validate that user input is in correct format
    public void inputValidationAndNextStep() {

        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
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
        new UserLocation(getApplicationContext(), new UserLocationInterface() {
            @Override
            public void onReceivedLocation(double latitude, double longitude) {

                Intent intent = new Intent(SignUpPart1Activity.this, SignUpPart2Activity.class);
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
    public void displayUserTakenDialog() {
        Helpers.showAlertDialog(this, "User has been taken...");
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayToast(this, "Error. Try again...");
    }
}
