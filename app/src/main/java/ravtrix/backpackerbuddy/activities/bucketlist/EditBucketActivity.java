package ravtrix.backpackerbuddy.activities.bucketlist;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserBucketListSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBucketActivity extends OptionMenuSaveBaseActivity {
    @BindView(R.id.activity_edit_bucket_etBucket) protected EditText etBucket;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private int bucketID;
    private String oldBucket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bucket);
        ButterKnife.bind(this);
        setTitle("Edit Goal");

        Helpers.setToolbar(this, toolbar);

        getBundle();
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
                    editBucketRetrofit();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Edit bucket post, given
     */
    private void editBucketRetrofit() {

        Call<JsonObject> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .updateBucket()
                .updateBucket(bucketID, etBucket.getText().toString().trim());

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body().get("status").getAsInt() == 1) {
                    setResult(1); // Pass result code 2 back to BucketListFrag
                    finish();
                } else {
                    Helpers.displayToast(EditBucketActivity.this, "Error");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(EditBucketActivity.this, "Error");
            }
        });
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            this.bucketID = bundle.getInt("bucketID");
            this.oldBucket = bundle.getString("post");
        }
    }

}
