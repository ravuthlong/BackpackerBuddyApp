package ravtrix.backpackerbuddy.fragments.mainfrag.countrybytime;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import ravtrix.backpackerbuddy.R;
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
    private static final String TAG = CountryRecentFragment.class.getSimpleName();
    private ProgressDialog progressDialog;
    private List<FeedItem> feedItems;
    private List<FeedItem> feedTen;
    private FeedListAdapterMain feedListAdapter;
    private FloatingActionButton postWidget;
    private UserLocalStore userLocalStore;
    private FragActivityResetDrawer fragActivityResetDrawer;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private View view;
    private long currentTime;
    private int feedSize = 0;
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
        view = inflater.inflate(R.layout.frag_usercountries, container, false);
        view.setVisibility(View.INVISIBLE);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        feedItems = new ArrayList<>();
        feedTen = new ArrayList<>();
        //RefWatcher refWatcher = UserMainPage.getRefWatcher(getActivity());
        //refWatcher.watch(this);

        fragActivityProgressBarInterface.setProgressBarVisible();
        waveSwipeRefreshLayout.setWaveColor(Color.GRAY);
        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        recyclerView.addItemDecoration(dividerDecorator);
        setRefreshListener();
        userLocalStore = new UserLocalStore(getContext());
        this.linearLayoutManager = new LinearLayoutManager(getActivity());
        floatingActionButton.setOnClickListener(this);

        currentTime = System.currentTimeMillis();
        // If it's been a minute since last location update, do the update
        if (Helpers.timeDifInMinutes(currentTime,
                userLocalStore.getLoggedInUser().getTime()) > 1) {
            Helpers.updateLocationAndTime(getContext(), userLocalStore, currentTime);
        }
        retrieveUserCountryPostsRetrofit();
        handleFloatingButtonScroll(this.floatingActionButton);
        return view;
    }

    // Remove distance spinner when user changes fragment
    @Override
    public void onStop() {
        super.onStop();
        //dropdownToolbar.removeView(distance_spinner);
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

    // Make retrofit call to retrieve user country list to populate the recycler view
    private void retrieveUserCountryPostsRetrofit() {

        Call<List<FeedItem>> returnedFeed = RetrofitUserCountrySingleton.getRetrofitUserCountry()
                                            .getNotLoggedInCountryPosts()
                                            .countryPosts(userLocalStore.getLoggedInUser().getUserID());
        returnedFeed.enqueue(new Callback<List<FeedItem>>() {
            @Override
            public void onResponse(Call<List<FeedItem>> call, Response<List<FeedItem>> response) {

                // Set up array list of country feeds
                feedItems =  response.body();

                for (int i = 0; i < 10; i++) {
                    feedTen.add(i, feedItems.get(i));
                }

                feedListAdapter = new FeedListAdapterMain(CountryRecentFragment.this,
                        userLocalStore.getLoggedInUser().getUserID(), CountryRecentFragment.this);
                feedListAdapter.setLinearLayoutManager(linearLayoutManager);
                feedListAdapter.setRecyclerView(recyclerView);
                feedListAdapter.addAll(feedTen);

                setRecyclerView(feedListAdapter);
                fragActivityProgressBarInterface.setProgressBarInvisible();
                view.setVisibility(View.VISIBLE);
                waveSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<FeedItem>> call, Throwable t) {
                System.out.println(t.getMessage());
                fragActivityProgressBarInterface.setProgressBarInvisible();
                view.setVisibility(View.VISIBLE);
                waveSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setRecyclerView(FeedListAdapterMain feedListAdapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapter);
        recyclerView.setLayoutManager(this.linearLayoutManager);


    }

    @Override
    public void onPause() {
        super.onPause();

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
                retrieveUserCountryPostsRetrofit();
            }
        });
    }

    @Override
    public void onLoadMore() {

        feedListAdapter.setProgressMore(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                feedTen.clear();
                feedListAdapter.setProgressMore(false);
                int start = feedListAdapter.getItemCount();
                int end = start + 10; // ten more added

                for (int i = start + 1; i < end; i++) {
                    if (i < feedItems.size()) {
                        feedTen.add(feedItems.get(i));
                    } else {
                        break;
                    }
                }
                feedListAdapter.addItemMore(feedTen);
                feedListAdapter.setMoreLoading(false);
            }
        },2000);
    }
}

