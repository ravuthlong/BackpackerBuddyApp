package ravtrix.backpackerbuddy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSendBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPart3Activity extends OptionMenuSendBaseActivity implements View.OnClickListener {

    @BindView(R.id.userImage) protected CircleImageView circleImageView;
    @BindView(R.id.bEditImage) protected ImageView bEditImage;
    private static final int RESULT_LOAD_IMAGE = 1;
    private UserLocalStore userLocalStore;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
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
        circleImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_photo));
        bEditImage.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bEditImage:
                // Get the image from gallery
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            default:
        }
    }

    // Set the image from gallery onto ivProfilePic imageview
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            circleImageView.setImageURI(selectedImage);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitSend:
                signUserUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signUserUp() {
        final ProgressDialog progressDialog = Helpers.showProgressDialog(this, "Signing Up...");

        String username = "";
        String password = "";
        String email = "";
        String firstname = "";
        String lastname = "";
        Double longitude = 0.0;
        Double latitude = 0.0;

        // Turn image into base 64 encoded string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ((BitmapDrawable) circleImageView
                .getDrawable())
                .getBitmap()
                .compress(Bitmap
                        .CompressFormat
                        .JPEG, 15, byteArrayOutputStream);
        final String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        Bundle signUp1Info = getIntent().getExtras();
        if (signUp1Info != null) {
            username = signUp1Info.getString("username");
            password = signUp1Info.getString("password");
            email = signUp1Info.getString("email");
            longitude = signUp1Info.getDouble("longitude");
            latitude = signUp1Info.getDouble("latitude");
            firstname = signUp1Info.getString("firstname");
            lastname = signUp1Info.getString("lastname");
        }

        final SignedUpUser signedUpUser = new SignedUpUser();
        signedUpUser.setUsername(username);
        signedUpUser.setEmail(email);
        signedUpUser.setLatitude(latitude);
        signedUpUser.setLongitude(longitude);

        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("firstname", firstname);
        userInfo.put("lastname", lastname);
        userInfo.put("email", email);
        userInfo.put("username", username);
        userInfo.put("password", password);
        userInfo.put("latitude", Double.toString(latitude));
        userInfo.put("longitude", Double.toString(longitude));
        userInfo.put("userpic", encodedImage);
        currentTime = System.currentTimeMillis();
        userInfo.put("time", Long.toString(currentTime));


        // Make Retrofit call to communicate with the server
        Call<JsonObject> returnedStatus = RetrofitUserInfoSingleton.getRetrofitUserInfo().signUserUpPart1().signedUpStatus(userInfo);
        returnedStatus.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject status = response.body();

                int userStatus = status.get("status").getAsInt();

                // Sign up success
                if (userStatus == 1) {
                    LoggedInUser user = new LoggedInUser();
                    user.setUsername(signedUpUser.getUsername());
                    user.setEmail(signedUpUser.getEmail());
                    user.setUserID(status.get("id").getAsInt());
                    user.setUserImageURL(status.get("userpic").getAsString());
                    user.setLatitude(signedUpUser.getLatitude());
                    user.setLongitude(signedUpUser.getLongitude());
                    user.setTime(currentTime);
                    userLocalStore.storeUserData(user);
                    startActivity(new Intent(SignUpPart3Activity.this, UserMainPage.class));
                } else {
                    // User has been taken
                    Helpers.showAlertDialog(SignUpPart3Activity.this, "Error. Try again...");
                }

                Helpers.hideProgressDialog(progressDialog);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(SignUpPart3Activity.this, "Error signing up. Try again.");
                Helpers.hideProgressDialog(progressDialog);

            }
        });
    }


    private class SignedUpUser {
        private String username;
        private String email;
        private Double longitude;
        private Double latitude;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }
    }


}
