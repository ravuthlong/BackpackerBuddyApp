package ravtrix.backpackerbuddy.notificationactivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview.CommentModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data.DiscussionModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity is only started through a push notification click of new post comment
 */
public class NotificationPostActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.activity_notification_post_recyclerView) protected RecyclerView recyclerView;
    @BindView(R.id.activity_notification_post_progressBar) protected ProgressBar progressBar;
    @BindView(R.id.activity_notification_post_submitButton) protected Button submitButton;
    @BindView(R.id.activity_notification_post_etPost) protected EditText etPost;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private NotificationPostAdapter notificationPostAdapter;
    private UserLocalStore userLocalStore;
    private DiscussionModel discussionModel;
    private List<CommentModel> commentModels;
    private int ownerID;
    private int discussionID;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_post);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Post");

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.ownerID = bundle.getInt("ownerID");
            this.discussionID = bundle.getInt("discussionID");
        }

        this.userLocalStore = new UserLocalStore(this);
        discussionModel = new DiscussionModel();
        commentModels = new ArrayList<>();

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(this, R.drawable.line_divider_inbox);
        recyclerView.addItemDecoration(dividerDecorator);
        fetchParentDiscussion(userLocalStore.getLoggedInUser().getUserID(), discussionID);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {


        // Prevents double clicking
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (view.getId()) {
            case R.id.activity_notification_post_submitButton:

                if (etPost.getText().toString().trim().isEmpty()) {
                    Helpers.displayToast(this, "Empty comment...");
                } else if (etPost.getText().toString().trim().length() >= 500) {
                    Helpers.displayToast(this, "Exceeded max character count (500)");
                } else {
                    // Submit comment
                    HashMap<String, String> commentHash = new HashMap<>();
                    commentHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                    commentHash.put("discussionID", Integer.toString(discussionID));
                    commentHash.put("comment", etPost.getText().toString().trim());
                    commentHash.put("time", Long.toString(System.currentTimeMillis()));
                    insertCommentRetrofit(commentHash);
                }
                break;
            default:
                break;
        }
    }

    private void fetchParentDiscussion(int userID, final int discussionID) {

        Call<List<DiscussionModel>> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getADiscussion()
                .getADiscussion(discussionID, userID);

        retrofit.enqueue(new Callback<List<DiscussionModel>>() {
            @Override
            public void onResponse(Call<List<DiscussionModel>> call, Response<List<DiscussionModel>> response) {
                discussionModel = response.body().get(0);
                fetchDiscussionComments(userLocalStore.getLoggedInUser().getUserID(), discussionID);
            }

            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
                Helpers.displayErrorToast(NotificationPostActivity.this);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void fetchParentDiscussionRefresh(int userID, final int discussionID) {

        Call<List<DiscussionModel>> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getADiscussion()
                .getADiscussion(discussionID, userID);

        retrofit.enqueue(new Callback<List<DiscussionModel>>() {
            @Override
            public void onResponse(Call<List<DiscussionModel>> call, Response<List<DiscussionModel>> response) {
                discussionModel = response.body().get(0);
                notificationPostAdapter.swapParentDiscussion(discussionModel);
            }

            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
                Helpers.displayErrorToast(NotificationPostActivity.this);
            }
        });
    }

    private void fetchDiscussionComments(int userID, int discussionID) {

        Call<List<CommentModel>> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getDiscussionComments()
                .getDiscussionComments(userID, discussionID);

        retrofit.enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                commentModels = response.body();

                notificationPostAdapter = new NotificationPostAdapter(NotificationPostActivity.this, commentModels,discussionModel,  userLocalStore);
                recyclerView.setAdapter(notificationPostAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(NotificationPostActivity.this));
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {

            }
        });
    }

    public void fetchDiscussionCommentsRefresh(int userID, int discussionID) {

        Call<List<CommentModel>> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getDiscussionComments()
                .getDiscussionComments(userID, discussionID);

        retrofit.enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                commentModels = response.body();
                notificationPostAdapter.swapComments(commentModels);
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {

            }
        });
    }

    public void insertCommentRetrofit(final HashMap<String, String> discussionHash) {
        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .insertComment()
                .insertComment(discussionHash);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                etPost.getText().clear();
                Helpers.hideKeyboard(NotificationPostActivity.this);
                fetchDiscussionCommentsRefresh(userLocalStore.getLoggedInUser().getUserID(), discussionID);
                incrementTotalComment(discussionID);

                // scroll recycler view to last position to see new comment
                recyclerView.scrollToPosition(commentModels.size());

                // Don't notify if user comments on their own post..
                if (userLocalStore.getLoggedInUser().getUserID() != ownerID) {
                    notifyTheOwner(ownerID, discussionHash.get("comment"), discussionID);
                }
                // Notify all other users that has commented in this post that is NOT the current poster
                notifyOtherUsers(userLocalStore.getLoggedInUser().getUserID(), ownerID, discussionHash.get("comment"), discussionID);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayErrorToast(NotificationPostActivity.this);
            }
        });
    }


    public void notifyTheOwner(int userID, String comment, int discussionID) {
        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .sendNotification()
                .sendNotification(userID, comment, discussionID);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    public void notifyOtherUsers(int userID, int ownerID, String comment, int discussionID) {
        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .sendNotificationToOtherUsers()
                .sendNotificationToOtherUsers(userID, ownerID, comment, discussionID);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {}
        });
    }

    public void incrementTotalComment(int discussionID) {
        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .incrementCommentCount()
                .incrementCommentCount(Integer.toString(discussionID));

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1:
                fetchParentDiscussionRefresh(userLocalStore.getLoggedInUser().getUserID(), discussionID);
                break;
            case 2:
                fetchDiscussionCommentsRefresh(userLocalStore.getLoggedInUser().getUserID(), discussionID);
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, UserMainPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
