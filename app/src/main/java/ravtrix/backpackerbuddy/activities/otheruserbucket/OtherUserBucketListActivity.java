package ravtrix.backpackerbuddy.activities.otheruserbucket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.adapter.BucketListAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;

public class OtherUserBucketListActivity extends AppCompatActivity implements IOtherUserBucketListView {

    @BindView(R.id.activity_other_recyclerView) protected RecyclerView recyclerView;
    @BindView(R.id.activity_other_progressBar) protected ProgressBar progressBar;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_other_noBucket) protected TextView tvNoBucket;
    private String otherUserID;
    private List<BucketListModel> bucketListModels;
    private RecyclerView.ItemDecoration dividerDecorator;
    private OtherUserBucketListPresenter otherUserBucketListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_bucket_list);

        // View components set
        ButterKnife.bind(this);
        setTitle("Bucket List");
        setTypeface();
        Helpers.setToolbar(this, toolbar);
        dividerDecorator = new DividerDecoration(this, R.drawable.line_divider_inbox);

        // Data set
        setBundle();

        // Presenter set
        otherUserBucketListPresenter = new OtherUserBucketListPresenter(this);
        otherUserBucketListPresenter.fetchUserBucketList(otherUserID);
    }

    private void setBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            this.otherUserID = bundle.getString("otherUserID");
        }
    }

    private void setTypeface() {
        Helpers.overrideFonts(this, tvNoBucket);
    }

    @Override
    public void setBucketListModels(List<BucketListModel> bucketListModels) {
        this.bucketListModels = bucketListModels;
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(this);
    }

    @Override
    public void showTvNoBucket() {
        tvNoBucket.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRecyclerView() {
        BucketListAdapter bucketListAdapter = new BucketListAdapter(OtherUserBucketListActivity.this, null, bucketListModels);
        recyclerView.setAdapter(bucketListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(OtherUserBucketListActivity.this));
        recyclerView.addItemDecoration(dividerDecorator);
    }
}
