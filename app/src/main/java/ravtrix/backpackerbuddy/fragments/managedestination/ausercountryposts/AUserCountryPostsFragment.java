package ravtrix.backpackerbuddy.fragments.managedestination.ausercountryposts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import ravtrix.backpackerbuddy.helpers.RetrofitUserCountrySingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.ausercountryrecyclerview.adapter.FeedListAdapterAUserPosts;
import ravtrix.backpackerbuddy.recyclerviewfeed.ausercountryrecyclerview.data.FeedItemAUserCountry;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 10/8/16.
 */

public class AUserCountryPostsFragment extends Fragment {

    @BindView(R.id.recyclerView_userPost) protected RecyclerView recyclerView;
    @BindView(R.id.tvNoInfo_FragUserPost) protected TextView noInfoTv;
    @BindView(R.id.frag_a_user_posts_progressBar) protected ProgressBar progressBar;
    private View view;
    private List<FeedItemAUserCountry> feedItemAUserCountries;
    private FeedListAdapterAUserPosts feedListAdapterAUserPosts;
    private UserLocalStore userLocalStore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_user_posts, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Helpers.overrideFonts(getContext(), noInfoTv);

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        recyclerView.addItemDecoration(dividerDecorator);

        userLocalStore = new UserLocalStore(getContext());
        retrieveAUserCountryPostsRetrofit();

        return view;
    }

    public void refresh() {
        retrieveAUserCountryPostsRetrofit();
    }

    private void retrieveAUserCountryPostsRetrofit() {

        Call<List<FeedItemAUserCountry>> retrofitCall = RetrofitUserCountrySingleton.getRetrofitUserCountry()
                .getAUserCountryPosts().getAUserCountryPosts(userLocalStore.getLoggedInUser().getUserID());

        retrofitCall.enqueue(new Callback<List<FeedItemAUserCountry>>() {
            @Override
            public void onResponse(Call<List<FeedItemAUserCountry>> call, Response<List<FeedItemAUserCountry>> response) {

                recyclerView.setVisibility(View.VISIBLE);
                if (response.body().get(0).isSuccess() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noInfoTv.setVisibility(View.VISIBLE);

                } else {
                    feedItemAUserCountries = response.body();
                    feedListAdapterAUserPosts = new FeedListAdapterAUserPosts(AUserCountryPostsFragment.this, feedItemAUserCountries);
                    setRecyclerView(feedListAdapterAUserPosts);
                }
                progressBar.setVisibility(View.INVISIBLE);
                //fragActivityProgressBarInterface.setProgressBarInvisible();
            }
            @Override
            public void onFailure(Call<List<FeedItemAUserCountry>> call, Throwable t) {
                System.out.println(t.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setRecyclerView(FeedListAdapterAUserPosts feedListAdapterAUserPosts) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapterAUserPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
