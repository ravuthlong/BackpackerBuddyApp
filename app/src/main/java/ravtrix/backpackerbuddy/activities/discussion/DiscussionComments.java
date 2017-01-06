package ravtrix.backpackerbuddy.activities.discussion;

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
import android.widget.Toast;

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
import ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview.CommentDiscussionAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview.CommentModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscussionComments extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.submitButton) protected Button submitButton;
    @BindView(R.id.etComment) protected EditText etComment;
    @BindView(R.id.recyclerView_comment) protected RecyclerView recyclerView;
    @BindView(R.id.linearRecycler) protected LinearLayout linearLayout;
    @BindView(R.id.linearProgressbar) protected LinearLayout linearProg;
    private int discussionID, ownerID;
    private UserLocalStore userLocalStore;
    private CommentDiscussionAdapter commentDiscussionAdapter;
    private List<CommentModel> commentModels;
    private long mLastClickTime = 0;
    private int backPressExit = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_comments);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Comments");

        linearProg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(this, R.drawable.line_divider_inbox);
        recyclerView.addItemDecoration(dividerDecorator);

        getDiscussionBundle();
        userLocalStore = new UserLocalStore(this);
        submitButton.setOnClickListener(this);
        fetchDiscussionComments();
    }

    @Override
    public void onClick(View view) {

        // Prevents double clicking
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch(view.getId()) {
            case R.id.submitButton:
                mLastClickTime = SystemClock.elapsedRealtime();

                if (userLocalStore.getLoggedInUser().getUserID() != 0) {
                    if (etComment.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Empty comment...", Toast.LENGTH_SHORT).show();
                    } else {
                        HashMap<String, String> discussionHash = new HashMap<>();
                        discussionHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                        discussionHash.put("discussionID", Integer.toString(discussionID));
                        discussionHash.put("comment", etComment.getText().toString().trim());
                        discussionHash.put("time", Long.toString(System.currentTimeMillis()));
                        insertCommentRetrofit(discussionHash);
                    }
                } else {
                    Helpers.displayToast(this, "Become a member to comment");
                }
                break;
            default:
                break;
        }
    }

    private void insertCommentRetrofit(final HashMap<String, String> discussionHash) {
        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .insertComment()
                .insertComment(discussionHash);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 0) {
                    Toast.makeText(DiscussionComments.this, "Error",
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
                    incrementTotalComment(discussionID);
                    if (commentDiscussionAdapter != null) {
                        commentDiscussionAdapter.clearData();
                    }
                    // Don't notify if user comments on their own post..
                    if (userLocalStore.getLoggedInUser().getUserID() != ownerID) {
                        notifyTheOwner(ownerID, discussionHash.get("comment"), discussionID);
                    }
                    fetchDiscussionComments();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(DiscussionComments.this, "Error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDiscussionComments() {
        Call<List<CommentModel>> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getDiscussionComments()
                .getDiscussionComments(userLocalStore.getLoggedInUser().getUserID(), discussionID);

        retrofit.enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {

                if (response.body().get(0).getSuccess() == 1) {
                    commentModels = response.body(); // GSON convert json into models
                } else {
                    commentModels = new ArrayList<>(); // empty list
                    linearProg.setVisibility(View.GONE);
                }
                commentDiscussionAdapter = new CommentDiscussionAdapter(DiscussionComments.this,
                        commentModels, userLocalStore);
                recyclerView.setAdapter(commentDiscussionAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(DiscussionComments.this));

                // Bring recycler view to end of its scroll to see new comment

                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DiscussionComments.this);
                linearLayoutManager.scrollToPosition(commentModels.size() - 1);
                recyclerView.setLayoutManager(linearLayoutManager);
                displayAfterLoading();

            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                displayAfterLoading();

            }
        });
    }

    public void fetchDiscussionCommentsRefresh() {
        linearProg.setVisibility(View.VISIBLE);

        Call<List<CommentModel>> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getDiscussionComments()
                .getDiscussionComments(userLocalStore.getLoggedInUser().getUserID(), discussionID);

        retrofit.enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                linearProg.setVisibility(View.GONE);

                if (response.body().get(0).getSuccess() == 1) {
                    commentModels = response.body(); // GSON convert json into models
                } else {
                    commentModels = new ArrayList<>(); // empty list
                }
                commentDiscussionAdapter.swap(commentModels);
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                Helpers.displayErrorToast(DiscussionComments.this);
            }
        });
    }

    private void notifyTheOwner(int userID, String comment, int discussionID) {

        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .sendNotification()
                .sendNotification(userID, comment, discussionID);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // status 0 = error, status 1 = ok
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) { // refresh from edit comment
                fetchDiscussionCommentsRefresh();
            }
        }
    }

    private void incrementTotalComment(int discussionID) {
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

    private void getDiscussionBundle() {
        Bundle discussionBundle = getIntent().getExtras();
        if (discussionBundle != null) {
            discussionID = discussionBundle.getInt("discussionID");
            ownerID = discussionBundle.getInt("ownerID");

            if (discussionBundle.containsKey("backpressExit")) {
                backPressExit = discussionBundle.getInt("backpressExit");
            }
        }
    }

    private void displayAfterLoading() {
        linearProg.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {

        if (backPressExit == 0) {
            startActivity(new Intent(this, UserMainPage.class));
        } else {
            super.onBackPressed();
        }
    }
}
