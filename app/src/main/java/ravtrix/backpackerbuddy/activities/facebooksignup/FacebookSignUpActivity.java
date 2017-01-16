package ravtrix.backpackerbuddy.activities.facebooksignup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.UserLocation;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.activities.signup2.OnCountryReceived;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSendBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.interfacescom.UserLocationInterface;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacebookSignUpActivity extends OptionMenuSendBaseActivity implements View.OnClickListener {

    @BindView(R.id.etEmail_facebook) protected EditText etEmailFB;
    @BindView(R.id.etUsername_facebook) protected EditText etUsernameFB;
    @BindView(R.id.userImage_facebook) protected CircleImageView imageFB;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.imgRotate_facebook) protected ImageView imgRotateFB;
    @BindView(R.id.bEditImage_facebook) protected ImageView bEditImageFB;
    private Bitmap bitmapImage;
    private long mLastClickTime = 0;
    private String email;
    private long currentTime;
    private UserLocation userLocation;
    private UserLocalStore userLocalStore;
    private ProgressDialog progressDialog;
    private boolean alreadySignedUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_sign_up);
        ButterKnife.bind(this);
        setTitle("Sign Up");
        Helpers.setToolbar(this, toolbar);

        bEditImageFB.setOnClickListener(this);
        currentTime = System.currentTimeMillis();
        userLocalStore = new UserLocalStore(this);
        userLocation = new UserLocation(this, userLocalStore.getLoggedInUser().getLatitude(),
                userLocalStore.getLoggedInUser().getLongitude());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.email = bundle.getString("email");
            etEmailFB.setText(email);
            etEmailFB.setEnabled(false);

            String username = bundle.getString("firstName") + bundle.getString("lastName");
            etUsernameFB.setText(username);

            Picasso.with(this)
                    .load(bundle.getString("imageURL"))
                    .fit()
                    .centerCrop()
                    .into(imageFB);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Prevents double clicking
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (item.getItemId()) {
            case R.id.submitSend:

                this.progressDialog = Helpers.showProgressDialog(this, "Signing up...");

                if (etUsernameFB.getText().toString().trim().length() <= 0) {
                    Helpers.hideProgressDialog(progressDialog);
                    Helpers.displayToast(FacebookSignUpActivity.this, "Empty username");
                } else {
                    Call<JsonObject> retrofitCall = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                            .isUsernameOrEmailTaken()
                            .isUsernameOrEmailTaken(etUsernameFB.getText().toString().trim(), email);

                    retrofitCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.body().get("usernametaken").getAsInt() == 1) {
                                displayToastCenter("Username Taken");
                                Helpers.hideProgressDialog(progressDialog);
                            } else {
                                // Username not taken. Continue to get longitude, latitude

                                // Turn image into base 64 encoded string
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                ((BitmapDrawable) imageFB
                                        .getDrawable())
                                        .getBitmap()
                                        .compress(Bitmap
                                                .CompressFormat
                                                .JPEG, 95, byteArrayOutputStream);
                                final String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                                userLocation.startLocationService(new UserLocationInterface() {
                                    @Override
                                    public void onReceivedLocation(double latitude, double longitude) {
                                        final HashMap<String, String> userInfo = new HashMap<>();
                                        userInfo.put("email", email);
                                        userInfo.put("isFacebook", "1");
                                        userInfo.put("username", etUsernameFB.getText().toString().trim());
                                        userInfo.put("latitude", Double.toString(latitude));
                                        userInfo.put("longitude", Double.toString(longitude));
                                        userInfo.put("userpic", encodedImage);
                                        userInfo.put("time", Long.toString(currentTime));
                                        userInfo.put("token", getFCMToken());

                                        try {
                                            Helpers.getCountryGeocoder(FacebookSignUpActivity.this, latitude, longitude, new OnCountryReceived() {
                                                @Override
                                                public void onCountryReceived(String country) {
                                                   signUp(country, userInfo);
                                                }
                                            });
                                        } catch (IOException e) {

                                            new FacebookSignUpActivity.RetrieveCityCountryTask(Double.toString(latitude), Double.toString(longitude), new OnCountryReceived() {
                                                @Override
                                                public void onCountryReceived(String country) {
                                                    signUp(country, userInfo);

                                                }
                                            }).execute();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Helpers.hideProgressDialog(progressDialog);
                            Helpers.displayErrorToast(FacebookSignUpActivity.this);
                        }
                    });
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bEditImage_facebook:


                break;
            case R.id.imgRotate_facebook:
                bitmapImage = Helpers.rotateBitmap(bitmapImage);
                imageFB.setImageBitmap(bitmapImage);
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Log user out of facebook
        if (AccessToken.getCurrentAccessToken() != null){
            LoginManager.getInstance().logOut();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Log user out of facebook
        if (AccessToken.getCurrentAccessToken() != null){
            LoginManager.getInstance().logOut();
        }
    }

    private void signUp(String country, HashMap<String, String> userInfo) {
        if (country.isEmpty()) {
            userInfo.put("Unknown", country);
        } else {
            userInfo.put("country", country);
        }
        // Make Retrofit call to communicate with the server
        //sign up
        if (!alreadySignedUp) {
            signUserUp(userInfo);
            alreadySignedUp = true;
        }
    }

    private void displayToastCenter(String text) {
        Toast toast= Toast.makeText(this,
                text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    private void signUserUp(final HashMap<String, String> userInfo) {
        // Make Retrofit call to communicate with the server
        Call<JsonObject> returnedStatus = RetrofitUserInfoSingleton.getRetrofitUserInfo().signUserUpPart1().signedUpStatus(userInfo);
        returnedStatus.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();

                int userStatus = jsonObject.get("status").getAsInt();
                // Sign up success
                if (userStatus == 1) {
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

                    Intent intent = new Intent(FacebookSignUpActivity.this, UserMainPage.class);
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

    /**
     * Get Notification token from fire base
     * @return the token
     */
    private String getFCMToken() {
        return FirebaseInstanceId.getInstance().getToken();
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
            return (Helpers.getLocationInfo(latitude, longitude));
        }

        @Override
        protected void onPostExecute(String s) {
            onCountryReceived.onCountryReceived(s);
        }
    }

    // Case for users with grant access needed. Location access granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    userLocation.configureButton();
                }
                break;
        }
    }
}
