package ravtrix.backpackerbuddy.activities.perfectphoto.commentsperfectphoto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitPerfectPhotoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.commentphotorecyclerview.PhotoCommentAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.commentphotorecyclerview.PhotoCommentModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentPerfectPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_comment_photo_relativeMain) protected RelativeLayout relativeLayoutMain;
    @BindView(R.id.activity_comment_photo_linearProg) protected LinearLayout linearProg;
    @BindView(R.id.activity_comment_photo_recyclerView) protected RecyclerView recyclerViewComments;
    @BindView(R.id.activity_comment_photo_etComment) protected EditText etComment;
    @BindView(R.id.activity_comment_photo_submitButton) protected Button bSubmit;
    private UserLocalStore userLocalStore;
    private int photoID; // unique ID of the photo
    private int ownerID; // unique ID of the owner of the image post
    private long mLastClickTime = 0;
    private List<PhotoCommentModel> photoCommentModelList;
    private PhotoCommentAdapter photoCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_perfect_photo);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Comments");
        linearProg.setVisibility(View.VISIBLE);

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(this, R.drawable.line_divider_inbox);
        recyclerViewComments.addItemDecoration(dividerDecorator);

        getPhotoBundle();
        userLocalStore = new UserLocalStore(this);
        bSubmit.setOnClickListener(this);

        fetchPhotoComments();
    }

    @Override
    public void onClick(View view) {
        // Prevents double clicking
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (view.getId()) {
            case R.id.activity_comment_photo_submitButton:
                if (userLocalStore.getLoggedInUser().getUserID() != 0) {

                    if (etComment.getText().toString().trim().isEmpty()) {
                        Toast.makeText(this, "Empty comment...", Toast.LENGTH_SHORT).show();
                    } else if (etComment.getText().toString().trim().length() >= 500) {
                        Toast.makeText(this, "Exceeded max character count (500)", Toast.LENGTH_SHORT).show();
                    } else {
                        // Submit comment
                        insertPhotoCommentRetrofit();
                    }
                } else {
                    Helpers.displayToast(this, "Become a member to comment");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) { // refresh from edit comment
                fetchPhotoCommentsRefresh();
            }
        }
    }

    private void fetchPhotoComments() {

        Call<List<PhotoCommentModel>> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .getPhotoComments()
                .getPhotoComments(photoID);
        retrofit.enqueue(new Callback<List<PhotoCommentModel>>() {
            @Override
            public void onResponse(Call<List<PhotoCommentModel>> call, Response<List<PhotoCommentModel>> response) {
                linearProg.setVisibility(View.GONE);

                if (response.body().get(0).getSuccess() == 1) {
                    photoCommentModelList = response.body();
                } else {
                    // Empty list
                    photoCommentModelList = new ArrayList<>();
                }

                photoCommentAdapter = new PhotoCommentAdapter(CommentPerfectPhotoActivity.this, photoCommentModelList,
                        userLocalStore.getLoggedInUser().getUserID());
                recyclerViewComments.setAdapter(photoCommentAdapter);
                recyclerViewComments.setLayoutManager(new LinearLayoutManager(CommentPerfectPhotoActivity.this));

                // Bring recycler view to end of its scroll to see new comment
                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentPerfectPhotoActivity.this);
                linearLayoutManager.scrollToPosition(photoCommentModelList.size() - 1);
                recyclerViewComments.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onFailure(Call<List<PhotoCommentModel>> call, Throwable t) {
                linearProg.setVisibility(View.GONE);
            }
        });
    }

    public void fetchPhotoCommentsRefresh() {

        Call<List<PhotoCommentModel>> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .getPhotoComments()
                .getPhotoComments(photoID);
        retrofit.enqueue(new Callback<List<PhotoCommentModel>>() {
            @Override
            public void onResponse(Call<List<PhotoCommentModel>> call, Response<List<PhotoCommentModel>> response) {
                linearProg.setVisibility(View.GONE);

                if (response.body().get(0).getSuccess() == 1) {
                    photoCommentModelList = response.body();
                } else {
                    // Empty list
                    photoCommentModelList = new ArrayList<>();
                }
                photoCommentAdapter.swap(photoCommentModelList);
                // Bring recycler view to end of its scroll to see new comment
                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentPerfectPhotoActivity.this);
                linearLayoutManager.scrollToPosition(photoCommentModelList.size() - 1);
                recyclerViewComments.setLayoutManager(linearLayoutManager);
            }
            @Override
            public void onFailure(Call<List<PhotoCommentModel>> call, Throwable t) {
                linearProg.setVisibility(View.GONE);
                Helpers.displayErrorToast(CommentPerfectPhotoActivity.this);
            }
        });
    }

    private void insertPhotoCommentRetrofit() {
        HashMap<String, String> photoHash = new HashMap<>();
        photoHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        photoHash.put("photoID", Integer.toString(photoID));
        photoHash.put("comment", etComment.getText().toString().trim());
        photoHash.put("time", Long.toString(System.currentTimeMillis()));

        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .insertPhotoComment()
                .insertPhotoComment(photoHash);
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 0) {
                    Toast.makeText(CommentPerfectPhotoActivity.this, "Error",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Hide keyboard
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    etComment.getText().clear();
                    // Success
                    incrementTotalComment(photoID);
                    fetchPhotoCommentsRefresh(); // refresh page
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {}
        });
    }

    private void incrementTotalComment(int photoID) {

        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .incrementPhotoCommentCount()
                .incrementPhotoCommentCount(Integer.toString(photoID));
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {}
        });
    }

    private void getPhotoBundle() {
        Bundle discussionBundle = getIntent().getExtras();
        if (discussionBundle != null) {
            photoID = discussionBundle.getInt("photoID");
            ownerID = discussionBundle.getInt("ownerID");
            /*
            if (discussionBundle.containsKey("backpressExit")) {
                backPressExit = discussionBundle.getInt("backpressExit");
            }*/
        }
    }
}
