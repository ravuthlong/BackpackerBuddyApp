package ravtrix.backpackerbuddy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.Models.UserLocalStore;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.VolleyServerConnections.VolleyUserInfo;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart2Activity extends AppCompatActivity {

    @BindView(R.id.etFirstname) protected EditText etFirstname;
    @BindView(R.id.etLastname) protected EditText etLastname;
    private VolleyUserInfo volleyUserInfo;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_signup2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitSignup2:

                String firstName = etFirstname.getText().toString();
                String lastName = etLastname.getText().toString();

                volleyUserInfo.storeExtraUserInfo(userLocalStore.getLoggedInUser().getUserID(), firstName, lastName);

                startActivity(new Intent(this, UserMainPage.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
