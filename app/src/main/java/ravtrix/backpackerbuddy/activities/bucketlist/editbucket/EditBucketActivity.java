package ravtrix.backpackerbuddy.activities.bucketlist.editbucket;

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

public class EditBucketActivity extends OptionMenuSaveBaseActivity implements IEditBucketView {
    @BindView(R.id.activity_edit_bucket_etBucket) protected EditText etBucket;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_edit_bucket_relative) protected RelativeLayout relativeLayoutBucket;
    private int bucketID;
    private String oldBucket;
    private EditBucketPresenter editBucketPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bucket);
        ButterKnife.bind(this);
        setTitle("Edit Goal");
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, relativeLayoutBucket);

        getBundle(); // get bundle from bucket list fragment

        editBucketPresenter = new EditBucketPresenter(this);

        etBucket.setText(oldBucket);
        etBucket.setSelection(etBucket.getText().length());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:
                if (etBucket.getText().toString().trim().length() < 10) {
                    Helpers.displayToast(this, "Goal is too short...");
                } else {
                    // Call presenter to edit text
                    editBucketPresenter.editBucket(bucketID, etBucket.getText().toString().trim());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayToast(EditBucketActivity.this, "Error");
    }

    @Override
    public void setResultCode(int code) {
        setResult(code);
    }

    @Override
    public void finished() {
        finish();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.bucketID = bundle.getInt("bucketID");
            this.oldBucket = bundle.getString("post");
        }
    }
}
