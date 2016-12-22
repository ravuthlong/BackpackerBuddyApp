package ravtrix.backpackerbuddy.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.login.LogInActivity;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.activities.signup1.SignUpPart1Activity;
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tvBackpacker) protected TextView tvBackpacker;
    @BindView(R.id.tvBuddy) protected TextView tvBuddy;
    @BindView(R.id.imgbSignUp) protected ImageButton imgbSignUp;
    @BindView(R.id.imgbLogIn) protected ImageView imgbLogIn;
    @BindView(R.id.bFacebookLogin) protected LoginButton loginButton;
    private UserLocalStore userLocalStore;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        setContentView(R.layout.activity_mainpage);
        ButterKnife.bind(this);

        checkIsUserLoggedIn();
        setFontStyle();
        imgbSignUp.setOnClickListener(this);
        imgbLogIn.setOnClickListener(this);

        /*
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
                graphRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "CANCEL", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.getCause().toString(), Toast.LENGTH_LONG).show();


            }
        });*/
    }

    public void graphRequest(AccessToken token) {

        //Make a request to fetch facebook user data based on the given token
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Toast.makeText(getApplicationContext(), object.getJSONObject("picture").getJSONObject("data").get("url").toString(), Toast.LENGTH_LONG).show();

                    System.out.println("ID: " + object.get("id").toString());
                    System.out.println("GENDER: " + object.get("gender").toString());
                    System.out.println("EMAIL: " + object.get("email").toString());
                    System.out.println("PHOTO: " + object.getJSONObject("picture").getJSONObject("data").get("url").toString());

                } catch (JSONException e) {

                }
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("fields", "id, gender, email, picture.type(large)");
        request.setParameters(bundle);
        request.executeAsync(); // which will invoke onCompleted
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);
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

    private void setFontStyle() {
        Typeface monuFont = Typeface.createFromAsset(getAssets(), "Monu.otf");

        tvBackpacker.setTypeface(monuFont);
        tvBackpacker.setTextSize(85);
        tvBuddy.setTypeface(monuFont);
        tvBuddy.setTextSize(80);
    }

    /**
     * Check to see if the user is already logged in. If so, go straight to main user page
     */
    private void checkIsUserLoggedIn() {
        userLocalStore = new UserLocalStore(this);
        if (!userLocalStore.getLoggedInUser().getUsername().equals("")) {
            startActivity(new Intent(this, UserMainPage.class));
        }
    }
}
