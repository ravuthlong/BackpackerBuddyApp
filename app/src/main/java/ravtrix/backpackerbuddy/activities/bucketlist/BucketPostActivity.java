package ravtrix.backpackerbuddy.activities.bucketlist;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuPostBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserBucketListSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BucketPostActivity extends OptionMenuPostBaseActivity {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_bucket_etNewBucket) protected EditText etNewBucket;
    private long mLastClickTime = 0;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_post);
        ButterKnife.bind(this);
        setTitle("New Goal");

        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, etNewBucket);

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

                if (etNewBucket.getText().toString().trim().length() < 10) {
                    Toast toast = Toast.makeText(BucketPostActivity.this, "Goal too short...", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (etNewBucket.getText().toString().trim().length() >= 500) {
                    Toast.makeText(this, "Exceeded max character count (500)", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert into database
                    HashMap<String, String> bucketHash = new HashMap<>();
                    bucketHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                    bucketHash.put("post", etNewBucket.getText().toString().trim());
                    bucketHash.put("time", Long.toString(System.currentTimeMillis()));
                    insertPostRetrofit(bucketHash);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void insertPostRetrofit(HashMap<String, String> bucketInfo) {

        Call<JsonObject> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .insertBucket()
                .insertBucket(bucketInfo);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body().get("status").getAsInt() == 1) {
                    setResult(1); // Send back to calling activity, which is the Bucket fragment
                    finish();
                } else {
                    displayToastError();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                displayToastError();
            }
        });
    }

    private void displayToastError() {
        Toast toast = Toast.makeText(BucketPostActivity.this, "Error", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

