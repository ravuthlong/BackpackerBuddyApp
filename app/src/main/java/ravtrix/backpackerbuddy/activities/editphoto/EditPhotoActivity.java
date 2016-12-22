package ravtrix.backpackerbuddy.activities.editphoto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
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
                bitmapImage = rotateBitmap(bitmapImage);
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
                bitmapImage = decodeBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            circleImageView.setImageBitmap(bitmapImage);
            imgRotate.setVisibility(View.VISIBLE);
            isNewPhotoSet = true; // User selected a new image
            isNewPhotoSetAlias = true;
        }
    }

    /**
     * Scale image
     * @param selectedImage     the image selected by user
     * @return                  scaled version of bitmap
     * @throws FileNotFoundException
     */
    public  Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 300;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }


    private Bitmap rotateBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        return Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
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

                    Intent intent = new Intent();
                    intent.putExtra("refresh", isNewPhotoSetAlias);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(EditPhotoActivity.this, "Upload Error");
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
                    .load("http://backpackerbuddy.net23.net/profile_pic/" +
                    userLocalStore.getLoggedInUser().getUserID() + ".JPG")
                    .noFade()
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
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
