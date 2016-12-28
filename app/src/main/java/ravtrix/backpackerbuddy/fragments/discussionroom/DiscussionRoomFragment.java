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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.DiscussionPostActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.DiscussionAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.DiscussionModel;
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
                startActivity(new Intent(getContext(), DiscussionPostActivity.class));
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

                discussionAdapter = new DiscussionAdapter(DiscussionRoomFragment.this, discussionModels,
                        view, fragActivityProgressBarInterface, userLocalStore, swipeRefreshLayout);
                recyclerViewDiscussion.setAdapter(discussionAdapter);
                recyclerViewDiscussion.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
                fragActivityProgressBarInterface.setProgressBarInvisible();
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
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
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
