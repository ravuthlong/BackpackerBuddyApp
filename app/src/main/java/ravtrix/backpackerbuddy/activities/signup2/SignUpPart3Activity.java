package ravtrix.backpackerbuddy.activities.signup2;

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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import ravtrix.backpackerbuddy.helpers.HelpersPermission;

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
    private boolean isPhotoUploaed = false;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private AlertDialog mAlertDialog;

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
            this.isPhotoUploaed = true;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Get the image from gallery
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Prevents double clicking
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return false;
        } else {
            mLastClickTime = SystemClock.elapsedRealtime();
            switch (item.getItemId()) {
                case R.id.submitSend:

                    if (isPhotoUploaed) {
                        signUserUp();
                    } else {
                        Helpers.displayToast(this, "You must upload a photo...");
                    }
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
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

        if (!HelpersPermission.hasLocationPermission(this)) {
            userInfo.put("country", "Somewhere...");
            // Make Retrofit call to communicate with the server
            signUpPart3Presenter.retrofitStoreUser(userInfo);
        } else {
            try {
                Helpers.getCountryGeocoder(this, latitude, longitude, new OnCountryReceived() {
                    @Override
                    public void onCountryReceived(String country) {
                        if (country.isEmpty()) {
                            userInfo.put("country", "Somewhere...");
                        } else {
                            userInfo.put("country", country);
                        }
                        // Make Retrofit call to communicate with the server
                        signUpPart3Presenter.retrofitStoreUser(userInfo);
                    }
                });
            } catch (IOException e) {
                userInfo.put("country", "");
                signUpPart3Presenter.retrofitStoreUser(userInfo);
            }
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
                            ActivityCompat.requestPermissions(SignUpPart3Activity.this,
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
        mAlertDialog = builder.show();
    }

    @Override
    public void updateCountry(final String username) {
        // Because this process will take a long time, let me update after its done
        new RetrieveCountryTask(Double.toString(latitude), Double.toString(longitude), new OnCountryReceived() {
            @Override
            public void onCountryReceived(String country) {
                if (country.isEmpty()) {
                    // Make Retrofit call to communicate with the server
                    signUpPart3Presenter.updateCountry(username, "Somewhere...");
                } else {
                    // Make Retrofit call to communicate with the server
                    signUpPart3Presenter.updateCountry(username, country);
                }
            }
        }).execute();
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
        progressDialog = Helpers.showProgressDialog(this, "Preparing your account. Please wait...");
    }

    @Override
    public void hideProgressBar() {
        Helpers.hideProgressDialog(progressDialog);
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
