package ravtrix.backpackerbuddy.fragments.discussionroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.discussion.DiscussionPostActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.adapter.DiscussionAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data.DiscussionModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 12/21/16.
 */

public class DiscussionRoomFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.postRecyclerViewDiscussion) protected  RecyclerView recyclerViewDiscussion;
    @BindView(R.id.swipe_refresh_discussion) protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tvNoInfo_FragDiscussion) protected TextView tvNoInfo;
    @BindView(R.id.bFloatingActionButtonDiscussion) protected FloatingActionButton addPostButton;
    private View view;
    private DiscussionAdapter discussionAdapter;
    private List<DiscussionModel> discussionModels;
    private UserLocalStore userLocalStore;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_discussion_room, container, false);

        ButterKnife.bind(this, view);
        view.setVisibility(View.INVISIBLE);

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_inbox);
        recyclerViewDiscussion.addItemDecoration(dividerDecorator);
        handleFloatingButtonScroll(addPostButton);

        userLocalStore = new UserLocalStore(getContext());
        swipeRefreshLayout.setOnRefreshListener(this);
        addPostButton.setOnClickListener(this);

        Helpers.checkLocationUpdate(getContext(), userLocalStore);
        fragActivityProgressBarInterface.setProgressBarVisible();
        fetchDiscussionPosts();

        return view;
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
        fetchDiscussionPostsRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) { // refresh
                refresh();
            }
        }
    }

    public void refresh() {
        fetchDiscussionPostsRefresh();
    }

    private void fetchDiscussionPosts() {

        Call<List <DiscussionModel>> retrofitCall = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getDiscussions()
                .getDiscussions(userLocalStore.getLoggedInUser().getUserID());

        retrofitCall.enqueue(new Callback<List<DiscussionModel>>() {
            @Override
            public void onResponse(Call<List<DiscussionModel>> call, Response<List<DiscussionModel>> response) {

                if (response.body().get(0).getSuccess() == 1) {
                    discussionModels = response.body(); // GSON convert json into models
                } else {
                    fragActivityProgressBarInterface.setProgressBarInvisible();
                    view.setVisibility(View.VISIBLE);
                    discussionModels = new ArrayList<>(); //empty
                }

                discussionAdapter = new DiscussionAdapter(DiscussionRoomFragment.this, discussionModels, userLocalStore);
                recyclerViewDiscussion.setAdapter(discussionAdapter);
                recyclerViewDiscussion.setLayoutManager(new LinearLayoutManager(getActivity()));
                displayAfterLoading();
            }
            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
                displayAfterLoading();
                Helpers.displayErrorToast(getContext());
            }
        });
    }

    private void fetchDiscussionPostsRefresh() {
        Call<List <DiscussionModel>> retrofitCall = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getDiscussions()
                .getDiscussions(userLocalStore.getLoggedInUser().getUserID());

        retrofitCall.enqueue(new Callback<List<DiscussionModel>>() {
            @Override
            public void onResponse(Call<List<DiscussionModel>> call, Response<List<DiscussionModel>> response) {

                if (response.body().get(0).getSuccess() == 1) {
                    discussionModels = response.body(); // GSON convert json into models
                } else {
                    discussionModels = new ArrayList<>(); //empty
                }
                discussionAdapter.swap(discussionModels);
                displayAfterLoading();
            }
            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
                Helpers.displayErrorToast(getContext());
                displayAfterLoading();
            }
        });
    }

    private void displayAfterLoading() {
        // stop progress bar
        fragActivityProgressBarInterface.setProgressBarInvisible();
        view.setVisibility(View.VISIBLE);

        // stop refreshing layout is it is running
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
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

}
