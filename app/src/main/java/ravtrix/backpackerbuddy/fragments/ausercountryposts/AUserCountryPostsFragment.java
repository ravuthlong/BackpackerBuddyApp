package ravtrix.backpackerbuddy.fragments.ausercountryposts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.RetrofitUserCountrySingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.ausercountryrecyclerview.adapter.FeedListAdapterAUserPosts;
import ravtrix.backpackerbuddy.recyclerviewfeed.ausercountryrecyclerview.data.FeedItemAUserCountry;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 10/8/16.
 */

public class AUserCountryPostsFragment extends Fragment {

    @BindView(R.id.postRecyclerView1) protected RecyclerView recyclerView;
    private View view;
    private List<FeedItemAUserCountry> feedItemAUserCountries;
    private FeedListAdapterAUserPosts feedListAdapterAUserPosts;
    private UserLocalStore userLocalStore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_usercountries, container, false);
        //view.setVisibility(View.GONE);
        ButterKnife.bind(this, view);

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        recyclerView.addItemDecoration(dividerDecorator);

        userLocalStore = new UserLocalStore(getContext());
        retrieveAUserCountryPostsRetrofit();

        return view;
    }

    private void retrieveAUserCountryPostsRetrofit() {

        Call<List<FeedItemAUserCountry>> retrofitCall = RetrofitUserCountrySingleton.getRetrofitUserCountry()
                .getAUserCountryPosts().getAUserCountryPosts(userLocalStore.getLoggedInUser().getUserID());

        retrofitCall.enqueue(new Callback<List<FeedItemAUserCountry>>() {
            @Override
            public void onResponse(Call<List<FeedItemAUserCountry>> call, Response<List<FeedItemAUserCountry>> response) {
                if (response == null) {
                    System.out.println("YOU HAVE NO POST HAHA");

                } else {
                    feedItemAUserCountries = response.body();
                    feedListAdapterAUserPosts = new FeedListAdapterAUserPosts(AUserCountryPostsFragment.this, feedItemAUserCountries,
                            userLocalStore.getLoggedInUser().getUserID());
                    setRecyclerView(feedListAdapterAUserPosts);
                }
            }
            @Override
            public void onFailure(Call<List<FeedItemAUserCountry>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void setRecyclerView(FeedListAdapterAUserPosts feedListAdapterAUserPosts) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapterAUserPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}
