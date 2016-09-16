package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
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
    private View view;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private UserLocalStore userLocalStore;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
        fragActivityProgressBarInterface.setProgressBarVisible();
        userLocalStore = new UserLocalStore(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_gridview, container, false);
        view.setVisibility(View.GONE);

        ButterKnife.bind(this, view);

        Call<List<UserLocationInfo>> retrofitCall = RetrofitUserInfoSingleton.getRetrofitUserInfo ()
                .getNearbyUsers()
                .getNearbyUsers(userLocalStore.getLoggedInUser().getUserID(), 40);
        retrofitCall.enqueue(new Callback<List<UserLocationInfo>>() {
            @Override
            public void onResponse(Call<List<UserLocationInfo>> call, Response<List<UserLocationInfo>> response) {
                List<UserLocationInfo> userList = response.body();
                fragActivityProgressBarInterface.setProgressBarInvisible();

                CustomGridView customGridViewAdapter = new CustomGridView(getActivity(), userList);
                profileImageGridView.setAdapter(customGridViewAdapter);
                profileImageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
                view.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<List<UserLocationInfo>> call, Throwable t) {
            }
        });
        return view;
    }
}
