package ravtrix.backpackerbuddy.fragments.userdestinationfrag.countrybytime;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.createdestination.DestinationActivity;
import ravtrix.backpackerbuddy.fragments.userdestinationfrag.CountryTabFragment;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserCountrySingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.adapter.FeedListAdapterMain;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.data.FeedItem;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 7/29/16.
 */
public class CountryRecentFragment extends Fragment implements  View.OnClickListener,
        FeedListAdapterMain.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.postRecyclerView1) protected RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_country)protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.bFloatingActionButton) protected FloatingActionButton floatingActionButton;
    @BindView(R.id.tvNoInfo_FragUserCountries) protected TextView tvNoResult;
    @BindView(R.id.frag_usercountries_progressBar) protected ProgressBar progressBar;
    private List<FeedItem> feedItems;
    private List<FeedItem> feedTen;
    private FeedListAdapterMain feedListAdapter;
    private UserLocalStore userLocalStore;
    private View view;
    private Bundle receivedQueryBundle;
    private LinearLayoutManager linearLayoutManager;
    private MenuItem bRecentPostsItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_usercountries_recent, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        feedListAdapter = null;
        userLocalStore = new UserLocalStore(getActivity());
        Helpers.checkLocationUpdate(getActivity(), userLocalStore);
        feedItems = new ArrayList<>();
        feedTen = new ArrayList<>();

        Helpers.overrideFonts(getContext(), tvNoResult);

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        recyclerView.addItemDecoration(dividerDecorator);
        swipeRefreshLayout.setOnRefreshListener(this);

        this.linearLayoutManager = new LinearLayoutManager(getActivity());
        floatingActionButton.setOnClickListener(this);

        if (receivedQueryBundle != null) {
            // Retrieve with filter
            retrieveUserCountryFilteredPostsRetrofit(receivedQueryBundle.getInt("month"),
                    receivedQueryBundle.getString("country"));
        } else {
            // Retrieve without filter (Newest posts)
            retrieveUserCountryPostsRetrofit();
        }
        handleFloatingButtonScroll(this.floatingActionButton);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_recent_posts, menu);

        bRecentPostsItem = menu.findItem(R.id.tbRecentPosts);

        // only show toolbar item if the user submits a filter
        if (receivedQueryBundle != null) {
            this.bRecentPostsItem.setVisible(true);
        } else {
            bRecentPostsItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.tbRecentPosts:
                this.tvNoResult.setVisibility(View.INVISIBLE);
                // refresh this fragment
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new CountryTabFragment()).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bFloatingActionButton:

                if (userLocalStore.getLoggedInUser().getUserID() != 0) {
                    CountryTabFragment parentTab = (CountryTabFragment) getParentFragment();
                    // Request code = 1 for editing
                    parentTab.startActivityForResult(new Intent(getContext(), DestinationActivity.class), 1);
                } else {
                    Helpers.displayToast(getContext(), "Become a member to add a destination");
                }
                break;
            default:
        }
    }

    @Override
    public void onRefresh() {
        feedListAdapter.resetAll();
        retrieveUserCountryPostsRetrofit();
    }

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

    // Make retrofit to retrieve user country posts with filter option to populate recycler view
    private void retrieveUserCountryFilteredPostsRetrofit(int month, String country) {

        swipeRefreshLayout.setEnabled(false);
        Call<List<FeedItem>> filterRetrofit = RetrofitUserCountrySingleton.getRetrofitUserCountry()
                .getFilteredPosts()
                .getFilterdPosts(month, country);

        filterRetrofit.enqueue(new Callback<List<FeedItem>>() {
            @Override
            public void onResponse(Call<List<FeedItem>> call, Response<List<FeedItem>> response) {
                setRespondFeed(response);
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
            @Override
            public void onFailure(Call<List<FeedItem>> call, Throwable t) {
                showErrorMessage();
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    // Make retrofit call to retrieve user country posts to populate recycler view
    private void retrieveUserCountryPostsRetrofit() {

        swipeRefreshLayout.setEnabled(true);

        Call<List<FeedItem>> returnedFeed = RetrofitUserCountrySingleton.getRetrofitUserCountry()
                                            .getNotLoggedInCountryPosts()
                                            .countryPosts();
        returnedFeed.enqueue(new Callback<List<FeedItem>>() {
            @Override
            public void onResponse(Call<List<FeedItem>> call, Response<List<FeedItem>> response) {
                setRespondFeed(response);
            }
            @Override
            public void onFailure(Call<List<FeedItem>> call, Throwable t) {
                showErrorMessage();
            }
        });
    }

    // Set up recycler view when the feed is received from Retrofit
    private void setRespondFeed( Response<List<FeedItem>> response) {
        // Set up array list of country feeds
        feedTen.clear();
        feedItems = response.body();
        int loop = 0;

        if (feedItems == null) {
            this.swipeRefreshLayout.setVisibility(View.INVISIBLE);
            this.tvNoResult.setVisibility(View.VISIBLE);
        } else if (feedItems.size() < 11) {
            loop = feedItems.size();
        } else {
            loop = 10;
        }

        for (int i = 0; i < loop; i++) {
            feedTen.add(i, feedItems.get(i));
        }

        feedListAdapter = new FeedListAdapterMain(CountryRecentFragment.this,
                userLocalStore.getLoggedInUser().getUserID(), CountryRecentFragment.this);

        feedListAdapter.setLinearLayoutManager(linearLayoutManager);
        feedListAdapter.addAll(feedTen);
        feedListAdapter.setRecyclerView(recyclerView);

        setRecyclerView(feedListAdapter);

        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        Helpers.displayErrorToast(getContext());
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setRecyclerView(FeedListAdapterMain feedListAdapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapter);
        recyclerView.setLayoutManager(this.linearLayoutManager);
    }


    public void refreshPage() {
        feedListAdapter.resetAll();
        retrieveUserCountryPostsRetrofit();
    }

    @Override
    public void onLoadMore() {

        // Load 10 more posts when the user reaches the bottom of the page
        feedListAdapter.setProgressMore(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                feedTen.clear();
                feedListAdapter.setProgressMore(false);
                int start = feedListAdapter.getItemCount();
                int end = start + 10; // ten more added

                for (int i = start; i < end; i++) {
                    if (i < feedItems.size()) {
                        feedTen.add(feedItems.get(i));
                    } else {
                        break;
                    }
                }
                feedListAdapter.addItemMore(feedTen);
                feedListAdapter.setMoreLoading(false);
            }
        }, 1000);
    }

    // Set bundle received from filter fragment
    public void setReceivedQueryBundle(Bundle receivedQueryBundle) {
        this.receivedQueryBundle = receivedQueryBundle;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) { // refresh
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new CountryRecentFragment()).commit();
            }
        }
    }
}

