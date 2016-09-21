package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

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
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.models.UserLocationInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/12/16.
 */
public class FindBuddyNearFragment extends Fragment {

    @BindView(R.id.grid_view) protected GridView profileImageGridView;
    @BindView(R.id.frag_gridview_city) protected TextView city;
    private View view;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private UserLocalStore userLocalStore;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_gridview, container, false);
        view.setVisibility(View.INVISIBLE);
        ButterKnife.bind(this, view);
        //linearLayout.setVisibility(View.INVISIBLE);
        fragActivityProgressBarInterface.setProgressBarVisible();
        userLocalStore = new UserLocalStore(getActivity());

        checkLocationUpdate();
        fetchNearbyUsers();
        return view;
    }

    private void fetchNearbyUsers() {
        Call<List<UserLocationInfo>> retrofitCall = RetrofitUserInfoSingleton.getRetrofitUserInfo ()
                .getNearbyUsers()
                .getNearbyUsers(userLocalStore.getLoggedInUser().getUserID(), 40);
        retrofitCall.enqueue(new Callback<List<UserLocationInfo>>() {
            @Override
            public void onResponse(Call<List<UserLocationInfo>> call, Response<List<UserLocationInfo>> response) {
                List<UserLocationInfo> userList = response.body();

                CustomGridView customGridViewAdapter = new CustomGridView(getActivity(), userList,
                        view, fragActivityProgressBarInterface);
                profileImageGridView.setAdapter(customGridViewAdapter);

                city.setText(Helpers.cityGeocoder(getContext(), userLocalStore.getLoggedInUser().getLatitude(),
                        userLocalStore.getLoggedInUser().getLongitude()));
            }

            @Override
            public void onFailure(Call<List<UserLocationInfo>> call, Throwable t) {
                fragActivityProgressBarInterface.setProgressBarInvisible();
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    /*
     * Check if location update is needed. If needed, update local store and server
     */
    private void checkLocationUpdate() {
        long currentTime = System.currentTimeMillis();

        // If it's been a minute since last location update, do the update
        if (Helpers.timeDifInMinutes(currentTime,
                userLocalStore.getLoggedInUser().getTime()) > 1) {
            Helpers.updateLocationAndTime(getContext(), userLocalStore, currentTime);
        }
    }
}
