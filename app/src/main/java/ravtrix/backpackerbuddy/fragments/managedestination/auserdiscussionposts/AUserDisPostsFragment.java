package ravtrix.backpackerbuddy.fragments.managedestination.auserdiscussionposts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.adapter.DiscussionAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data.DiscussionModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 12/13/16.
 */

public class AUserDisPostsFragment extends Fragment {
    @BindView(R.id.recyclerView_userFav) protected RecyclerView recyclerView;
    @BindView(R.id.tvNoInfo_FragUserFav) protected TextView noInfoTv;
    @BindView(R.id.frag_a_user_discussion_progressBar) protected ProgressBar progressBar;
    private List<DiscussionModel> discussionModels;
    private DiscussionAdapter discussionAdapter;
    private UserLocalStore userLocalStore;
    private View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_user_discussions, container, false);
        ButterKnife.bind(this, v);

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Helpers.overrideFonts(getContext(), noInfoTv);

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_inbox);
        recyclerView.addItemDecoration(dividerDecorator);

        userLocalStore = new UserLocalStore(getContext());
        retrieveAUserDisPostsRetrofit();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) { // refresh
                refresh();
            }
        }
    }

    /**
     * Fetch for favorite posts
     */
    private void retrieveAUserDisPostsRetrofit() {

        Call<List<DiscussionModel>> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getAUserDiscussions()
                .getAUserDiscussions(userLocalStore.getLoggedInUser().getUserID());

        retrofit.enqueue(new Callback<List<DiscussionModel>>() {
            @Override
            public void onResponse(Call<List<DiscussionModel>> call, Response<List<DiscussionModel>> response) {
                recyclerView.setVisibility(View.VISIBLE);

                if (response.body().get(0).getSuccess() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noInfoTv.setVisibility(View.VISIBLE);
                } else {
                    discussionModels = response.body();
                    setModelColors(discussionModels);
                    discussionAdapter = new DiscussionAdapter(AUserDisPostsFragment.this, discussionModels,
                            userLocalStore);
                    setRecyclerView(discussionAdapter);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Helpers.displayErrorToast(getContext());
            }
        });
    }

    public void refresh() {
        retrieveAUserDisPostsRetrofitRefresh();
    }

    private void retrieveAUserDisPostsRetrofitRefresh() {

        Call<List<DiscussionModel>> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getAUserDiscussions()
                .getAUserDiscussions(userLocalStore.getLoggedInUser().getUserID());

        retrofit.enqueue(new Callback<List<DiscussionModel>>() {
            @Override
            public void onResponse(Call<List<DiscussionModel>> call, Response<List<DiscussionModel>> response) {

                if (response.body().get(0).getSuccess() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noInfoTv.setVisibility(View.VISIBLE);
                } else {
                    discussionModels = response.body();
                    setModelColors(discussionModels);
                    discussionAdapter.swap(discussionModels);
                }
            }

            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
                Helpers.displayErrorToast(getContext());
            }
        });
    }

    private void setRecyclerView(DiscussionAdapter feedListAdapterUserFav) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapterUserFav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setModelColors(List<DiscussionModel> discussionModels) {
        // Generate random text color for country tag
        for (int i = 0; i < discussionModels.size(); i++) {
            discussionModels.get(i).setTagColor(ContextCompat.getColor(getContext(), R.color.tagColor));
        }
    }
}
