package ravtrix.backpackerbuddy.activities.userinfoedit.changeemail;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class ChangeEmail extends OptionMenuSaveBaseActivity implements IChangeEmailView {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.tvEmail_change) protected EditText newEmail;
    @BindView(R.id.tvPassword_confirm) protected EditText passwordConfirm;
    private UserLocalStore userLocalStore;
    private ChangeEmailPresenter changeEmailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        this.setTitle("Change email");
        userLocalStore = new UserLocalStore(this);
        changeEmailPresenter = new ChangeEmailPresenter(this);

        newEmail.setText(userLocalStore.getLoggedInUser().getEmail());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.submitSave:
                if (haveMissingField()) {
                    Helpers.displayToast(ChangeEmail.this, "You have missing fields");
                } else if (!Helpers.isEmailValid(newEmail.getText().toString())) {
                    Helpers.displayToast(ChangeEmail.this, "Email not in valid format (aaa@aaa.aaa)");
                } else {
                    // check if email is taken
                    changeEmailPresenter.checkEmailTaken(newEmail.getText().toString().trim());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Retrofit call to update email
     */
    @Override
    public void changeEmail() {

        final HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("email", newEmail.getText().toString().trim());
        userInfo.put("username", userLocalStore.getLoggedInUser().getUsername());
        userInfo.put("password", passwordConfirm.getText().toString());
        changeEmailPresenter.changeEmail(userInfo);
    }

    /**
     * Check if the edit texts have missing fields
     * @return true is missing fields, else false
     */
    private boolean haveMissingField() {
        boolean missingInfo = false;

        if (newEmail.getText().toString().isEmpty() ||
                passwordConfirm.getText().toString().isEmpty()) {
            missingInfo = true;
        }
        return missingInfo;
    }

    @Override
    public void displayEmailTaken() {
        Helpers.displayToast(ChangeEmail.this, "Email taken. Try a different one.");
    }

    @Override
    public void displayEmailChanged() {
        Helpers.displayToast(ChangeEmail.this, "Email changed successfully");
    }

    @Override
    public void displayWrongPassword() {
        Helpers.displayToast(ChangeEmail.this, "Incorrect password");
    }

    @Override
    public void finished() {
        finish();
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(ChangeEmail.this);
    }

    @Override
    public void changeEmailLocalstore() {
        userLocalStore.changeEmail(newEmail.getText().toString().trim());
    }
}
