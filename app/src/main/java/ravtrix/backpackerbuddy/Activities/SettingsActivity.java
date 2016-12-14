package ravtrix.backpackerbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.bSignOut) protected Button bSignOut;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.bPassword) protected Button bChangePassword;
    @BindView(R.id.bEmail) protected Button bChangeEmail;

    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        this.setTitle("Settings");

        bSignOut.setOnClickListener(this);
        bChangePassword.setOnClickListener(this);
        bChangeEmail.setOnClickListener(this);
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
                break;
            case R.id.bPassword:
                startActivity(new Intent(this, ChangePassword.class));
                break;
            case R.id.bEmail:
                startActivity(new Intent(this, ChangeEmail.class));
                break;
            default:
                break;
        }
    }

}
