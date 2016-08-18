package ravtrix.backpackerbuddy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.Models.LoggedInUser;
import ravtrix.backpackerbuddy.Models.User;
import ravtrix.backpackerbuddy.Models.UserLocalStore;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.ServerRequests.Callbacks.GetUserCallBack;
import ravtrix.backpackerbuddy.VolleyServerConnections.VolleyUserInfo;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart1Activity extends AppCompatActivity implements  View.OnClickListener {

    @BindView(R.id.etEmail) protected EditText etEmail;
    @BindView(R.id.etUsername) protected EditText etUsername;
    @BindView(R.id.etPassword) protected EditText etPassword;
    private VolleyUserInfo volleyUserInfo;
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

        volleyUserInfo = new VolleyUserInfo(this);
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

                User signedUpUser = new User(email, username, password);

                volleyUserInfo.storeUserInfo(signedUpUser, new GetUserCallBack() {
                    @Override
                    public void done(User returnedUser) {
                        // Username has been taken and user info will not be stored
                        if (returnedUser == null) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpPart1Activity.this);
                            dialogBuilder.setMessage("User has been taken");
                            dialogBuilder.setPositiveButton("Ok", null);
                            dialogBuilder.show();
                        } else {
                            LoggedInUser user = new LoggedInUser();
                            user.setUsername(returnedUser.getUsername());
                            user.setEmail(returnedUser.getEmail());
                            user.setUserID(returnedUser.getUserID());

                            userLocalStore.storeUserData(user);
                            UserLocalStore.isUserLoggedIn = true;
                            startActivity(new Intent(SignUpPart1Activity.this, SignUpPart2Activity.class));
                        }
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
