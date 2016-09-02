package ravtrix.backpackerbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helper.Helper;
import ravtrix.backpackerbuddy.helper.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart1Activity extends AppCompatActivity implements  View.OnClickListener {

    @BindView(R.id.etEmail) protected EditText etEmail;
    @BindView(R.id.etUsername) protected EditText etUsername;
    @BindView(R.id.etPassword) protected EditText etPassword;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_signup1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitSignup1:

                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                //User signedUpUser = new User(email, username, password);

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
                            user.setUsername(status.get("username").getAsString());
                            user.setEmail(status.get("email").getAsString());
                            user.setUserID(status.get("userID").getAsInt());

                            userLocalStore.storeUserData(user);
                            startActivity(new Intent(SignUpPart1Activity.this, SignUpPart2Activity.class));
                        } else {
                            // User has been taken
                            Helper.showAlertDialog(SignUpPart1Activity.this, "User Taken");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
