package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddyrecentlyonline;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.fragments.findbuddy.findbuddyrecentlyonline.adapter.CustomGridViewOnline;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.models.UserLocationInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/26/16.
 */

public class RecentlyOnlineUsersFragment extends Fragment {
    @BindView(R.id.grid_viewOnline) protected GridView profileImageGridView;
    @BindView(R.id.frag_gridview_recentlyOnline) protected TextView onlineTxt;
    @BindView(R.id.frag_gridview_online_progressbar) protected ProgressBar progressBar;
    private View view;
    private UserLocalStore userLocalStore;
    private int currentSelectedDropdown;
    private CustomGridViewOnline customGridViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_gridview_online, container, false);

        ButterKnife.bind(this, view);
        setTypeface();
        userLocalStore = new UserLocalStore(getActivity());

        fetchRecentlyOnlineUsers();
        return view;
    }


    /**
     * Fetch recently online user through retrofit
     */
    private void fetchRecentlyOnlineUsers() {
        progressBar.setVisibility(View.VISIBLE);
        profileImageGridView.setVisibility(View.INVISIBLE);
        Call<List<UserLocationInfo>> retrofitCall = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .getRecentlyOnlineUsers()
                .getRecentlyOnlineUsers(userLocalStore.getLoggedInUser().getUserID());

        retrofitCall.enqueue(new Callback<List<UserLocationInfo>>() {
            @Override
            public void onResponse(Call<List<UserLocationInfo>> call, Response<List<UserLocationInfo>> response) {
                List<UserLocationInfo> userList = response.body();
                customGridViewAdapter = new CustomGridViewOnline(getActivity(), userList);
                profileImageGridView.setAdapter(customGridViewAdapter);
                progressBar.setVisibility(View.INVISIBLE);
                profileImageGridView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<UserLocationInfo>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                profileImageGridView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setTypeface() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Text.ttf");
        onlineTxt.setTypeface(font);
    }

    public void setCurrentSelectedDropdown(int currentSelectedDropdown) {
        this.currentSelectedDropdown = currentSelectedDropdown;
    }

    public int getCurrentSelectedDropdown() {
        return this.currentSelectedDropdown;
    }
}
