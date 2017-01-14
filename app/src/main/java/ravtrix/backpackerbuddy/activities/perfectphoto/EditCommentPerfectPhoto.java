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


public class EditCommentPerfectPhoto extends OptionMenuSaveBaseActivity {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_edit_comment_relative) protected RelativeLayout relativeLayout;
    @BindView(R.id.activity_edit_comment_etEditComment) protected EditText etComment;
    private String comment = "";
    private int commentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment_perfect_photo);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Edit Comment");

        getBundle();
        etComment.setText(comment);
        etComment.setSelection(etComment.getText().length());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:

                if (etComment.getText().toString().trim().length() == 0) {
                    Helpers.displayToast(this, "Post is too short...");
                } else {
                    // Update comment
                    updateCommentRetrofit(commentID, etComment.getText().toString().trim());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateCommentRetrofit(int commentID, String newComment) {

        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .updatePhotoComment()
                .updatePhotoComment(commentID, newComment);
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
                    setResult(1); // result code 1
                    finish();
                } else {
                    Helpers.displayErrorToast(EditCommentPerfectPhoto.this);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayErrorToast(EditCommentPerfectPhoto.this);
            }
        });

    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            comment = bundle.getString("comment");
            commentID = bundle.getInt("commentID");
        }
    }
}
