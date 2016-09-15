package ravtrix.backpackerbuddy.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.login.LogInActivity;
import ravtrix.backpackerbuddy.activities.signup1.SignUpPart1Activity;
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tvBackpacker) protected TextView tvBackpacker;
    @BindView(R.id.tvBuddy) protected TextView tvBuddy;
    @BindView(R.id.imgbSignUp) protected ImageButton imgbSignUp;
    @BindView(R.id.imgbLogIn) protected ImageView imgbLogIn;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        //LeakCanary.install(getApplication());
        ButterKnife.bind(this);

        // Check to see if the user is already logged in. If so, go straight to main user page
        userLocalStore = new UserLocalStore(this);
        if (!userLocalStore.getLoggedInUser().getUsername().equals("")) {
            startActivity(new Intent(this, UserMainPage.class));
        }

        Typeface monuFont = Typeface.createFromAsset(getAssets(), "Monu.otf");

        tvBackpacker.setTypeface(monuFont);
        tvBackpacker.setTextSize(85);
        tvBuddy.setTypeface(monuFont);
        tvBuddy.setTextSize(80);
        imgbSignUp.setOnClickListener(this);
        imgbLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbSignUp:
                startActivity(new Intent(this, SignUpPart1Activity.class));
                break;
            case R.id.imgbLogIn:
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
