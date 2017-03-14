package ravtrix.backpackerbuddy.fragments.discussionroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.discussion.insertdiscussion.DiscussionPostActivity;
import ravtrix.backpackerbuddy.fcm.FirebaseMessagingService;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.HelpersDisplay;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.adapter.DiscussionAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data.DiscussionModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;

/**
 * Created by Ravinder on 12/21/16.
 */

public class DiscussionRoomFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        IDiscussionRoomFragView {

    @BindView(R.id.postRecyclerViewDiscussion) protected  RecyclerView recyclerViewDiscussion;
    @BindView(R.id.swipe_refresh_discussion) protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tvNoInfo_FragDiscussion) protected TextView tvNoInfo;
    @BindView(R.id.bFloatingActionButtonDiscussion) protected FloatingActionButton addPostButton;
    @BindView(R.id.frag_discussion_progressBar) protected ProgressBar progressBar;
    private DiscussionRoomFragPresenter discussionRoomFragPresenter;
    private View view;
    private DiscussionAdapter discussionAdapter;
    private List<DiscussionModel> discussionModels;
    private UserLocalStore userLocalStore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_discussion_room, container, false);
        ButterKnife.bind(this, view);

        swipeRefreshLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        // Cancel push notification if exists
        FirebaseMessagingService.cancelNotification(getActivity(), 1);

        // Adding decorator to recycler view
        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_inbox);
        recyclerViewDiscussion.addItemDecoration(dividerDecorator);

        handleFloatingButtonScroll(addPostButton); // hiding floating button on scroll

        userLocalStore = new UserLocalStore(getContext());
        discussionRoomFragPresenter = new DiscussionRoomFragPresenter(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        addPostButton.setOnClickListener(this);

        Helpers.checkLocationUpdate(getActivity(), userLocalStore); // check the need for location update
        discussionRoomFragPresenter.fetchDiscussionPosts(userLocalStore.getLoggedInUser().getUserID());

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.buttonFilter:
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Filter By Country");
                final String[] types = getResources().getStringArray(R.array.countriesShort);
                types[0] = "All"; // Change the first option from None to All because first option should show all country instead
                b.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (which != 0) {
                            retrofitFetchFilteredPosts(types[which]);
                        } else {
                            // fetch all instead, user chose "All" option
                            discussionRoomFragPresenter.fetchDiscussionPostsRefresh(userLocalStore.getLoggedInUser().getUserID());
                        }
                    }
                });
                b.setCancelable(true);
                b.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bFloatingActionButtonDiscussion:
                if (userLocalStore.getLoggedInUser().getUserID() != 0) {
                    startActivity(new Intent(getContext(), DiscussionPostActivity.class));
                } else {
                    Helpers.displayToast(getContext(), "Become a member to start a discussion");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        // refresh fragment
       discussionRoomFragPresenter.fetchDiscussionPostsRefresh(userLocalStore.getLoggedInUser().getUserID());
    }

    public void refresh() {
        discussionRoomFragPresenter.fetchDiscussionPostsRefresh(userLocalStore.getLoggedInUser().getUserID());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) { // refresh
                refresh();
            }
        }
    }

    public void retrofitFetchFilteredPosts(String countryTag) {
        HashMap<String, String> postHash = new HashMap<>();
        postHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        postHash.put("tag", countryTag);

        discussionRoomFragPresenter.fetchDiscussionPostsFilter(postHash);
    }


    // Hide floating action button on scroll down and show on scroll up
    private void handleFloatingButtonScroll(final FloatingActionButton floatingActionButton) {
        this.recyclerViewDiscussion.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void setModelColors(List<DiscussionModel> discussionModels) {
        // Generate random text color for country tag
        if (getActivity() != null) {
            for (int i = 0; i < discussionModels.size(); i++) {
                discussionModels.get(i).setTagColor(ContextCompat.getColor(getActivity(), R.color.tagColor));
            }
        }
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(getContext());
    }

    @Override
    public void swapData(List<DiscussionModel> discussionModels) {
        setModelColors(discussionModels);
        discussionAdapter.swap(discussionModels);
    }

    @Override
    public void setDiscussionModels(List<DiscussionModel> discussionModels) {
        setModelColors(discussionModels);
        this.discussionModels = discussionModels;
    }

    @Override
    public void setDiscussionModelsEmpty() {
        this.discussionModels = new ArrayList<>(); //empty
    }

    @Override
    public void setRecyclerView() {
        discussionAdapter = new DiscussionAdapter(DiscussionRoomFragment.this, discussionModels, userLocalStore);
        recyclerViewDiscussion.setAdapter(discussionAdapter);
        recyclerViewDiscussion.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void hideSwipeLayout() {
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void showSwipeLayout() {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopSwipeRefreshing() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void displayNoResultSnack() {
        HelpersDisplay.makeSnackbar(recyclerViewDiscussion, "No results matching search");
    }
}
