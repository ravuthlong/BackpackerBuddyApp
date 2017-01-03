package ravtrix.backpackerbuddy.activities.userinfoedit;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.gson.JsonObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeEmail extends OptionMenuSaveBaseActivity {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.tvEmail_change) protected EditText newEmail;
    @BindView(R.id.tvPassword_confirm) protected EditText passwordConfirm;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setToolbarTitle();
        userLocalStore = new UserLocalStore(this);
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
                    changeEmail(newEmail.getText().toString(), passwordConfirm.getText().toString());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Retrofit call to update email
     * @param email         new email
     * @param password      user password
     */
    private void changeEmail(final String email, String password) {

        final HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("email", email);
        userInfo.put("username", userLocalStore.getLoggedInUser().getUsername());
        userInfo.put("password", password);

        Call<JsonObject> changeEmail = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .changeEmail()
                .changeEmail(userInfo);

        changeEmail.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body().get("status").getAsInt() == 1) {
                    Helpers.displayToast(ChangeEmail.this, "Email changed successfully");
                    userLocalStore.changeEmail(email);
                }  else {
                    // Changed unsuccessfully
                    Helpers.displayToast(ChangeEmail.this, "Problem changing email1");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(ChangeEmail.this, "Problem changing email");
            }
        });
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

    private void setToolbarTitle() {
        this.setTitle("Change email");
    }
}
