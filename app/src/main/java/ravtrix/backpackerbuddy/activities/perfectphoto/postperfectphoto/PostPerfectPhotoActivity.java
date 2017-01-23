package ravtrix.backpackerbuddy.activities.perfectphoto.postperfectphoto;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuPostBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitPerfectPhotoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostPerfectPhotoActivity extends OptionMenuPostBaseActivity {

    @BindView(R.id.activity_post_perfect_imgImage) protected ImageView imgPerfect;
    @BindView(R.id.activity_post_perfect_post) protected EditText post;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private static final int REQUEST_REFRESH = 1;
    private static final int RESULT_REFRESH = 1;
    private long mLastClickTime = 0;
    private UserLocalStore userLocalStore;
    private Bitmap thumbnailBitmap;

    public static void startWithUri(Context context, Fragment fragment, Uri uri) {
        Intent intent = new Intent(context, PostPerfectPhotoActivity.class);
        intent.setData(uri);
        fragment.startActivityForResult(intent, REQUEST_REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_perfect_photo);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Your Perfect Photo");

        userLocalStore = new UserLocalStore(this);
        Uri imageUri = getIntent().getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            thumbnailBitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.08), (int)(bitmap.getHeight()*0.08), true);
            imgPerfect.setImageBitmap(bitmap);
        } catch (IOException e) {e.printStackTrace();}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitPost:
                // Prevents double clicking
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return false;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (post.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Empty post...", Toast.LENGTH_SHORT).show();
                } else if (post.getText().toString().trim().length() >= 500) {
                    Toast.makeText(this, "Exceeded max character count (500)", Toast.LENGTH_SHORT).show();
                } else {
                   postNewPerfectPhoto();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void postNewPerfectPhoto() {
        final ProgressDialog progressDialog = Helpers.showProgressDialog(this, "Posting...");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ((BitmapDrawable) imgPerfect
                .getDrawable())
                .getBitmap()
                .compress(Bitmap
                        .CompressFormat
                        .JPEG, 100, byteArrayOutputStream);

        ByteArrayOutputStream byteArrayOutputStreamThumbNail = new ByteArrayOutputStream();
        thumbnailBitmap.compress(Bitmap.CompressFormat
                .JPEG, 100, byteArrayOutputStreamThumbNail);

        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        String encodedImageThumbnail = Base64.encodeToString(byteArrayOutputStreamThumbNail.toByteArray(), Base64.DEFAULT);

        HashMap<String, String> perfectPhotoInfo = new HashMap<>();
        perfectPhotoInfo.put("image", encodedImage);
        perfectPhotoInfo.put("thumbnail", encodedImageThumbnail);
        perfectPhotoInfo.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        perfectPhotoInfo.put("time", Long.toString(System.currentTimeMillis()));
        perfectPhotoInfo.put("post", post.getText().toString().trim());

        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .insertPerfectPhoto()
                .insertPerfectPhoto(perfectPhotoInfo);
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Helpers.hideProgressDialog(progressDialog);
                setResult(RESULT_REFRESH); // Send result 1 back to Fragment perfect photo to do refresh
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayErrorToast(PostPerfectPhotoActivity.this);
            }
        });
    }
}
