package ravtrix.backpackerbuddy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.BucketComparator;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserBucketListSingleton;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.adapter.BucketListAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherUserBucketListActivity extends AppCompatActivity {

    @BindView(R.id.activity_other_recyclerView) protected RecyclerView recyclerView;
    @BindView(R.id.activity_other_progressBar) protected ProgressBar progressBar;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_other_noBucket) protected TextView tvNoBucket;
    private String otherUserID;
    private List<BucketListModel> bucketListModels;
    private RecyclerView.ItemDecoration dividerDecorator;
    private BucketListAdapter bucketListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_bucket_list);
        ButterKnife.bind(this);
        setTitle("Bucket List");

        progressBar.setVisibility(View.VISIBLE);
        Helpers.setToolbar(this, toolbar);

        dividerDecorator = new DividerDecoration(this, R.drawable.line_divider_inbox);

        setBundle();
        setTypeface();
        fetchUserBucketList();
    }

    private void setBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            this.otherUserID = bundle.getString("otherUserID");
        }
    }

    private void fetchUserBucketList() {

        Call<List<BucketListModel>> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .fetchUserBucketList()
                .fetchUserBucketList(Integer.parseInt(otherUserID));

        retrofit.enqueue(new Callback<List<BucketListModel>>() {
            @Override
            public void onResponse(Call<List<BucketListModel>> call, Response<List<BucketListModel>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.body().get(0).getSuccess() == 0) {
                    // Empty list
                    recyclerView.setVisibility(View.GONE);
                    tvNoBucket.setVisibility(View.VISIBLE);
                } else {
                    bucketListModels = response.body();
                    Collections.sort(bucketListModels, new BucketComparator());

                    bucketListAdapter = new BucketListAdapter(OtherUserBucketListActivity.this, null, bucketListModels);
                    recyclerView.setAdapter(bucketListAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(OtherUserBucketListActivity.this));
                    recyclerView.addItemDecoration(dividerDecorator);
                }
            }

            @Override
            public void onFailure(Call<List<BucketListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Helpers.displayToast(OtherUserBucketListActivity.this, "Error");
            }
        });
    }

    private void setTypeface() {
        Helpers.overrideFonts(this, tvNoBucket);
    }
}
