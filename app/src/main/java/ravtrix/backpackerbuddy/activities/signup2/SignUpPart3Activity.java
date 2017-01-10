package ravtrix.backpackerbuddy.activities.signup2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSendBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;

public class SignUpPart3Activity extends OptionMenuSendBaseActivity implements View.OnClickListener, ISignUpPart3View {

    @BindView(R.id.userImage) protected CircleImageView circleImageView;
    @BindView(R.id.bEditImage) protected ImageView bEditImage;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.imgRotate) protected ImageView imgRotate;
    private static final int RESULT_LOAD_IMAGE = 1;
    private SignUpPart3Presenter signUpPart3Presenter;
    private ProgressDialog progressDialog;
    private long currentTime;
    private Bitmap bitmapImage;
    private long mLastClickTime = 0;
    private String username = "";
    private String password = "";
    private String email = "";
    private Double longitude = 0.0;
    private Double latitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        ButterKnife.bind(this);

        Helpers.setToolbar(this, toolbar);
        setTitle("Sign Up Part 2");
        setBundle();

        circleImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_photo));
        bEditImage.setOnClickListener(this);
        imgRotate.setOnClickListener(this);

        currentTime = System.currentTimeMillis();
        signUpPart3Presenter = new SignUpPart3Presenter(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bEditImage:
                // Get the image from gallery
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.imgRotate:
                bitmapImage = Helpers.rotateBitmap(bitmapImage);
                circleImageView.setImageBitmap(bitmapImage);
                break;
            default:
                break;
        }
    }

    // Set the image from gallery onto ivProfilePic imageview
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            bitmapImage = null;

            try {
                bitmapImage = Helpers.decodeBitmap(this, selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            circleImageView.setImageBitmap(bitmapImage);
            imgRotate.setVisibility(View.VISIBLE);
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
                signUserUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setBundle() {
        Bundle signUp1Info = getIntent().getExtras();
        if (signUp1Info != null) {
            this.username = signUp1Info.getString("username");
            password = signUp1Info.getString("password");
            email = signUp1Info.getString("email");
            longitude = signUp1Info.getDouble("longitude");
            latitude = signUp1Info.getDouble("latitude");
        }
    }

    /**
     * Sign user up with information provided by the user during the sign up process
     */
    private void signUserUp() {

        showProgressBar();

        // Turn image into base 64 encoded string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ((BitmapDrawable) circleImageView
                .getDrawable())
                .getBitmap()
                .compress(Bitmap
                        .CompressFormat
                        .JPEG, 15, byteArrayOutputStream);
        final String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        final HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("email", email);
        userInfo.put("username", username);
        userInfo.put("password", password);
        userInfo.put("latitude", Double.toString(latitude));
        userInfo.put("longitude", Double.toString(longitude));
        userInfo.put("userpic", encodedImage);
        userInfo.put("time", Long.toString(currentTime));
        userInfo.put("token", getFCMToken());

        try {
            Helpers.getCountryGeocoder(this, latitude, longitude, new OnCountryReceived() {
                @Override
                public void onCountryReceived(String country) {
                    if (country.isEmpty()) {
                        userInfo.put("Unknown", country);
                    } else {
                        userInfo.put("country", country);
                    }
                    // Make Retrofit call to communicate with the server
                    signUpPart3Presenter.retrofitStoreUser(userInfo);
                }
            });
        } catch (IOException e) {

            new RetrieveCityCountryTask(Double.toString(latitude), Double.toString(longitude), new OnCountryReceived() {
                @Override
                public void onCountryReceived(String country) {
                    if (country.isEmpty()) {
                        userInfo.put("Unknown", country);
                    } else {
                        userInfo.put("country", country);
                    }
                    // Make Retrofit call to communicate with the server
                    signUpPart3Presenter.retrofitStoreUser(userInfo);
                }
            }).execute();
        }
    }

    /**
     * Get Notification token from fire base
     * @return the token
     */
    private String getFCMToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void startUserMainPage() {
        Intent intent = new Intent(SignUpPart3Activity.this, UserMainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showAlertDialogError() {
        Helpers.displayErrorToast(this);
    }

    @Override
    public void showProgressBar() {
        progressDialog = Helpers.showProgressDialog(this, "Signing Up...");
    }

    @Override
    public void hideProgressBar() {
        Helpers.hideProgressDialog(progressDialog);
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
}
