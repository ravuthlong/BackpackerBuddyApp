package ravtrix.backpackerbuddy.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.retrofit.retrofitrequests.retrofituserinforequests.RetrofitUserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 3/29/16.
 */
public class LogInActivity extends AppCompatActivity {

    @BindView(R.id.etLoggedInUsername) protected EditText etLoggedInUsername;
    @BindView(R.id.etLoggedInPassword) protected EditText etLoggedInPassword;
    private UserLocalStore userLocalStore;
    private RetrofitUserInfo retrofitUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLogIn);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

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
        retrofitUser = new RetrofitUserInfo();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_login, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logInContinute:

                logUserIn();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Log user in if right credentials given by the user
     * Animate bouncing effect if the field is empty
     */

    private void logUserIn() {
        String username = etLoggedInUsername.getText().toString();
        String password = etLoggedInPassword.getText().toString();

        if (username.isEmpty() && password.isEmpty()) {
            // Animation bounce if username AND password fields entered are empty
            YoYo.with(Techniques.Bounce).duration(500).playOn(findViewById(R.id.etLoggedInUsername));
            YoYo.with(Techniques.Bounce).duration(500).playOn(findViewById(R.id.etLoggedInPassword));

        } else if (username.isEmpty()) {
            // Animation bounce if username field entered is empty
            YoYo.with(Techniques.Bounce).duration(500).playOn(findViewById(R.id.etLoggedInUsername));
        } else if (password.isEmpty()) {
            // Animation bounce if password field entered is empty
            YoYo.with(Techniques.Bounce).duration(500).playOn(findViewById(R.id.etLoggedInPassword));
        } else {

            // Prepare HashMap of username and password to send to retrofit call
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("username", username);
            arguments.put("password", password);

            Call<LoggedInUser> responseUser = retrofitUser.loggedInUser().userInfo(arguments);
            responseUser.enqueue(new Callback<LoggedInUser>() {
                @Override
                public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                    LoggedInUser user = response.body();

                    if (user.getStatus() == 0) {
                        // User not found. 0 returned from PHP
                        showNoUserErrorMessage();
                    } else {
                        // User authenticated. Log user in
                        userLocalStore.storeUserData(user);
                        startActivity(new Intent(LogInActivity.this, UserMainPage.class));
                    }
                }
                @Override
                public void onFailure(Call<LoggedInUser> call, Throwable t) {
                    System.out.println(t.getMessage());

                }
            });
        }

    }

    // Error if the user info is incorrect
    private void showNoUserErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
}
