package ravtrix.backpackerbuddy.activities.bucketlist.newbucket;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuPostBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class BucketPostActivity extends OptionMenuPostBaseActivity implements IBucketPostView {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_bucket_etNewBucket) protected EditText etNewBucket;
    private long mLastClickTime = 0;
    private UserLocalStore userLocalStore;
    private BucketPostPresenter bucketPostPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_post);
        ButterKnife.bind(this);
        setTitle("New Goal");

        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, etNewBucket);

        userLocalStore = new UserLocalStore(this);
        bucketPostPresenter = new BucketPostPresenter(this);
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

                if (etNewBucket.getText().toString().trim().length() < 10) {
                    displayErrorToast("Goal too short...");
                } else if (etNewBucket.getText().toString().trim().length() >= 500) {
                    displayErrorToast("Exceeded max character count (500)");
                } else {
                    // Insert data and call presenter
                    HashMap<String, String> bucketHash = new HashMap<>();
                    bucketHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                    bucketHash.put("post", etNewBucket.getText().toString().trim());
                    bucketHash.put("time", Long.toString(System.currentTimeMillis()));
                    bucketPostPresenter.insertPost(bucketHash);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void displayErrorToast(String error) {
        Toast toast = Toast.makeText(BucketPostActivity.this, error, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void setResultCode(int code) {
        setResult(code); // Send back to calling activity, which is the Bucket fragment
    }

    @Override
    public void finished() {
        finish();
    }
}

