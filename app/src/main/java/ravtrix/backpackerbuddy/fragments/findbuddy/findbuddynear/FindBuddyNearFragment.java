package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear.adapter.CustomGridView;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.models.UserLocationInfo;

import static ravtrix.backpackerbuddy.R.id.spinner;

/**
 * Created by Ravinder on 9/12/16.
 */
public class FindBuddyNearFragment extends Fragment implements IFindBuddyNearView {

    @BindView(R.id.grid_view) protected GridView profileImageGridView;
    @BindView(R.id.frag_gridview_city) protected TextView city;
    @BindView(R.id.layout_noNearby) protected LinearLayout layout_noNearby;
    @BindView(R.id.grid_relativeLayout) protected RelativeLayout nearbyRelative;
    @BindView(R.id.frag_gridview_progressbar) protected ProgressBar progressBar;
    private UserLocalStore userLocalStore;
    private FindBuddyPresenter findBuddyPresenter;
    private Spinner distanceSpinner;
    private int currentSelectedDropdown = 0;
    private static final int OPTION_ONE = 100;
    private static final int OPTION_TWO = 500;
    private static final int OPTION_THREE = 1000;
    private static final int OPTION_FOUR = 5000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.frag_gridview, container, false);

        ButterKnife.bind(this, fragView);
        setHasOptionsMenu(true);
        profileImageGridView.setVisibility(View.INVISIBLE);

        Helpers.overrideFonts(getActivity(), nearbyRelative);
        userLocalStore = new UserLocalStore(getActivity());
        findBuddyPresenter = new FindBuddyPresenter(this, getContext());

        Helpers.checkLocationUpdate(getActivity(), userLocalStore);

        if (userLocalStore.getLoggedInUser().getUserID() != 0) {
            findBuddyPresenter.fetchBuddyNearRetrofit(userLocalStore.getLoggedInUser().getUserID(), OPTION_ONE);
        } else {
            findBuddyPresenter.fetchBuddyNearGuestRetrofit(OPTION_ONE);
        }
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

                /**
                 * Position 0 = 25 miles
                 * Position 1 = 50 miles
                 * Position 2 = 100 miles
                 * Position 3 = 200 miles
                 */
                // Only refresh retrofit if the selected drop down option is not the same as the selected drop down before
                if (currentSelectedDropdown != position) {
                    progressBar.setVisibility(View.VISIBLE);

                    switch (position) {
                        case 0:
                            currentSelectedDropdown = 0;
                            fetchBuddy(OPTION_ONE);
                            break;
                        case 1:
                            currentSelectedDropdown = 1;
                            fetchBuddy(OPTION_TWO);
                            break;
                        case 2:
                            currentSelectedDropdown = 2;
                            fetchBuddy(OPTION_THREE);

                            break;
                        case 3:
                            currentSelectedDropdown = 3;
                            fetchBuddy(OPTION_FOUR);
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

    /**
     * Choose the desired retrofit call based on the type of user.
     * Logged in user uses their localstore longitude and latitude
     * Guest user needs additional location fetching
     * @param radius                the radius to find other nearby users
     */
    private void fetchBuddy(int radius) {
        if (userLocalStore.getLoggedInUser().getUserID() != 0) {
            findBuddyPresenter.fetchBuddyNearRetrofit(userLocalStore.getLoggedInUser().getUserID(), radius);
        } else {
            findBuddyPresenter.fetchBuddyNearGuestRetrofit(radius);
        }
    }

    public int getCurrentSelectedDropdown() {
        return this.currentSelectedDropdown;
    }

    public void setCurrentSelectedDropdown(int currentSelectedDropdown) {
        this.currentSelectedDropdown = currentSelectedDropdown;
    }

    @Override
    public void setCustomGridView(List<UserLocationInfo> userList) {
        CustomGridView customGridViewAdapter = new CustomGridView(getActivity(), userList);
        profileImageGridView.setAdapter(customGridViewAdapter);
    }

    @Override
    public void setCityText() {
        try {
            city.setText(Helpers.cityGeocoder(getActivity(), userLocalStore.getLoggedInUser().getLatitude(),
                    userLocalStore.getLoggedInUser().getLongitude()));

        } catch (IOException e) {
            // When the device failed to retrieve city and country information using Geocoder,
            // run google location API directly
            RetrieveCityCountryTask retrieveFeedTask =
                    new RetrieveCityCountryTask(userLocalStore.getLoggedInUser().getLatitude().toString(),
                    userLocalStore.getLoggedInUser().getLongitude().toString());
            retrieveFeedTask.execute();
        }
    }

    @Override
    public void showErrorToast() {
        Helpers.displayErrorToast(getContext());
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setViewInvisible() {
        profileImageGridView.setVisibility(View.INVISIBLE);
        nearbyRelative.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setViewVisible() {
        profileImageGridView.setVisibility(View.VISIBLE);
        nearbyRelative.setVisibility(View.VISIBLE);
    }

    @Override
    public void setNoNearbyVisible() {
        this.layout_noNearby.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoNearby() {
        this.layout_noNearby.setVisibility(View.GONE);
    }

    /**
     * Retrieve city and country location information from google location API
     */
    private class RetrieveCityCountryTask extends AsyncTask<Void, Void, String> {
        String latitude;
        String longitude;

        RetrieveCityCountryTask(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
        @Override
        protected String doInBackground(Void... voids) {
            return (Helpers.getLocationInfo(latitude, longitude));
        }
        @Override
        protected void onPostExecute(String s) {
            city.setText(s);
        }
    }
}
