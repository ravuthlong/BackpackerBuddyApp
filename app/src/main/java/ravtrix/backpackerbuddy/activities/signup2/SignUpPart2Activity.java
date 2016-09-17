package ravtrix.backpackerbuddy.activities.signup2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.squareup.leakcanary.LeakCanary;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.SignUpPart3Activity;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSendBaseActivity;
import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart2Activity extends OptionMenuSendBaseActivity {

    @BindView(R.id.etFirstname) protected EditText etFirstname;
    @BindView(R.id.etLastname) protected EditText etLastname;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitSend:

                String username = "";
                String password = "";
                String email = "";
                Double longitude = 0.0;
                Double latitude = 0.0;

                Bundle signUp1Info = getIntent().getExtras();
                if (signUp1Info != null) {
                    username = signUp1Info.getString("username");
                    password = signUp1Info.getString("password");
                    email = signUp1Info.getString("email");
                    longitude = signUp1Info.getDouble("longitude");
                    latitude = signUp1Info.getDouble("latitude");
                }

                Intent intent = new Intent(SignUpPart2Activity.this, SignUpPart3Activity.class);
                intent.putExtra("email", email);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("firstname", etFirstname.getText().toString());
                intent.putExtra("lastname", etLastname.getText().toString());
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
