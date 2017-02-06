package ravtrix.backpackerbuddy.activities.discussion.editdiscussion;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;

public class EditDiscussionActivity extends OptionMenuSaveBaseActivity implements IEditDiscussionView {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.relativeEditDiscussion) protected RelativeLayout relativeEdit;
    @BindView(R.id.etEditDiscussion) protected EditText etEditDiscussion;
    private EditDiscussionPresenter editDiscussionPresenter;
    private int discussionID;
    private String post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_discussion);
        ButterKnife.bind(this);
        setTitle("Edit Post");
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, relativeEdit);

        editDiscussionPresenter = new EditDiscussionPresenter(this);

        getBundle();
        etEditDiscussion.setText(post);
        etEditDiscussion.setSelection(etEditDiscussion.getText().length());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:

                if (etEditDiscussion.getText().toString().trim().length() < 10) {
                    Helpers.displayToast(this, "Post is too short...");
                } else {
                    editDiscussionPresenter.editDiscussion(discussionID, etEditDiscussion.getText().toString().trim());
                    Helpers.hideKeyboard(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            discussionID = bundle.getInt("discussionID");
            post = bundle.getString("post");
        }
    }

    @Override
    public void setResultCode(int code) {
        setResult(1);
    }

    @Override
    public void finished() {
        finish();
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(this);
    }
}

