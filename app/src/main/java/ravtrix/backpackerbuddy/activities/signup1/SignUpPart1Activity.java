package ravtrix.backpackerbuddy.activities.signup1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.UserLocation;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.activities.signup2.OnCountryReceived;
import ravtrix.backpackerbuddy.activities.startingpage.WelcomeActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.HelpersPermission;
import ravtrix.backpackerbuddy.interfacescom.UserLocationInterface;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart1Activity extends AppCompatActivity implements ISignUpPart1View, View.OnClickListener {

    @BindView(R.id.etEmail) protected EditText etEmail;
    @BindView(R.id.etUsername) protected EditText etUsername;
    @BindView(R.id.etPassword) protected EditText etPassword;
    @BindView(R.id.etConfirmPass) protected  EditText etConfirmPassword;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_signup1_tvProfilePic) protected TextView tvProfilePic;
    @BindView(R.id.activity_signup1_tvSignUp) protected TextView tvSignUp;
    @BindView(R.id.activity_signup1_imgProfileImage) protected ImageView imgProfileImage;
    private SignUpPart1Presenter signUpPart1Presenter;
    private ProgressDialog progressDialog;
    private UserLocation userLocation;
    private long mLastClickTime = 0;
    private String username, password, email;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int RESULT_LOAD_IMAGE = 1;
    private boolean isPhotoUploaed = false;
    private Bitmap bitmapImage;
    private long currentTime;
    private Double userLongitude = 0.0;
    private Double userLatitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);

        setFonts();
        setTitle("Sign Up");

        imgProfileImage.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);

        currentTime = System.currentTimeMillis();
        signUpPart1Presenter = new SignUpPart1Presenter(this);
        userLocation = new UserLocation(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_signup1_imgProfileImage:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                            getString(R.string.permission_read_storage_rationale),
                            REQUEST_STORAGE_READ_ACCESS_PERMISSION);
                } else {
                    // Get the image from gallery
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                }

                break;
            case R.id.activity_signup1_tvSignUp:

                // Prevents double clicking
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                progressDialog = Helpers.showProgressDialog(this, "Setting up your account...");
                // Regular sign up
                inputValidationAndNextStep();
                break;
            default:
                break;
        }
    }

    // Validate that user input is in correct format
    private void inputValidationAndNextStep() {

        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPass = etConfirmPassword.getText().toString();
        signUpPart1Presenter.inputValidation(isPhotoUploaed, username, password, email, confirmPass);
    }

    // Go to part 2 sign up after location is received
    private void startPart2SignUp() {

        userLocation.startLocationService(new UserLocationInterface() {
            @Override
            public void onReceivedLocation(double latitude, double longitude) {
                Helpers.hideProgressDialog(progressDialog);
                userLongitude = longitude;
                userLatitude = latitude;
                signUpRetrofit();
            }
        });
    }

    // Sign up without accessing latitude and longitude since user refuses
    private void startPart2WithoutLocation() {
        Helpers.hideProgressDialog(progressDialog);
        // longitude and latitude 0.0
        userLongitude = 0.0;
        userLatitude = 0.0;
        signUpRetrofit();
    }

    private void signUpRetrofit() {

        // Turn image into base 64 encoded string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ((BitmapDrawable) imgProfileImage
                .getDrawable())
                .getBitmap()
                .compress(Bitmap
                        .CompressFormat
                        .JPEG, 60, byteArrayOutputStream);
        final String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        final HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("email", email);
        userInfo.put("username", username);
        userInfo.put("password", password);
        userInfo.put("latitude", Double.toString(this.userLatitude));
        userInfo.put("longitude", Double.toString(this.userLongitude));
        userInfo.put("userpic", encodedImage);
        userInfo.put("time", Long.toString(currentTime));
        userInfo.put("token", getFCMToken());

        if (!HelpersPermission.hasLocationPermission(this)) {
            userInfo.put("country", "Somewhere");
            // Make Retrofit call to communicate with the server
            signUpPart1Presenter.retrofitStoreUser(userInfo);
        } else {
            try {
                Helpers.getCountryGeocoder(this, this.userLatitude,
                        this.userLongitude, new OnCountryReceived() {
                    @Override
                    public void onCountryReceived(String country) {

                        if (null == country || country.isEmpty()) {
                            userInfo.put("country", "Somewhere");
                        } else {
                            userInfo.put("country", country);
                        }
                        // Make Retrofit call to communicate with the server
                        signUpPart1Presenter.retrofitStoreUser(userInfo);
                    }
                });
            } catch (IOException e) {
                userInfo.put("country", "Somewhere");
                signUpPart1Presenter.retrofitStoreUser(userInfo);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    @Override
    public void startSignUpPart2Activity(final String email, final String username, final String password) {

        this.email = email;
        this.username = username;
        this.password = password;

        if (HelpersPermission.hasLocationPermission(this)) {
            startPart2SignUp();
        } else {
            // Request for location access
            HelpersPermission.showLocationRequest(this);
        }
    }

    @Override
    public void displayUsernameTakenDialog() {
        Helpers.hideProgressDialog(progressDialog);

        Helpers.showAlertDialog(this, "Username has been taken...");
    }

    @Override
    public void displayEmailTakenDialog() {
        Helpers.hideProgressDialog(progressDialog);

        Helpers.showAlertDialog(this, "Email has been taken...");
    }

    @Override
    public void displayUsernameAndEmailTakenDialog() {
        Helpers.hideProgressDialog(progressDialog);
        Helpers.showAlertDialog(this, "Username and Email have been taken...");
    }

    public void hideProgressDialog() {
        Helpers.hideProgressDialog(progressDialog);
    }

    @Override
    public void displayErrorToast() {
        Helpers.hideProgressDialog(progressDialog);

        Helpers.displayToast(this, "Error. Try again...");
    }

    private void setFonts() {
        Helpers.overrideFonts(this, etEmail);
        Helpers.overrideFonts(this, etUsername);
        Helpers.overrideFonts(this, etPassword);
        Helpers.overrideFonts(this, etConfirmPassword);
        Helpers.overrideFonts(this, tvSignUp);
        Helpers.overrideFonts(this, tvProfilePic);
    }

    @Override
    public void startUserMainPage() {
        Intent intent = new Intent(SignUpPart1Activity.this, UserMainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showAlertDialogError() {
        Helpers.displayErrorToast(this);
    }

    @Override
    public void updateCountry(final String username) {
        // Because this process will take a long time, let me update after its done
        new SignUpPart1Activity.RetrieveCountryTask(Double.toString(this.userLatitude),
                Double.toString(this.userLongitude), new OnCountryReceived() {
            @Override
            public void onCountryReceived(String country) {
                if (country.isEmpty()) {
                    // Make Retrofit call to communicate with the server
                    signUpPart1Presenter.updateCountry(username, "Somewhere");
                } else {
                    // Make Retrofit call to communicate with the server
                    signUpPart1Presenter.updateCountry(username, country);
                }
            }
        }).execute();
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
            case HelpersPermission.LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startPart2SignUp();
                } else {
                    startPart2WithoutLocation();
                }
                break;
        }
    }

    // Set the image from gallery onto ivProfilePic imageview
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            this.isPhotoUploaed = true;
            Uri selectedImage = data.getData();
            bitmapImage = null;

            try {
                bitmapImage = Helpers.decodeBitmap(this, selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            tvProfilePic.setVisibility(View.GONE);
            imgProfileImage.setImageBitmap(bitmapImage);
            //imgRotate.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SignUpPart1Activity.this,
                                    new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    protected void showAlertDialog(String title, String message,
                                   DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   String positiveText,
                                   DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
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
    private class RetrieveCountryTask extends AsyncTask<Void, Void, String> {
        private String latitude, longitude;
        private OnCountryReceived onCountryReceived;

        RetrieveCountryTask(String latitude, String longitude, OnCountryReceived onCountryReceived) {
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
}
