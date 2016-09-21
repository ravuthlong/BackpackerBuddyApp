package ravtrix.backpackerbuddy.activities.signup2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.leakcanary.LeakCanary;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.SignUpPart3Activity;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSendBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart2Activity extends OptionMenuSendBaseActivity {

    @BindView(R.id.etFirstname) protected EditText etFirstname;
    @BindView(R.id.etLastname) protected EditText etLastname;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        LeakCanary.install(getApplication());
        ButterKnife.bind(this);

        Helpers.setToolbar(this, toolbar);
        userLocalStore = new UserLocalStore(this);
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

        String lastname = etLastname.getText().toString();
        String firstname = etFirstname.getText().toString();

        String errorFields = "";

        if (lastname.matches("")) {
            errorFields += "Missing Last Name \n";
        } else if (!isAlpha(lastname)) {
            errorFields += "Last Name must only contain letters\n";
        }
        if (firstname.matches("")) {
            errorFields += "Missing First Name \n";
        } else if (!isAlpha(firstname)) {
            errorFields += "First Name must only contain letters\n";
        }
        if (errorFields.length() > 0) {
            Toast toast= Toast.makeText(getApplicationContext(),
                    errorFields, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {
            startSignUpPart3(firstname, lastname);
        }
    }

    // Validate string must only contain letters
    private boolean isAlpha(String text) {
        char[] chars = text.toCharArray();
        boolean isValid = true;

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                isValid = false;
            }
        }
        return isValid;
    }

    private void startSignUpPart3(String firstname, String lastname) {
        String username = "";
        String password = "";
        String email = "";
        Double longitude = 0.0;
        Double latitude = 0.0;

        Bundle signUp1Info = getIntent().getExtras();
        if (signUp1Info != null) {
            username = signUp1Info.getString("username");
            password = signUp1Info.getString("password");
            email = signUp1Info.getString("email");
            longitude = signUp1Info.getDouble("longitude");
            latitude = signUp1Info.getDouble("latitude");
        }

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
}
