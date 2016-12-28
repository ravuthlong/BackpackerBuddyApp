package ravtrix.backpackerbuddy.fragments.managedestination.auserdiscussionposts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
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
 * Created by Ravinder on 12/13/16.
 */

public class AUserDisPostsFragment extends Fragment {
    @BindView(R.id.recyclerView_userFav) protected RecyclerView recyclerView;
    @BindView(R.id.tvNoInfo_FragUserFav) protected TextView noInfoTv;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private List<DiscussionModel> discussionModels;
    private DiscussionAdapter discussionAdapter;
    private UserLocalStore userLocalStore;
    private View v;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_user_discussions, container, false);
        v.setVisibility(View.GONE);

        ButterKnife.bind(this, v);
        fragActivityProgressBarInterface.setProgressBarVisible();
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
                v.setVisibility(View.VISIBLE);

                if (response.body().get(0).getSuccess() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noInfoTv.setVisibility(View.VISIBLE);
                } else {
                    discussionModels = response.body();
                    discussionAdapter = new DiscussionAdapter(AUserDisPostsFragment.this, discussionModels,
                            v, fragActivityProgressBarInterface, userLocalStore, null);
                    setRecyclerView(discussionAdapter);
                }
                fragActivityProgressBarInterface.setProgressBarInvisible();
            }

            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
                fragActivityProgressBarInterface.setProgressBarInvisible();
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
                    discussionAdapter.swap(discussionModels);
                }
            }

            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView(DiscussionAdapter feedListAdapterUserFav) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapterUserFav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
