package ravtrix.backpackerbuddy.activities.signup1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.squareup.leakcanary.LeakCanary;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.activities.signup2.SignUpPart2Activity;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSendBaseActivity;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart1Activity extends OptionMenuSendBaseActivity implements  View.OnClickListener {

    @BindView(R.id.etEmail) protected EditText etEmail;
    @BindView(R.id.etUsername) protected EditText etUsername;
    @BindView(R.id.etPassword) protected EditText etPassword;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
        LeakCanary.install(getApplication());
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitSend:

                final String username = etUsername.getText().toString();
                final String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                //User signedUpUser = new User(email, username, password);

                final ProgressDialog progressDialog = Helpers.showProgressDialog(this, "");

                // Prepare HashMap to send over to the database
                HashMap<String, String> signedUpUser = new HashMap<>();
                signedUpUser.put("email", email);
                signedUpUser.put("username", username);
                signedUpUser.put("password", password);

                // Make Retrofit call to communicate with the server
                Call<JsonObject> returnedStatus = RetrofitUserInfoSingleton.getRetrofitUserInfo().signUserUpPart1().signedUpStatus(signedUpUser);
                returnedStatus.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject status = response.body();

                        int userStatus = status.get("status").getAsInt();

                        // Sign up success
                        if (userStatus == 1) {
                            LoggedInUser user = new LoggedInUser();
                            user.setUsername(username);
                            user.setEmail(email);
                            user.setUserID(status.get("id").getAsInt());

                            userLocalStore.storeUserData(user);
                            startActivity(new Intent(SignUpPart1Activity.this, SignUpPart2Activity.class));
                        } else {
                            // User has been taken
                            Helpers.showAlertDialog(SignUpPart1Activity.this, "User Taken");
                        }

                        Helpers.hideProgressDialog(progressDialog);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Helpers.displayToast(SignUpPart1Activity.this, "Error signing up. Try again.");
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
