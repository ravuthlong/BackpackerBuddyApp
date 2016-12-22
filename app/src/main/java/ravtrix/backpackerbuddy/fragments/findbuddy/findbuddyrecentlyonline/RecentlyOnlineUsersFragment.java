package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddyrecentlyonline;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.fragments.findbuddy.OnFinishedImageLoading;
import ravtrix.backpackerbuddy.fragments.findbuddy.findbuddyrecentlyonline.adapter.CustomGridViewOnline;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
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
    @BindView(R.id.frag_gridview_nearTxtOnline) protected TextView nearTxt;
    @BindView(R.id.frag_gridview_cityOnline) protected TextView city;
    private View view;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private UserLocalStore userLocalStore;
    private int currentSelectedDropdown;
    private CustomGridViewOnline customGridViewAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_gridview_online, container, false);

        ButterKnife.bind(this, view);

        nearTxt.setText("Recently");
        city.setText("Online");

        userLocalStore = new UserLocalStore(getActivity());
        fetchRecentlyOnlineUsers();

        return view;
    }



    private void fetchRecentlyOnlineUsers() {
        fragActivityProgressBarInterface.setProgressBarVisible();

        Call<List<UserLocationInfo>> retrofitCall = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .getRecentlyOnlineUsers()
                .getRecentlyOnlineUsers(userLocalStore.getLoggedInUser().getUserID());

        retrofitCall.enqueue(new Callback<List<UserLocationInfo>>() {
            @Override
            public void onResponse(Call<List<UserLocationInfo>> call, Response<List<UserLocationInfo>> response) {
                List<UserLocationInfo> userList = response.body();
                customGridViewAdapter = new CustomGridViewOnline(getActivity(), userList,
                        view, new OnFinishedImageLoading() {
                    @Override
                    public void onFinishedImageLoading() {
                        hideProgressbar();
                    }
                });
                profileImageGridView.setAdapter(customGridViewAdapter);
            }

            @Override
            public void onFailure(Call<List<UserLocationInfo>> call, Throwable t) {
                fragActivityProgressBarInterface.setProgressBarInvisible();
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setCurrentSelectedDropdown(int currentSelectedDropdown) {
        this.currentSelectedDropdown = currentSelectedDropdown;
    }

    public int getCurrentSelectedDropdown() {
        return this.currentSelectedDropdown;
    }

    private void hideProgressbar() {
        fragActivityProgressBarInterface.setProgressBarInvisible();
    }
}
