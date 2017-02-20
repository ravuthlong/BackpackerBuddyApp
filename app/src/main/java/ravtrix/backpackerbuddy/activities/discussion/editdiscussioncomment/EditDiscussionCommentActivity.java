package ravtrix.backpackerbuddy.activities.discussion.editdiscussioncomment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;

public class EditDiscussionCommentActivity extends OptionMenuSaveBaseActivity implements IEditDiscussionCommentView {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.etEditComment) protected EditText etEditComment;
    @BindView(R.id.relativeEditComment) protected RelativeLayout relativeLayout;
    private EditDiscussionCommentPresenter editDiscussionCommentPresenter;
    private String comment = "";
    private int commentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, relativeLayout);
        setTitle("Edit Comment");

        this.editDiscussionCommentPresenter = new EditDiscussionCommentPresenter(this);

        getBundle();

        Helpers.overrideFonts(this, etEditComment);
        etEditComment.setText(comment);
        etEditComment.setSelection(etEditComment.getText().length());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:
                if (etEditComment.getText().toString().trim().length() == 0) {
                    Helpers.displayToast(this, "Post is too short...");
                } else {
                    // Update comment
                    editDiscussionCommentPresenter.updateComment(commentID, etEditComment.getText().toString().trim());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            comment = bundle.getString("comment");
            commentID = bundle.getInt("commentID");
        }
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(this);
    }

    @Override
    public void hideKeyboard() {
        // Hide soft keyboard
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void setResultCode(int code) {
        setResult(code);
    }

    @Override
    public void finished() {
        finish();
    }
}
