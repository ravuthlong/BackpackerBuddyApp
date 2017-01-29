package ravtrix.backpackerbuddy.activities.perfectphoto.editphotopostperfect;

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

public class EditPhotoPostActivity extends OptionMenuSaveBaseActivity implements IEditPhotoPostView {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_edit_photo_relative) protected RelativeLayout relativeLayout;
    @BindView(R.id.activity_edit_photo_etEditComment) protected EditText etPost;
    private String post = "";
    private int photoID;
    private EditPhotoPostPresenter editPhotoPostPresenter;

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

        editPhotoPostPresenter = new EditPhotoPostPresenter(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:
                if (etPost.getText().toString().trim().length() == 0) {
                    Helpers.displayToast(this, "Post is too short...");
                } else {
                    // Update post
                    editPhotoPostPresenter.updatePhotoPost(photoID, etPost.getText().toString().trim());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            post = bundle.getString("post");
            photoID = bundle.getInt("photoID");
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
        setResult(code);
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
