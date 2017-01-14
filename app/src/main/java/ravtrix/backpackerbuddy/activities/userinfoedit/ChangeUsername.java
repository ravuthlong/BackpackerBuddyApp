package ravtrix.backpackerbuddy.activities.userinfoedit;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

public class ChangeUsername extends OptionMenuSaveBaseActivity {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.tvUsername_change) protected EditText etNewUsername;
    @BindView(R.id.tvPassword_confirm_username) protected EditText etPasswordConfirm;
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
                    checkUsernameTaken(etNewUsername.getText().toString().trim());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Check is new username is already taken
     * @param newUsername           the new username
     */
    private void checkUsernameTaken(final String newUsername) {

        Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .isUsernameOrEmailTaken()
                .isUsernameOrEmailTaken(newUsername, "");

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("usernametaken").getAsInt() == 1) {
                    Helpers.displayToast(ChangeUsername.this, "Username taken. Try a different one.");
                } else {

                    if (userLocalStore.getLoggedInUser().getIsFacebook() == 1) {
                        // Facebook user
                        HashMap<String, String> userInfo = new HashMap<>();
                        userInfo.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                        userInfo.put("newUsername", etNewUsername.getText().toString().trim());
                        userInfo.put("username", oldUsername);
                        changeUsernameFacebook(userInfo);
                    } else {
                        // Regular user
                        HashMap<String, String> userInfo = new HashMap<>();
                        userInfo.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                        userInfo.put("newUsername", etNewUsername.getText().toString().trim());
                        userInfo.put("password", etPasswordConfirm.getText().toString());
                        userInfo.put("username", oldUsername);
                        changeUsername(userInfo); // Change username
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    private void changeUsernameFacebook(final HashMap<String, String> userInfo) {

        Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .changeUsernameFacebook()
                .changeusernameFacebook(userInfo);
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 1) {
                    Helpers.displayToast(ChangeUsername.this, "Username changed successfully");
                    userLocalStore.changeUsername(userInfo.get("newUsername"));
                    finish();
                }  else {
                    // Changed unsuccessfully
                    Helpers.displayErrorToast(ChangeUsername.this);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayErrorToast(ChangeUsername.this);
            }
        });
    }

    /**
     * Change username of user
     * @param userInfo          Hash map of user information for retrofit
     */
    private void changeUsername(final HashMap<String, String> userInfo) {

        Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .changeUsername()
                .changeusername(userInfo);
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 1) {
                    Helpers.displayToast(ChangeUsername.this, "Username changed successfully");
                    userLocalStore.changeUsername(userInfo.get("newUsername"));
                    finish();
                }  else {
                    // Changed unsuccessfully
                    Helpers.displayToast(ChangeUsername.this, "Incorrect password");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayErrorToast(ChangeUsername.this);
            }
        });
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
}
