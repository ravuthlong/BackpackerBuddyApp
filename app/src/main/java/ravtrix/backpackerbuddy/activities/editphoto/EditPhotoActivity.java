package ravtrix.backpackerbuddy.activities.editphoto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
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
    private static final int RESULT_LOAD_IMAGE = 1;
    private boolean isNewPhotoSet = false;
    private UserLocalStore userLocalStore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        ButterKnife.bind(this);
        userLocalStore = new UserLocalStore(this);

        checkProfileImage();
        Helpers.setToolbar(this, toolbar);
        setTitle("Set Photo");
        bEditImage.setOnClickListener(this);
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

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            String picturePath = null;

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
            }

            circleImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            isNewPhotoSet = true; // User selected a new image
        }
    }

    @Override
    public void onBackPressed() {
        if (isNewPhotoSet) {
            showAlertDialog();
        } else {
            super.onBackPressed();
            Intent intent = new Intent();
            intent.putExtra("refresh", 0);
            setResult(RESULT_OK, intent);
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
                retrofitUploadProfileImg();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

        Call<JsonObject> jsonObjectCall = RetrofitUserInfoSingleton
                .getRetrofitUserInfo()
                .updateProfilePic()
                .updateProfilePic(profileImageInfo);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 1) {

                    // After uploaded
                    isNewPhotoSet = false;
                    Helpers.hideProgressDialog(progressDialog);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(EditPhotoActivity.this, "Upload Error");
            }
        });
    }

    private void checkProfileImage() {
        if ((userLocalStore.getLoggedInUser().getUserImageURL() == null) ||
                (userLocalStore.getLoggedInUser().getUserImageURL().equals("0"))) {
            Picasso.with(this).load("http://i.imgur.com/268p4E0.jpg").noFade().into(circleImageView);
        } else {
            Picasso.with(this).load("http://backpackerbuddy.net23.net/profile_pic/" +
                    userLocalStore.getLoggedInUser().getUserID() + ".JPG").noFade().into(circleImageView);
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
