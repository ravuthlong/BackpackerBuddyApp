package ravtrix.backpackerbuddy.fragments.mainfrag.countrybytime;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.fragments.destination.DestinationFragment;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserCountrySingleton;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.interfacescom.FragActivityResetDrawer;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.adapter.FeedListAdapterMain;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.data.FeedItem;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 7/29/16.
 */
public class CountryRecentFragment extends Fragment implements  View.OnClickListener, FeedListAdapterMain.OnLoadMoreListener {

    @BindView(R.id.postRecyclerView1) protected RecyclerView recyclerView;
    @BindView(R.id.main_swipe)protected WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    @BindView(R.id.bFloatingActionButton) protected FloatingActionButton floatingActionButton;
    @BindView(R.id.tvNoInfo_FragUserCountries) protected TextView tvNoResult;
    private static final String TAG = CountryRecentFragment.class.getSimpleName();
    private List<FeedItem> feedItems;
    private List<FeedItem> feedTen;
    private FeedListAdapterMain feedListAdapter;
    private FloatingActionButton postWidget;
    private UserLocalStore userLocalStore;
    private FragActivityResetDrawer fragActivityResetDrawer;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private View view;
    private Bundle receivedQueryBundle;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragActivityResetDrawer = (FragActivityResetDrawer) context;
        this.fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_usercountries_recent, container, false);
        view.setVisibility(View.INVISIBLE);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        feedListAdapter = null;
        userLocalStore = new UserLocalStore(getActivity());
        feedItems = new ArrayList<>();
        feedTen = new ArrayList<>();
        //RefWatcher refWatcher = UserMainPage.getRefWatcher(getActivity());
        //refWatcher.watch(this);

        fragActivityProgressBarInterface.setProgressBarVisible();
        waveSwipeRefreshLayout.setWaveColor(Color.GRAY);
        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        recyclerView.addItemDecoration(dividerDecorator);
        setRefreshListener();
        this.linearLayoutManager = new LinearLayoutManager(getActivity());
        floatingActionButton.setOnClickListener(this);

        long currentTime = System.currentTimeMillis();
        // If it's been a minute since last location update, do the update
        if (Helpers.timeDifInMinutes(currentTime,
                userLocalStore.getLoggedInUser().getTime()) > 1) {
            Helpers.updateLocationAndTime(getContext(), userLocalStore, currentTime);
        }

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.tbRecentPosts:
                this.waveSwipeRefreshLayout.setVisibility(View.VISIBLE);
                this.tvNoResult.setVisibility(View.INVISIBLE);
                // Refresh recycler view for recent posts
                //feedListAdapter.resetAll();
                //retrieveUserCountryPostsRetrofit();
                startActivity(new Intent(getContext(), UserMainPage.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bFloatingActionButton:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new DestinationFragment()).commit();
                fragActivityResetDrawer.onResetDrawer();
                break;
            default:
        }
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
        Call<List<FeedItem>> filterRetrofit = RetrofitUserCountrySingleton.getRetrofitUserCountry()
                .getFilteredPosts()
                .getFilterdPosts(month, userLocalStore.getLoggedInUser().getUserID(), country);

        filterRetrofit.enqueue(new Callback<List<FeedItem>>() {
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

    // Make retrofit call to retrieve user country posts to populate recycler view
    private void retrieveUserCountryPostsRetrofit() {

        Call<List<FeedItem>> returnedFeed = RetrofitUserCountrySingleton.getRetrofitUserCountry()
                                            .getNotLoggedInCountryPosts()
                                            .countryPosts(
                                                    userLocalStore.getLoggedInUser().getUserID());
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
            this.waveSwipeRefreshLayout.setVisibility(View.INVISIBLE);
            this.tvNoResult.setVisibility(View.VISIBLE);
            //System.out.println(" NULL FEED ");
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
        fragActivityProgressBarInterface.setProgressBarInvisible();
        view.setVisibility(View.VISIBLE);
        waveSwipeRefreshLayout.setRefreshing(false);
    }

    private void showErrorMessage() {
        Helpers.displayToast(getContext(), "Error");
        fragActivityProgressBarInterface.setProgressBarInvisible();
        view.setVisibility(View.VISIBLE);
        waveSwipeRefreshLayout.setRefreshing(false);
    }

    private void setRecyclerView(FeedListAdapterMain feedListAdapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapter);
        recyclerView.setLayoutManager(this.linearLayoutManager);
    }

    @Override
    public void onPause() {
        super.onPause();

        //this.feedListAdapter

        // Remove cache from Picasso so next time the page reloads the image and not use cache
        // Cache was used for recycler view scrolling only.
        if (feedItems != null) {
            for (int i = 0; i < feedItems.size(); i++) {
                Picasso.with(getContext()).invalidate("http://backpackerbuddy.net23.net/profile_pic/" +
                        feedItems.get(i).getUserID() + ".JPG");
            }
        }
    }

    private void setRefreshListener() {
        // Listens for refresh
        waveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                feedListAdapter.resetAll();
                retrieveUserCountryPostsRetrofit();
            }
        });
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
}

