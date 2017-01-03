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

public class ChangePassword extends OptionMenuSaveBaseActivity {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.tvUserPassword_change) protected EditText userPassword;
    @BindView(R.id.tvNewPassword_change) protected EditText newPassword;
    @BindView(R.id.tvNewPassword2_change) protected EditText newPassword2;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        this.setTitle("Change password");
        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:

                if (haveMissingField()) {
                    Helpers.displayToast(ChangePassword.this, "You have missing fields");
                } else if (!isNewPasswordMatch(newPassword.getText().toString(), newPassword2.getText().toString())) {
                    Helpers.displayToast(ChangePassword.this, "New password does not match");
                } else {
                    changePassword(userPassword.getText().toString(), newPassword.getText().toString());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Retrofit call to update password
     * @param oldPassword       old password
     * @param newPassword       new password
     */
    private void changePassword(String oldPassword, String newPassword) {

        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("username", userLocalStore.getLoggedInUser().getUsername());
        userInfo.put("oldpassword", oldPassword);
        userInfo.put("newpassword", newPassword);

        Call<JsonObject> passwordCheck = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .changePassword()
                .changePassword(userInfo);

        passwordCheck.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body().get("status").getAsInt() == 1) {
                    Helpers.displayToast(ChangePassword.this, "Password changed successfully");
                }  else {
                    // Changed unsuccessfully
                    Helpers.displayToast(ChangePassword.this, "Problem changing password");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(ChangePassword.this, "Problem changing password");
            }
        });
    }


    /**
     * Check if the new password and new confirm password matches
     * @param newPassword       the new password
     * @param newPassword2      the new password retyped
     * @return                  true is matched, else false
     */
    private boolean isNewPasswordMatch(String newPassword, String newPassword2) {
        return newPassword.equals(newPassword2);
    }

    /**
     * Check if the edit texts have missing fields
     * @return true is missing fields, else false
     */
    private boolean haveMissingField() {
        boolean missingInfo = false;

        if (userPassword.getText().toString().isEmpty() ||
                newPassword.getText().toString().isEmpty()||
                newPassword2.getText().toString().isEmpty()) {
            missingInfo = true;
        }
        return missingInfo;
    }

}
