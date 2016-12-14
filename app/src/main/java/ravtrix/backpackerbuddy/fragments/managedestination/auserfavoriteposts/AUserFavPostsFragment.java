package ravtrix.backpackerbuddy.fragments.managedestination.auserfavoriteposts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.RetrofitUserCountrySingleton;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.auserfavrecyclerview.adapter.FeedListAdapterUserFav;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.data.FeedItem;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 12/13/16.
 */

public class AUserFavPostsFragment extends Fragment {
    @BindView(R.id.recyclerView_userFav) protected RecyclerView recyclerView;
    @BindView(R.id.tvNoInfo_FragUserFav) protected TextView noInfoTv;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private List<FeedItem> feedItemAUserFavs;
    private FeedListAdapterUserFav feedListAdapterUserFav;
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

        v = inflater.inflate(R.layout.frag_user_favorites, container, false);
        v.setVisibility(View.GONE);

        ButterKnife.bind(this, v);
        fragActivityProgressBarInterface.setProgressBarVisible();
        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        recyclerView.addItemDecoration(dividerDecorator);

        userLocalStore = new UserLocalStore(getContext());
        retrieveAUserFavPostsRetrofit();
        return v;
    }

    /**
     * Fetch for favorite posts
     */
    private void retrieveAUserFavPostsRetrofit() {
        Call<List<FeedItem>> retrofitCall = RetrofitUserCountrySingleton.getRetrofitUserCountry()
                .getAUserFavPosts()
                .getAUserFavPosts(userLocalStore.getLoggedInUser().getUserID());

        retrofitCall.enqueue(new Callback<List<FeedItem>>() {
            @Override
            public void onResponse(Call<List<FeedItem>> call, Response<List<FeedItem>> response) {
                v.setVisibility(View.VISIBLE);

                if (response.body().get(0).isSuccess() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noInfoTv.setVisibility(View.VISIBLE);

                } else {
                    feedItemAUserFavs = response.body();
                    feedListAdapterUserFav = new FeedListAdapterUserFav(AUserFavPostsFragment.this,
                            feedItemAUserFavs, userLocalStore.getLoggedInUser().getUserID());
                    setRecyclerView(feedListAdapterUserFav);
                }
                fragActivityProgressBarInterface.setProgressBarInvisible();
            }

            @Override
            public void onFailure(Call<List<FeedItem>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });



    }

    private void setRecyclerView(FeedListAdapterUserFav feedListAdapterUserFav) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapterUserFav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
