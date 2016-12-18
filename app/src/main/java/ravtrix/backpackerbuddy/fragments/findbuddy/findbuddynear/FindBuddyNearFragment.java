package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.fragments.findbuddy.CustomGridView;
import ravtrix.backpackerbuddy.fragments.findbuddy.OnFinishedImageLoading;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.models.UserLocationInfo;

import static ravtrix.backpackerbuddy.R.id.spinner;

/**
 * Created by Ravinder on 9/12/16.
 */
public class FindBuddyNearFragment extends Fragment implements IFindBuddyNearView {

    @BindView(R.id.grid_view) protected GridView profileImageGridView;
    @BindView(R.id.frag_gridview_city) protected TextView city;
    private View fragView;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private UserLocalStore userLocalStore;
    private FindBuddyPresenter findBuddyPresenter;
    private Spinner distanceSpinner;
    private int currentSelectedDropdown = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.frag_gridview, container, false);

        ButterKnife.bind(this, fragView);
        setHasOptionsMenu(true);

        fragActivityProgressBarInterface.setProgressBarVisible();
        userLocalStore = new UserLocalStore(getActivity());
        findBuddyPresenter = new FindBuddyPresenter(this);

        checkLocationUpdate();
        findBuddyPresenter.fetchBuddyNearRetrofit(userLocalStore.getLoggedInUser().getUserID(), 25);
        return fragView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dropdown_toolbar, menu);

        MenuItem item = menu.findItem(spinner);
        distanceSpinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.distance_DropDown,
                R.layout.distance_dropdown);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceSpinner.setAdapter(spinnerAdapter);
        distanceSpinner.setSelection(currentSelectedDropdown);
        setSpinnerListener();
    }

    private void setSpinnerListener() {

        distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /*
                 * Position 0 = 25 miles
                 * Position 1 = 50 miles
                 * Position 2 = 100 miles
                 * Position 3 = 200 miles
                 */

                // Only refresh retrofit if the selected drop down option is not the same as the selected drop down before
                if (currentSelectedDropdown != position) {
                    fragActivityProgressBarInterface.setProgressBarVisible();

                    switch (position) {
                        case 0:
                            currentSelectedDropdown = 0;
                            findBuddyPresenter.fetchBuddyNearRetrofit(userLocalStore.getLoggedInUser().getUserID(), 25);
                            break;
                        case 1:
                            currentSelectedDropdown = 1;
                            findBuddyPresenter.fetchBuddyNearRetrofit(userLocalStore.getLoggedInUser().getUserID(), 50);
                            break;
                        case 2:
                            currentSelectedDropdown = 2;
                            findBuddyPresenter.fetchBuddyNearRetrofit(userLocalStore.getLoggedInUser().getUserID(), 100);
                            break;
                        case 3:
                            currentSelectedDropdown = 3;
                            findBuddyPresenter.fetchBuddyNearRetrofit(userLocalStore.getLoggedInUser().getUserID(), 200);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing selected.
            }
        });
    }

    public int getCurrentSelectedDropdown() {
        return this.currentSelectedDropdown;
    }

    public void setCurrentSelectedDropdown(int currentSelectedDropdown) {
        this.currentSelectedDropdown = currentSelectedDropdown;
    }

    /*
     * Check if location update is needed. If needed, update local store and server
     */
    private void checkLocationUpdate() {
        long currentTime = System.currentTimeMillis();

        // If it's been 5 minute since last location update, do the update
        if (Helpers.timeDifInMinutes(currentTime,
                userLocalStore.getLoggedInUser().getTime()) > 5) {
            Helpers.updateLocationAndTime(getContext(), userLocalStore, currentTime);
        }
    }

    @Override
    public void setCustomGridView(List<UserLocationInfo> userList) {
        CustomGridView customGridViewAdapter = new CustomGridView(getActivity(), userList,
                profileImageGridView, fragActivityProgressBarInterface, new OnFinishedImageLoading() {
            @Override
            public void onFinishedImageLoading() {
                hideProgressbar();
                setViewVisible();
            }
        });
        profileImageGridView.setAdapter(customGridViewAdapter);
    }

    @Override
    public void setCityText() {
        city.setText(Helpers.cityGeocoder(getContext(), userLocalStore.getLoggedInUser().getLatitude(),
                userLocalStore.getLoggedInUser().getLongitude()));
    }

    @Override
    public void showErrorToast() {
        Helpers.displayToast(getContext(), "Error");
    }

    @Override
    public void hideProgressbar() {
        fragActivityProgressBarInterface.setProgressBarInvisible();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        findBuddyPresenter.onDestroy();
    }

    @Override
    public void setViewInvisible() {
        fragView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setViewVisible() {
        fragView.setVisibility(View.VISIBLE);
    }
}
