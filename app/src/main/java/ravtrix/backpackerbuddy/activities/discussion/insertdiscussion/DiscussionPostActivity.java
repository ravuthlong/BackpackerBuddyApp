package ravtrix.backpackerbuddy.activities.discussion.insertdiscussion;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuPostBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class DiscussionPostActivity extends OptionMenuPostBaseActivity implements IDiscussionPostView {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.etDiscussion) protected EditText etDiscussion;
    @BindView(R.id.linear_newDiscussion) protected LinearLayout linearNewDis;
    private DiscussionPostPresenter discussionPostPresenter;
    private UserLocalStore userLocalStore;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_post);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, linearNewDis);
        this.setTitle("New Discussion");

        this.discussionPostPresenter = new DiscussionPostPresenter(this);
        userLocalStore = new UserLocalStore(this);
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

                if (etDiscussion.getText().toString().trim().length() < 10) {
                    Helpers.displayToast(this, "Post is too short...");
                } else if (etDiscussion.getText().toString().trim().length() >= 500) {
                    Helpers.displayToast(this, "Exceeded max character count (500)");
                }else {
                    discussionPostPresenter.insertDiscussion(getDiscussionHash());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private HashMap<String, String> getDiscussionHash() {

        HashMap<String, String> newDiscussion = new HashMap<>();
        newDiscussion.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        newDiscussion.put("post", etDiscussion.getText().toString());
        newDiscussion.put("time", Long.toString(System.currentTimeMillis()));

        return newDiscussion;
    }

    @Override
    public void startDiscussionPostActivity() {
        startActivity(new Intent(DiscussionPostActivity.this, UserMainPage.class));
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(this);
    }
}
