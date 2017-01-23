package ravtrix.backpackerbuddy.activities.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.facebook.login.LoginManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.aboutpage.AboutActivity;
import ravtrix.backpackerbuddy.activities.notificationsettings.NotificationActivity;
import ravtrix.backpackerbuddy.activities.startingpage.WelcomeActivity;
import ravtrix.backpackerbuddy.activities.userinfoedit.changeemail.ChangeEmail;
import ravtrix.backpackerbuddy.activities.userinfoedit.changepassword.ChangePassword;
import ravtrix.backpackerbuddy.activities.userinfoedit.changeusername.ChangeUsername;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.bSignOut) protected Button bSignOut;
    @BindView(R.id.bPassword) protected Button bChangePassword;
    @BindView(R.id.bEmail) protected Button bChangeEmail;
    @BindView(R.id.bUsername) protected Button bChangeUsername;
    @BindView(R.id.bFeedback) protected Button bFeedback;
    @BindView(R.id.bNotification) protected Button bNotificaton;
    @BindView(R.id.bAbout) protected Button bAbout;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.relative_setting_buttons) protected RelativeLayout relativeLayout;

    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, relativeLayout);
        this.setTitle("Settings");

        bSignOut.setOnClickListener(this);
        bChangePassword.setOnClickListener(this);
        bChangeEmail.setOnClickListener(this);
        bChangeUsername.setOnClickListener(this);
        bFeedback.setOnClickListener(this);
        bAbout.setOnClickListener(this);
        bNotificaton.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);

        if (userLocalStore.getLoggedInUser().getIsFacebook() == 1) {
            bChangeEmail.setVisibility(View.GONE);
            bChangePassword.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bSignOut:
                // Log user out of facebook if they are a facebook user
                if (userLocalStore.getLoggedInUser().getIsFacebook() == 1){
                    LoginManager.getInstance().logOut();
                }
                // Clear local storage because user has logged out
                userLocalStore.clearUserData();
                startActivity(new Intent(this, WelcomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
            case R.id.bPassword:
                startActivity(new Intent(this, ChangePassword.class));
                break;
            case R.id.bUsername:
                startActivity(new Intent(this, ChangeUsername.class));
                break;
            case R.id.bEmail:
                startActivity(new Intent(this, ChangeEmail.class));
                break;
            case R.id.bNotification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case R.id.bFeedback:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","ravtrixdev@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.bAbout:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            default:
                break;
        }
    }
}
