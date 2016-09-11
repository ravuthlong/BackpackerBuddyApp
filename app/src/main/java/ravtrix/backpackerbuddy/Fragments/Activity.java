package ravtrix.backpackerbuddy.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.interfaces.FragActivitySetDrawerInterface;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.adapter.FeedListAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.data.FeedItem;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.decorator.DividerDecoration;
import ravtrix.backpackerbuddy.retrofit.retrofitrequests.retrofitusercountriesrequests.RetrofitUserCountries;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 7/29/16.
 */
public class Activity extends Fragment implements  View.OnClickListener {

    @BindView(R.id.postRecyclerView1) protected RecyclerView recyclerView;
    @BindView(R.id.main_swipe)protected WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    @BindView(R.id.bFloatingActionButton) protected FloatingActionButton floatingActionButton;
    @BindView(R.id.spinner) protected ProgressBar spinner;

    private static final String TAG = Activity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private List<FeedItem> feedItems;
    private FeedListAdapter feedListAdapter;
    private FloatingActionButton postWidget;
    private UserLocalStore userLocalStore;
    private RetrofitUserCountries retrofitUserCountries;
    private FragActivitySetDrawerInterface fragActivitySetDrawerInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_usercountries, container, false);
        ButterKnife.bind(this, v);

        //RefWatcher refWatcher = UserMainPage.getRefWatcher(getActivity());
        //refWatcher.watch(this);

        waveSwipeRefreshLayout.setWaveColor(Color.GRAY);

        waveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                waveSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waveSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });

        userLocalStore = new UserLocalStore(getActivity());
        retrofitUserCountries = new RetrofitUserCountries();
        floatingActionButton.setOnClickListener(this);

        retrieveUserCountryPostsRetrofit();
        handleFloatingButtonScroll(this.floatingActionButton);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragActivitySetDrawerInterface = (FragActivitySetDrawerInterface) context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bFloatingActionButton:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new Destination()).commit();
                fragActivitySetDrawerInterface.setDrawerSelected(1);
                break;
            default:
        }
    }

    /*
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
    }*/

    // Hide floating action button on scroll down and show on scroll up
    private void handleFloatingButtonScroll(final FloatingActionButton floatingActionButton) {
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if ((dy > 0 || dy < 0) && floatingActionButton.isShown()) {
                    floatingActionButton.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    floatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    // Make retrofit call to retrieve user country list to populate the recycler view
    private void retrieveUserCountryPostsRetrofit() {

        spinner.setVisibility(View.VISIBLE);
        Call<List<FeedItem>> returnedFeed = retrofitUserCountries.getNotLoggedInCountryPosts().countryPosts();
        returnedFeed.enqueue(new Callback<List<FeedItem>>() {
            @Override
            public void onResponse(Call<List<FeedItem>> call, Response<List<FeedItem>> response) {
                spinner.setVisibility(View.GONE);

                // Set up array list of country feeds
                feedItems =  response.body();
                feedListAdapter = new FeedListAdapter(Activity.this, feedItems, userLocalStore.getLoggedInUser().getUserID());
                setRecyclerView(feedListAdapter);
            }

            @Override
            public void onFailure(Call<List<FeedItem>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void setRecyclerView(FeedListAdapter feedListAdapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity());
        recyclerView.addItemDecoration(dividerDecorator);
    }
}

