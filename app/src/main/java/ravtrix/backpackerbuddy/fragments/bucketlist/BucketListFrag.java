package ravtrix.backpackerbuddy.fragments.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.BucketComparator;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.bucketlist.BucketPostActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserBucketListSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.adapter.BucketListAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BucketListFrag extends Fragment implements View.OnClickListener {

    @BindView(R.id.frag_bucket_RecyclerView) protected RecyclerView bucketRecyclerView;
    @BindView(R.id.frag_bucket_noBucket) protected TextView tvNoBucket;
    @BindView(R.id.frag_bucket_progressBar) protected ProgressBar progressBar;
    @BindView(R.id.frag_bucket_txAddBucket) protected TextView tvAddBucket;
    private View view;
    private BucketListAdapter bucketListAdapter;
    private List<BucketListModel> bucketListModels;
    private RecyclerView.ItemDecoration dividerDecorator;
    private UserLocalStore userLocalStore;
    private static int REQUEST_CODE = 1;
    private static int RESULT_CODE = 1; // After new submission, edit

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.frag_bucket_list, container, false);
        ButterKnife.bind(this, view);
        Helpers.overrideFonts(getContext(), tvAddBucket);
        progressBar.setVisibility(View.VISIBLE);

        tvAddBucket.setOnClickListener(this);

        userLocalStore = new UserLocalStore(getContext());
        dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_inbox);

        fetchUserBucketList();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.frag_bucket_txAddBucket:
                startActivityForResult(new Intent(getContext(), BucketPostActivity.class), 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (requestCode == RESULT_CODE) {
                fetchUserBucketListRefresh(); // refresh from new bucket submission
            }
        }
    }

    private void fetchUserBucketList() {

        Call<List<BucketListModel>> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .fetchUserBucketList()
                .fetchUserBucketList(userLocalStore.getLoggedInUser().getUserID());

        retrofit.enqueue(new Callback<List<BucketListModel>>() {
            @Override
            public void onResponse(Call<List<BucketListModel>> call, Response<List<BucketListModel>> response) {

                progressBar.setVisibility(View.GONE);
                if (response.body().get(0).getSuccess() == 0) {
                    // Empty no list
                    bucketRecyclerView.setVisibility(View.GONE);
                    tvNoBucket.setVisibility(View.VISIBLE);
                } else {
                    bucketListModels = response.body();
                    Collections.sort(bucketListModels, new BucketComparator());

                    bucketListAdapter = new BucketListAdapter(getContext(), BucketListFrag.this, bucketListModels);
                    bucketRecyclerView.setAdapter(bucketListAdapter);
                    bucketRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    bucketRecyclerView.addItemDecoration(dividerDecorator);
                }
            }

            @Override
            public void onFailure(Call<List<BucketListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Helpers.displayToast(getContext(), "Error");
            }
        });
    }

    public void fetchUserBucketListRefresh() {

        Call<List<BucketListModel>> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .fetchUserBucketList()
                .fetchUserBucketList(userLocalStore.getLoggedInUser().getUserID());

        retrofit.enqueue(new Callback<List<BucketListModel>>() {
            @Override
            public void onResponse(Call<List<BucketListModel>> call, Response<List<BucketListModel>> response) {

                if (response.body().get(0).getSuccess() == 0) {
                    // Empty no list
                    bucketRecyclerView.setVisibility(View.GONE);
                    tvNoBucket.setVisibility(View.VISIBLE);
                } else {
                    bucketListModels = response.body();
                    Collections.sort(bucketListModels, new BucketComparator());

                    bucketListAdapter.swap(bucketListModels);
                }
            }
            @Override
            public void onFailure(Call<List<BucketListModel>> call, Throwable t) {
                Helpers.displayToast(getContext(), "Error");
            }
        });
    }
}
