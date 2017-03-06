package ravtrix.backpackerbuddy.activities.startingpage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.UserLocation;
import ravtrix.backpackerbuddy.activities.PrivacyPolicyActivity;
import ravtrix.backpackerbuddy.activities.login.LogInActivity;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.activities.signup1.SignUpPart1Activity;
import ravtrix.backpackerbuddy.activities.signup2.OnCountryReceived;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.HelpersPermission;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.interfacescom.UserLocationInterface;
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
    @BindView(R.id.bFacebookLogin) protected LoginButton loginButton;
    @BindView(R.id.activity_mainpage_tvLogin) protected TextView tvLogin;
    @BindView(R.id.activity_mainpage_tvRegister) protected TextView tvRegister;
    @BindView(R.id.activity_mainpage_privacy) protected TextView tvPrivacyPolicy;

    private UserLocalStore userLocalStore;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;
    private static final int LOCATION_REQUEST_CODE = 10;
    private UserLocation userLocation;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_mainpage);
        callbackManager = CallbackManager.Factory.create();
        ButterKnife.bind(this);

        setFontStyle();
        currentTime = System.currentTimeMillis();

        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvPrivacyPolicy.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
        userLocation = new UserLocation(this);

        if (!HelpersPermission.hasLocationPermission(this)) {
            HelpersPermission.showLocationRequest(this);
        }

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
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
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_mainpage_tvRegister:
                tvRegister.setBackgroundColor(ContextCompat.getColor(WelcomeActivity.this, R.color.whiteWithDarkerOpacity));
                startActivity(new Intent(this, SignUpPart1Activity.class));
                break;
            case R.id.activity_mainpage_tvLogin:
                tvLogin.setBackgroundColor(ContextCompat.getColor(WelcomeActivity.this, R.color.whiteWithDarkerOpacity));
                startActivity(new Intent(this, LogInActivity.class));
                break;
            case R.id.activity_mainpage_privacy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;
            default:
                break;
        }
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                Helpers.showAlertDialog(this, "Give this app permission in the future in order to take full advantage of the functionality.");
                // sign user up without location
                //System.out.println("USER DID NOT LET ACCESS LOCATION");
            } else {
                //System.out.println("USER LET ACCESS LOCATION");
                // sign user up
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

                                userLocation.setIsFacebookCalling(true); // this will invoke location request pop up dialog if permission not given
                                userLocation.startLocationService(new UserLocationInterface() {
                                    @Override
                                    public void onReceivedLocation(final double latitude, final double longitude) {

                                        final HashMap<String, String> userInfo = new HashMap<>();
                                        userInfo.put("email", email);
                                        userInfo.put("password", "");
                                        userInfo.put("username", firstName + " " + lastName);
                                        userInfo.put("isFacebook", "1");
                                        userInfo.put("latitude", Double.toString(latitude));
                                        userInfo.put("longitude", Double.toString(longitude));
                                        userInfo.put("userpicURL", imageURL);
                                        userInfo.put("time", Long.toString(currentTime));
                                        userInfo.put("token", getFCMToken());

                                        if (latitude == 0) {
                                            // location turned off
                                            System.out.println("LOCATION OFF");
                                            userInfo.put("country", "");
                                            signUpFacebook(userInfo);
                                        } else {
                                            try {
                                                Helpers.getCountryGeocoder(WelcomeActivity.this, latitude, longitude, new OnCountryReceived() {
                                                    @Override
                                                    public void onCountryReceived(String country) {
                                                        userInfo.put("country", country);
                                                        signUpFacebook(userInfo);
                                                    }
                                                });
                                            } catch (IOException e) {
                                                // will need to update country after sign up
                                                userInfo.put("country", "");
                                                signUpFacebook(userInfo);
                                            }
                                        }
                                    }
                                });
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

    private void signUpFacebook(final HashMap<String, String> userInfo) {

        Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .signUserUpFacebook()
                .signUserUpFacebook(userInfo);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();

                int userStatus = jsonObject.get("status").getAsInt();
                // Sign up success
                if (userStatus == 1) {

                    if (!userInfo.get("latitude").equals("0")) {
                        updateCountry(userInfo.get("username"), Double.valueOf(userInfo.get("latitude")),
                                Double.valueOf(userInfo.get("longitude")));
                    }

                    LoggedInUser user = new LoggedInUser();
                    user.setUsername(userInfo.get("username"));
                    user.setEmail(userInfo.get("email"));
                    user.setUserID(jsonObject.get("id").getAsInt());
                    user.setUserImageURL(jsonObject.get("userpic").getAsString());
                    user.setLatitude(Double.valueOf(userInfo.get("latitude")));
                    user.setLongitude(Double.valueOf(userInfo.get("longitude")));
                    user.setTime(currentTime);
                    user.setIsFacebook(1); // facebook user true
                    userLocalStore.storeUserData(user);
                    Helpers.hideProgressDialog(progressDialog);

                    Intent intent = new Intent(WelcomeActivity.this, UserMainPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    displayToastCenter("Error signing up");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                displayToastCenter("Error signing up");
                Helpers.hideProgressDialog(progressDialog);
            }
        });
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

    /**
     * Get Notification token from fire base
     * @return the token
     */
    private String getFCMToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }


    private void setFontStyle() {
        Typeface monuFont = Typeface.createFromAsset(getAssets(), "Monu.otf");
        tvBackpacker.setTypeface(monuFont);
        tvBuddy.setTypeface(monuFont);
        Helpers.overrideFonts(this, this.tvLogin);
        Helpers.overrideFonts(this, this.tvRegister);
        Helpers.overrideFonts(this, this.tvPrivacyPolicy);
    }

    private void displayToastCenter(String text) {
        Toast toast= Toast.makeText(this,
                text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }


    /**
     * Update user country through asynctask
     * @param username              the username of the signed up user
     * @param latitude              their latitude
     * @param longitude             their longitude
     */
    private void updateCountry(final String username, double latitude, double longitude) {
        new WelcomeActivity.RetrieveCityCountryTask(Double.toString(latitude), Double.toString(longitude), new OnCountryReceived() {
            @Override
            public void onCountryReceived(String country) {
                Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                        .updateUserCountry()
                        .updateUserCountry(username, country);
                retrofit.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {}
                });
            }
        }).execute();
    }

    /**
     * Retrieve city and country location information from google location API
     */
    private class RetrieveCityCountryTask extends AsyncTask<Void, Void, String> {
        private String latitude, longitude;
        private OnCountryReceived onCountryReceived;

        RetrieveCityCountryTask(String latitude, String longitude, OnCountryReceived onCountryReceived) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.onCountryReceived = onCountryReceived;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return (Helpers.getCountry(latitude, longitude));
        }

        @Override
        protected void onPostExecute(String s) {
            onCountryReceived.onCountryReceived(s);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (tvRegister != null) {
            tvRegister.setBackgroundColor(ContextCompat.getColor(WelcomeActivity.this, R.color.whiteWithOpacity));
        }
        if (tvLogin != null) {
            tvLogin.setBackgroundColor(ContextCompat.getColor(WelcomeActivity.this, R.color.whiteWithOpacity));
        }

        // Log user out of facebook
        if (AccessToken.getCurrentAccessToken() != null){
            LoginManager.getInstance().logOut();
        }
    }

}
