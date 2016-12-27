package ravtrix.backpackerbuddy.activities.editphoto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPhotoActivity extends OptionMenuSaveBaseActivity implements View.OnClickListener,
        IEditPhotoView {

    @BindView(R.id.userImage) protected CircleImageView circleImageView;
    @BindView(R.id.bEditImage) protected ImageView bEditImage;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.imgRotate) protected ImageView imgRotate;
    private static final int RESULT_LOAD_IMAGE = 1;
    private boolean isNewPhotoSet = false;
    private boolean isNewPhotoSetAlias = false;
    private UserLocalStore userLocalStore;
    private ProgressDialog progressDialog;
    private Bitmap bitmapImage;
    private long uploadedTime;
    private String newImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Set Photo");
        userLocalStore = new UserLocalStore(this);
        setProfileImage();
        bEditImage.setOnClickListener(this);
        imgRotate.setOnClickListener(this);
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
            isNewPhotoSet = true; // User selected a new image
            isNewPhotoSetAlias = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (isNewPhotoSet) {
            showAlertDialog();
        } else {
            Intent intent = new Intent();
            intent.putExtra("refresh", isNewPhotoSetAlias);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        }
    }


    private void showAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Are you sure you want to exit without saving?");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialogBuilder.setCancelable(false);
        dialogBuilder.setNegativeButton("No", null);
        dialogBuilder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitSave:
                uploadedTime = System.currentTimeMillis();
                retrofitUploadProfileImg();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void retrofitDeleteProfileImg() {
        String oldtime;
        HashMap<String, String> profileImageInfo = new HashMap<>();

        // the old time in link. used to delete the old profile image file
        if (userLocalStore.getLoggedInUser().getUserImageURL().contains("+")) {
            String[] split = userLocalStore.getLoggedInUser().getUserImageURL().split("\\+");
            String[] split2 = split[1].split("\\.");
            oldtime = split2[0];
            profileImageInfo.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
            profileImageInfo.put("oldTime", oldtime);

            Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                    .deleteProfilePic()
                    .deleteProfilePic(profileImageInfo);

            retrofit.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        }
    }


    private void retrofitUploadProfileImg() {

        showProgressDialog();
        // Turn image into base 64 encoded string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ((BitmapDrawable) circleImageView
                .getDrawable())
                .getBitmap()
                .compress(Bitmap
                        .CompressFormat
                        .JPEG, 15, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        HashMap<String, String> profileImageInfo = new HashMap<>();
        profileImageInfo.put("image", encodedImage);
        profileImageInfo.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        profileImageInfo.put("time", Long.toString(uploadedTime));

        newImageURL = "http://backpackerbuddy.net23.net/profile_pic/" + Integer.toString(userLocalStore.getLoggedInUser().getUserID())
                 + "+" + Long.toString(uploadedTime) + ".JPG";

        Call<JsonObject> jsonObjectCall = RetrofitUserInfoSingleton
                .getRetrofitUserInfo()
                .updateProfilePic()
                .updateProfilePic(profileImageInfo);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 1) {
                    retrofitDeleteProfileImg(); // delete old photo

                    // After uploaded
                    isNewPhotoSet = false;
                    Helpers.hideProgressDialog(progressDialog);

                    Intent intent = new Intent();
                    intent.putExtra("refresh", isNewPhotoSetAlias);
                    setResult(RESULT_OK, intent);

                    userLocalStore.changeImageURL(newImageURL);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(EditPhotoActivity.this, "Upload Error");
                Helpers.hideProgressDialog(progressDialog);
            }
        });
    }

    private void setProfileImage() {

        final ProgressDialog progressDialog = Helpers.showProgressDialog(this, "Loading...");
        if ((userLocalStore.getLoggedInUser().getUserImageURL() == null) ||
                (userLocalStore.getLoggedInUser().getUserImageURL().equals("0"))) {
            Picasso.with(this)
                    .load("http://i.imgur.com/268p4E0.jpg")
                    .noFade()
                    .fit()
                    .centerCrop()
                    .into(circleImageView);
        } else {
            Picasso.with(this)
                    .load(userLocalStore.getLoggedInUser().getUserImageURL())
                    .noFade()
                    .fit()
                    .centerCrop()
                    .into(circleImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            bEditImage.setVisibility(View.VISIBLE);
                            Helpers.hideProgressDialog(progressDialog);
                        }

                        @Override
                        public void onError() {
                            Helpers.hideProgressDialog(progressDialog);
                            Toast.makeText(EditPhotoActivity.this, "Error loading", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    @Override
    public void showUploadError() {
        Helpers.displayToast(EditPhotoActivity.this, "Upload Error");
    }

    @Override
    public void newPhotoSet() {
        isNewPhotoSet = false;
    }

    @Override
    public void hideProgressDialog() {
        Helpers.hideProgressDialog(progressDialog);
    }

    @Override
    public void showProgressDialog() {
        this.progressDialog = Helpers.showProgressDialog(this, "Uploading");
    }
}
