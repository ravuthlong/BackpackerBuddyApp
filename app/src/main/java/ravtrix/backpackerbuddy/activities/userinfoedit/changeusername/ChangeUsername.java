package ravtrix.backpackerbuddy.activities.userinfoedit.changeusername;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class ChangeUsername extends OptionMenuSaveBaseActivity implements IChangeUsernameView {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.tvUsername_change) protected EditText etNewUsername;
    @BindView(R.id.tvPassword_confirm_username) protected EditText etPasswordConfirm;
    private ChangeUsernamePresenter changeUsernamePresenter;
    private UserLocalStore userLocalStore;
    private String oldUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        this.setTitle("Change username");
        userLocalStore = new UserLocalStore(this);
        changeUsernamePresenter = new ChangeUsernamePresenter(this);

        if (userLocalStore.getLoggedInUser().getIsFacebook() == 1) {
            etPasswordConfirm.setVisibility(View.GONE);
        }
        this.oldUsername = userLocalStore.getLoggedInUser().getUsername();
        etNewUsername.setText(oldUsername);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:
                if (haveMissingField()) {
                    Helpers.displayToast(ChangeUsername.this, "You have missing fields");
                } else {
                    changeUsernamePresenter.checkUsernameTaken(etNewUsername.getText().toString().trim());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Check if the edit texts have missing fields
     * @return true is missing fields, else false
     */
    private boolean haveMissingField() {
        boolean missingInfo = false;

        if (userLocalStore.getLoggedInUser().getIsFacebook() == 1) {
            if (etNewUsername.getText().toString().isEmpty()) {
                missingInfo = true;
            }
        } else {
            if (etNewUsername.getText().toString().isEmpty() ||
                    etPasswordConfirm.getText().toString().isEmpty()) {
                missingInfo = true;
            }
        }
        return missingInfo;
    }

    @Override
    public void displayUsernameTaken() {
        Helpers.displayToast(ChangeUsername.this, "Username taken. Try a different one.");
    }

    @Override
    public void displayUsernameChanged() {
        Helpers.displayToast(ChangeUsername.this, "Username changed successfully");
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(ChangeUsername.this);
    }

    @Override
    public UserLocalStore getUserLocalStore() {
        return this.userLocalStore;
    }

    @Override
    public String getOldUsername() {
        return oldUsername;
    }

    @Override
    public String getNewUsername() {
        return etNewUsername.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return etPasswordConfirm.getText().toString();
    }

    @Override
    public void finished() {
        finish();
    }

    @Override
    public void setNewLocalStore(String newUsername) {
        userLocalStore.changeUsername(newUsername);
    }

    @Override
    public void displayErrorWrongPassword() {
        Helpers.displayToast(ChangeUsername.this, "Incorrect password");
    }
}
