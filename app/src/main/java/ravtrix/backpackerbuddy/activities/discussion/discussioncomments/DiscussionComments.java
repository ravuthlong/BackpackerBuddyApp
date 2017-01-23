package ravtrix.backpackerbuddy.activities.discussion.discussioncomments;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview.CommentDiscussionAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview.CommentModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;

public class DiscussionComments extends AppCompatActivity implements View.OnClickListener, IDiscussionCommentsView {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.submitButton) protected Button submitButton;
    @BindView(R.id.etComment) protected EditText etComment;
    @BindView(R.id.recyclerView_comment) protected RecyclerView recyclerView;
    @BindView(R.id.linearRecycler) protected LinearLayout linearLayout;
    @BindView(R.id.linearProgressbar) protected LinearLayout linearProg;
    private int discussionID, ownerID;
    private DiscussionCommentsPresenter discussionCommentsPresenter;
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

        hideProgressbar();
        hideRecyclerView();

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(this, R.drawable.line_divider_inbox);
        recyclerView.addItemDecoration(dividerDecorator);

        getDiscussionBundle();
        userLocalStore = new UserLocalStore(this);
        discussionCommentsPresenter = new DiscussionCommentsPresenter(this);
        submitButton.setOnClickListener(this);

        discussionCommentsPresenter.fetchDiscussionComments(ownerID, discussionID);
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
                if (userLocalStore.getLoggedInUser().getUserID() != 0) {
                    if (etComment.getText().toString().trim().isEmpty()) {
                        Helpers.displayToast(this, "Empty comment...");
                    } else if (etComment.getText().toString().trim().length() >= 500) {
                        Helpers.displayToast(this, "Exceeded max character count (500)");
                    } else {
                        // Submit comment
                        discussionCommentsPresenter.insertComment(getDiscussionHash(),
                                userLocalStore.getLoggedInUser().getUserID(), discussionID, ownerID);
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
                discussionCommentsPresenter.fetchDiscussionCommentsRefresh(ownerID, discussionID);
            }
        }
    }

    private void getDiscussionBundle() {
        Bundle discussionBundle = getIntent().getExtras();
        if (discussionBundle != null) {
            discussionID = discussionBundle.getInt("discussionID");
            ownerID = discussionBundle.getInt("ownerID");

            if (discussionBundle.containsKey("backpressExit")) {
                // Push notification send this key.
                backPressExit = discussionBundle.getInt("backpressExit");
            }
        }
    }

    @Override
    public void onBackPressed() {

         // Backpressexit is 0 if the user hits DiscussionComments activity from a push notification
        if (backPressExit == 0) {
            Intent intent = new Intent(this, UserMainPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void hideKeyboard() {
        // Hide keyboard
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(this);
    }

    @Override
    public void setModelsEmpty() {
        this.commentModels = new ArrayList<>(); // empty list
    }

    @Override
    public void setModels(List<CommentModel> commentModels) {
        this.commentModels = commentModels;
    }

    @Override
    public void setRecyclerView() {
        commentDiscussionAdapter = new CommentDiscussionAdapter(DiscussionComments.this,
                commentModels, userLocalStore);
        recyclerView.setAdapter(commentDiscussionAdapter);
        // Bring recycler view to end of its scroll to see new comment
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DiscussionComments.this);
        linearLayoutManager.scrollToPosition(commentModels.size() - 1);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void showDisplayAfterLoading() {
        linearProg.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        linearProg.setVisibility(View.GONE);
    }

    @Override
    public void showProgressbar() {
        linearProg.setVisibility(View.VISIBLE);
    }

    @Override
    public void swapModels(List<CommentModel> commentModels) {
        commentDiscussionAdapter.swap(commentModels);
    }

    @Override
    public void clearEditText() {
        etComment.getText().clear();
    }

    @Override
    public void clearData() {
        if (commentDiscussionAdapter != null) {
            commentDiscussionAdapter.clearData();
        }
    }

    @Override
    public void hideRecyclerView() {
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private HashMap<String, String> getDiscussionHash() {
        final HashMap<String, String> discussionHash = new HashMap<>();
        discussionHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        discussionHash.put("discussionID", Integer.toString(discussionID));
        discussionHash.put("comment", etComment.getText().toString().trim());
        discussionHash.put("time", Long.toString(System.currentTimeMillis()));
        return discussionHash;
    }

    public void fetchDiscussionCommentsRefresh() {
        discussionCommentsPresenter.fetchDiscussionCommentsRefresh(ownerID, discussionID);
    }
}
