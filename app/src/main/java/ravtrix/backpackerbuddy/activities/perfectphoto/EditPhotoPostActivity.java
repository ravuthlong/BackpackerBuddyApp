package ravtrix.backpackerbuddy.activities.perfectphoto;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitPerfectPhotoSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPhotoPostActivity extends OptionMenuSaveBaseActivity {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_edit_photo_relative) protected RelativeLayout relativeLayout;
    @BindView(R.id.activity_edit_photo_etEditComment) protected EditText etPost;
    private String post = "";
    private int photoID;
    private static final int REQUEST_REFRESH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo_post);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Edit Post");

        getBundle();
        etPost.setText(post);
        etPost.setSelection(etPost.getText().length());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:
                if (etPost.getText().toString().trim().length() == 0) {
                    Helpers.displayToast(this, "Post is too short...");
                } else {
                    // Update post
                    updatePhotoPost(photoID, etPost.getText().toString().trim());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updatePhotoPost(int photoID, String post) {

        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .updatePhotoPost()
                .updatePhotoPost(photoID, post);
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 1) {

                    // Hide soft keyboard
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    setResult(REQUEST_REFRESH); // result code 1
                    finish();
                } else {
                    Helpers.displayErrorToast(EditPhotoPostActivity.this);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayErrorToast(EditPhotoPostActivity.this);
            }
        });
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            post = bundle.getString("post");
            photoID = bundle.getInt("photoID");
        }
    }
}
