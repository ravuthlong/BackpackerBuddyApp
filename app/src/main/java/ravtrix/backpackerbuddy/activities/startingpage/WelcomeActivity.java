package ravtrix.backpackerbuddy.activities.startingpage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import ravtrix.backpackerbuddy.activities.facebooksignup.FacebookSignUpActivity;
import ravtrix.backpackerbuddy.activities.login.LogInActivity;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.activities.signup1.SignUpPart1Activity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.HelpersPermission;
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
    @BindView(R.id.activity_main_tvGuest) protected TextView tvGuestLogin;
    private UserLocalStore userLocalStore;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;
    private static final int LOCATION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_mainpage);
        ButterKnife.bind(this);

        setFontStyle();
        setUnderline();

        imgbSignUp.setOnClickListener(this);
        imgbLogIn.setOnClickListener(this);
        tvGuestLogin.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);

        if (!HelpersPermission.hasLocationPermission(this)) {
            HelpersPermission.showLocationRequest(this);
        }

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = Helpers.showProgressDialog(WelcomeActivity.this, "Logging In. Please wait...");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbSignUp:
                startActivity(new Intent(this, SignUpPart1Activity.class));
                break;
            case R.id.imgbLogIn:
                startActivity(new Intent(this, LogInActivity.class));
                break;
            case R.id.activity_main_tvGuest:
                Intent intent = new Intent(this, UserMainPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("isGuest", true);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Helpers.showAlertDialog(this, "Give this app permission in the future in order to take full advantage of the functionality.");
            }
        }
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
                    final String firstName = object.get("last_name").toString();
                    final String lastName = object.get("first_name").toString();

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

                                Intent signUpIntent = new Intent(WelcomeActivity.this, FacebookSignUpActivity.class);
                                signUpIntent.putExtra("email", email);
                                signUpIntent.putExtra("imageURL", imageURL);
                                signUpIntent.putExtra("firstName", firstName);
                                signUpIntent.putExtra("lastName", lastName);
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
        bundle.putString("fields", "first_name,last_name, email, picture.type(large)");
        request.setParameters(bundle);
        request.executeAsync(); // which will invoke onCompleted
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

    private void setUnderline() {
        this.tvGuestLogin.setPaintFlags(tvGuestLogin.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setFontStyle() {
        Typeface monuFont = Typeface.createFromAsset(getAssets(), "Monu.otf");
        tvBackpacker.setTypeface(monuFont);
        tvBuddy.setTypeface(monuFont);
        Helpers.overrideFonts(this, this.tvGuestLogin);
    }
}
