package ravtrix.backpackerbuddy.activities.signup3;

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

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
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
    private static final int RESULT_LOAD_IMAGE = 1;
    private SignUpPart3Presenter signUpPart3Presenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        ButterKnife.bind(this);

        Helpers.setToolbar(this, toolbar);
        circleImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_photo));
        bEditImage.setOnClickListener(this);
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


    /**
     * Sign user up with information provided by the user during the sign up process
     */
    private void signUserUp() {

        showProgressBar();
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

        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("firstname", firstname);
        userInfo.put("lastname", lastname);
        userInfo.put("email", email);
        userInfo.put("username", username);
        userInfo.put("password", password);
        userInfo.put("latitude", Double.toString(latitude));
        userInfo.put("longitude", Double.toString(longitude));
        userInfo.put("userpic", encodedImage);
        long currentTime = System.currentTimeMillis();
        userInfo.put("time", Long.toString(currentTime));
        userInfo.put("token", getFCMToken());

        // Make Retrofit call to communicate with the server
        signUpPart3Presenter.retrofitStoreUser(userInfo);
    }

    /**
     * Get Notification token from fire base
     * @return the token
     */
    String getFCMToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void startUserMainPage() {
        startActivity(new Intent(SignUpPart3Activity.this, UserMainPage.class));
    }

    @Override
    public void showAlertDialogError() {
        Helpers.showAlertDialog(SignUpPart3Activity.this, "Error. Try again...");
    }

    @Override
    public void showProgressBar() {
        progressDialog = Helpers.showProgressDialog(this, "Signing Up...");
    }

    @Override
    public void hideProgressBar() {
        Helpers.hideProgressDialog(progressDialog);
    }
}
