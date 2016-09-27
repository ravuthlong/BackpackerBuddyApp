package ravtrix.backpackerbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.squareup.leakcanary.LeakCanary;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.bSignOut) protected Button bSignOut;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        LeakCanary.install(getApplication());

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);

        bSignOut.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bSignOut:
                // Clear local storage because user has logged out
                userLocalStore.clearUserData();
                startActivity(new Intent(this, WelcomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }

}
