package ravtrix.backpackerbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import ravtrix.backpackerbuddy.ServerRequests.ServerRequests;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart2Activity extends AppCompatActivity {

    private EditText etFirstname, etLastname;
    private ServerRequests serverRequests;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSignUp2);
        setSupportActionBar(toolbar);

        etFirstname = (EditText) findViewById(R.id.etFirstname);
        etLastname = (EditText) findViewById(R.id.etLastname);

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

        serverRequests = new ServerRequests(this);
        userLocalStore = new UserLocalStore(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.signup2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitSignup2:

                String firstName = etFirstname.getText().toString();
                String lastName = etLastname.getText().toString();
                serverRequests.storeExtraUserInfoDataInBackground(userLocalStore.getLoggedInUser().getUserID(),
                        firstName, lastName);
                startActivity(new Intent(this, TravelSelection.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
