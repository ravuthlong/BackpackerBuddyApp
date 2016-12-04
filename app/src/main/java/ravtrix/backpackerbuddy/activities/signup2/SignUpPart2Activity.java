package ravtrix.backpackerbuddy.activities.signup2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.signup3.SignUpPart3Activity;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSendBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart2Activity extends OptionMenuSendBaseActivity implements ISignUpPart2View {

    @BindView(R.id.etFirstname) protected EditText etFirstname;
    @BindView(R.id.etLastname) protected EditText etLastname;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private SignUpPart2Presenter signUpPart2Presenter;
    private String username = "";
    private String password = "";
    private String email = "";
    private Double longitude = 0.0;
    private Double latitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Sign Up Part 2");
        signUpPart2Presenter = new SignUpPart2Presenter(this);
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

        String lastname = etLastname.getText().toString().trim();
        String firstname = etFirstname.getText().toString().trim();
        signUpPart2Presenter.inputValidation(firstname, lastname);
    }

    @Override
    public void displayInputErrorToast(String errorFields) {
        Toast toast= Toast.makeText(this,
                errorFields, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    @Override
    public void startSignUpPart3(String firstname, String lastname) {

        getBundleExtra();

        Intent intent = new Intent(SignUpPart2Activity.this, SignUpPart3Activity.class);
        intent.putExtra("email", email);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("firstname", firstname);
        intent.putExtra("lastname", lastname);
        startActivity(intent);
    }

    private void getBundleExtra() {
        Bundle signUp1Info = getIntent().getExtras();
        if (signUp1Info != null) {
            username = signUp1Info.getString("username");
            password = signUp1Info.getString("password");
            email = signUp1Info.getString("email");
            longitude = signUp1Info.getDouble("longitude");
            latitude = signUp1Info.getDouble("latitude");
        }
    }
}
