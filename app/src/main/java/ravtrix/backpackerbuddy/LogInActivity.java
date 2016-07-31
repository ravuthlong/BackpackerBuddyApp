package ravtrix.backpackerbuddy;

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

import ravtrix.backpackerbuddy.ServerRequests.Callbacks.GetUserCallBack;
import ravtrix.backpackerbuddy.VolleyServerConnections.VolleyUserInfo;

/**
 * Created by Ravinder on 3/29/16.
 */
public class LogInActivity extends AppCompatActivity {

    private EditText etLoggedInUsername, etLoggedInPassword;
    private VolleyUserInfo volleyUserInfo;
    //private ServerRequests serverRequests;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLogIn);
        setSupportActionBar(toolbar);

        etLoggedInUsername = (EditText) findViewById(R.id.etLoggedInUsername);
        etLoggedInPassword = (EditText) findViewById(R.id.etLoggedInPassword);

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

        //serverRequests = new ServerRequests(this);
        volleyUserInfo = new VolleyUserInfo(this);
        userLocalStore = new UserLocalStore(this);
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

                String username = etLoggedInUsername.getText().toString();
                String password = etLoggedInPassword.getText().toString();
                final User loggedInUser = new User(username, password);



                volleyUserInfo.logUserInDataInBackground(loggedInUser, new GetUserCallBack() {
                    @Override
                    public void done(User returnedUser) {
                        if (returnedUser == null) {
                            showNoUserErrorMessage();
                        } else {
                            userLocalStore.storeUserData(returnedUser);
                            startActivity(new Intent(LogInActivity.this, UserMainPage.class));
                        }
                    }
                });

                /*
                serverRequests.logUserInDataInBackground(loggedInUser, new GetUserCallBack() {
                    @Override
                    public void done(User returnedUser) {
                        if (returnedUser == null) {
                            showNoUserErrorMessage();
                        } else {
                            userLocalStore.storeUserData(returnedUser);
                            startActivity(new Intent(LogInActivity.this, UserMainPage.class));
                        }
                    }
                });*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
