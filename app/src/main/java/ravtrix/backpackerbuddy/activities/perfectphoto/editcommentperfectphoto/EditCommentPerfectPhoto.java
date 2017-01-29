package ravtrix.backpackerbuddy.activities.perfectphoto.editcommentperfectphoto;

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


public class EditCommentPerfectPhoto extends OptionMenuSaveBaseActivity implements IEditCommentPerfectPhotoView {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_edit_comment_relative) protected RelativeLayout relativeLayout;
    @BindView(R.id.activity_edit_comment_etEditComment) protected EditText etComment;
    private String comment = "";
    private int commentID;
    private EditCommentPerfectPhotoPresenter editCommentPerfectPhotoPresenter;

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

        editCommentPerfectPhotoPresenter = new EditCommentPerfectPhotoPresenter(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:

                if (etComment.getText().toString().trim().length() == 0) {
                    Helpers.displayToast(this, "Post is too short...");
                } else {
                    // Update comment
                    editCommentPerfectPhotoPresenter.updateComment(commentID, etComment.getText().toString().trim());
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
        setResult(code); // result code 1
    }

    @Override
    public void finished() {
        finish();
    }

    @Override
    public void showErrorToast() {
        Helpers.displayErrorToast(this);
    }
}
