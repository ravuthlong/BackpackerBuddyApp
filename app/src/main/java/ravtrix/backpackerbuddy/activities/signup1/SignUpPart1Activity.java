package ravtrix.backpackerbuddy.activities.signup1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.leakcanary.LeakCanary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class SignUpPart1Activity extends OptionMenuSendBaseActivity implements  View.OnClickListener {

    @BindView(R.id.etEmail) protected EditText etEmail;
    @BindView(R.id.etUsername) protected EditText etUsername;
    @BindView(R.id.etPassword) protected EditText etPassword;
    @BindView(R.id.etConfirmPass) protected  EditText etConfirmPassword;
    @BindView(R.id.toolbar) protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
        LeakCanary.install(getApplication());
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);

    }

    @Override
    public void onClick(View v) {

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

        String errorFields = "";

        if (username.matches("")) {
            errorFields += "Missing Username \n";
        }
        if (password.matches("")) {
            errorFields += "Missing Password \n";
        } else if (!passwordLength(password)) {
            errorFields += "Password must be at least 6 characters long\n";
        }
        if (email.matches("")) {
            errorFields += "Missing Email \n";
        } else if (!isEmailValid(email)) {
            errorFields += "Email is in invalid format (Ex. xxxx@xxx.xxx)\n";
        }
        if(!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
            errorFields += "Password must match \n";
        }
        if (errorFields.length() > 0) {
            Toast toast= Toast.makeText(getApplicationContext(),
                    errorFields, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {
            startSignUpPart2(email, username, password);
        }
    }

    private void startSignUpPart2(final String email, final String username, final String password) {
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

    // Validate email input format
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    // Validate password length, must be 6 or more
    public static boolean passwordLength(String password) {
        boolean isValid = true;
        final int passwordLengthMin = 6;

        if (password.length() < passwordLengthMin) {
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, WelcomeActivity.class));
    }
}
