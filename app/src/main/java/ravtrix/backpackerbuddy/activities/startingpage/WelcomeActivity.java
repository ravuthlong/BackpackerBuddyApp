package ravtrix.backpackerbuddy.activities.startingpage;

import android.app.ProgressDialog;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.login.LogInActivity;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.activities.signup1.SignUpPart1Activity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tvBackpacker) protected TextView tvBackpacker;
    @BindView(R.id.tvBuddy) protected TextView tvBuddy;
    @BindView(R.id.imgbSignUp) protected ImageButton imgbSignUp;
    @BindView(R.id.imgbLogIn) protected ImageView imgbLogIn;
    @BindView(R.id.bFacebookLogin) protected LoginButton loginButton;
    private UserLocalStore userLocalStore;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;

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


        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = Helpers.showProgressDialog(WelcomeActivity.this, "Logging In...");
            }
        });
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                graphRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.getCause().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void graphRequest(final AccessToken token) {

        final String notificationToken = FirebaseInstanceId.getInstance().getToken();

        //Make a request to fetch facebook user data based on the given token
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    final String email = object.get("email").toString();
                    final String imageURL = object.getJSONObject("picture").getJSONObject("data").get("url").toString();
                    final String gender = object.get("gender").toString();

                    Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                            .isUsernameOrEmailTaken()
                            .isUsernameOrEmailTaken("", email);

                    retrofit.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                            if (progressDialog != null) {
                                Helpers.hideProgressDialog(progressDialog);
                            }

                            if (response.body().get("emailtaken").getAsInt() == 1) {
                                // Means the user exists, so log them in
                                logInFacebookRetrofit(email, notificationToken);
                            } else {
                                String userGender;

                                switch (gender) {
                                    case "male":
                                        userGender = "M";
                                        break;
                                    case "female":
                                        userGender = "F";
                                        break;
                                    default:
                                        userGender = "N/A";
                                        break;
                                }
                                Intent signUpIntent = new Intent(WelcomeActivity.this, SignUpPart1Activity.class);
                                signUpIntent.putExtra("email", email);
                                signUpIntent.putExtra("imageURL", imageURL);
                                signUpIntent.putExtra("gender", userGender);
                                startActivity(signUpIntent);
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            if (progressDialog != null) {
                                Helpers.hideProgressDialog(progressDialog);
                            }
                        }
                    });

                } catch (JSONException e) {
                    Helpers.displayToast(WelcomeActivity.this, "Error");
                }
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("fields", "gender, email, picture.type(large)");
        request.setParameters(bundle);
        request.executeAsync(); // which will invoke onCompleted
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    private void logInFacebookRetrofit(String email, String token) {

        Call<LoggedInUser> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .logUserInFacebook()
                .logUserInFacebook(email, token);

        retrofit.enqueue(new Callback<LoggedInUser>() {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                LoggedInUser loggedInUser = response.body();

                userLocalStore.clearUserData();
                userLocalStore.storeUserData(loggedInUser);

                Intent intent = new Intent(WelcomeActivity.this, UserMainPage.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                Helpers.displayToast(WelcomeActivity.this, "Error logging in...");
            }
        });
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
            Intent intent = new Intent(WelcomeActivity.this, UserMainPage.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
