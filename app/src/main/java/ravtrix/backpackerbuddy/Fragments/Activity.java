package ravtrix.backpackerbuddy.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Cache;

import java.util.List;

import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.adapter.FeedListAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.data.FeedItem;
import ravtrix.backpackerbuddy.retrofit.retrofitrequests.retrofitusercountriesrequests.RetrofitUserCountries;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 7/29/16.
 */
public class Activity extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = Activity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private List<FeedItem> feedItems;
    private FeedListAdapter feedListAdapter;
    private FloatingActionButton postWidget;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar spinner;
    private Cache.Entry entry;
    private UserLocalStore userLocalStore;
    private FragmentManager fragmentManager;
    private RetrofitUserCountries retrofitUserCountries;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_usercountries, container, false);

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshLayout1);
       // spinner = (ProgressBar) v.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) v.findViewById(R.id.postRecyclerView1);

        refreshLayout.setOnRefreshListener(this);
        userLocalStore = new UserLocalStore(getActivity());

        retrofitUserCountries = new RetrofitUserCountries();

        Call<List<FeedItem>> returnedFeed = retrofitUserCountries.getNotLoggedInCountryPosts().countryPosts();

        returnedFeed.enqueue(new Callback<List<FeedItem>>() {
            @Override
            public void onResponse(Call<List<FeedItem>> call, Response<List<FeedItem>> response) {

                // Set up array list of country feeds
                feedItems =  response.body();
                feedListAdapter = new FeedListAdapter(getActivity(), feedItems);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(feedListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onFailure(Call<List<FeedItem>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
        return v;
    }

    @Override
    public void onRefresh() {

        Call<List<FeedItem>> returnedFeed = retrofitUserCountries.getNotLoggedInCountryPosts().countryPosts();

        returnedFeed.enqueue(new Callback<List<FeedItem>>() {
            @Override
            public void onResponse(Call<List<FeedItem>> call, Response<List<FeedItem>> response) {
                feedItems = response.body();
                feedListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<FeedItem>> call, Throwable t) {
                System.out.println(t.getMessage());

            }
        });

        refreshLayout.setRefreshing(false);
        /*
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(Activity.this).attach(Activity.this).commit();
        feedItems.clear();*/
    }
}
