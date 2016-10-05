package ravtrix.backpackerbuddy.fragments.destination;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.maincountry.UserMainPage;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSendBaseFragment;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 7/29/16.
 */
public class DestinationFragment extends OptionMenuSendBaseFragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, IDestinationView {

    @BindView(R.id.spinnerCountries) protected Spinner spinnerCountries;
    protected static TextView tvDateArrival;
    protected static TextView tvDateLeave;
    protected String selectedCountry;
    private static final int DIALOG_ID = 0;
    private static int thisYear;
    private static int thisDay;
    private static int thisMonth;
    private static String dateFrom;
    private static String dateTo;
    private UserLocalStore userLocalStore;
    private ProgressDialog progressDialog;
    private DestinationPresenter destinationPresenter;
    private Calendar c = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_travelselection, container, false);
        //RefWatcher refWatcher = UserMainPage.getRefWatcher(getActivity());
        //refWatcher.watch(this);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, v);
        getActivity().setTitle("Destination");
        setTodayDate();
        tvDateArrival = (TextView) v.findViewById(R.id.tvDateArrival);
        tvDateLeave = (TextView) v.findViewById(R.id.tvDateLeave);

        setCountrySpinnerDropdown();
        spinnerCountries.setOnItemSelectedListener(this);
        tvDateArrival.setOnClickListener(this);
        tvDateLeave.setOnClickListener(this);
        userLocalStore = new UserLocalStore(getActivity());
        destinationPresenter = new DestinationPresenter(this);
        setDefaultDateFromAndTo();

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitSend:

                showProgressDialog();
                HashMap<String, String> travelSpot = new HashMap<>();
                travelSpot.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                travelSpot.put("country", selectedCountry);
                travelSpot.put("from", dateFrom);
                travelSpot.put("until", dateTo);

                // call retrofit to insert travel spot
                destinationPresenter.insertTravelRetrofit(travelSpot);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerCountries:
                selectedCountry = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDateArrival:
                android.support.v4.app.DialogFragment dateFragArrival = new DatePickerFragmentFrom();
                dateFragArrival.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.tvDateLeave:
                android.support.v4.app.DialogFragment dateFragLeave = new DatePickerFragmentTo();
                dateFragLeave.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
        }
    }

    // Use the current date as the default date in the picker
    private void setTodayDate() {
        thisYear = c.get(Calendar.YEAR);
        thisMonth = c.get(Calendar.MONTH);
        thisDay = c.get(Calendar.DAY_OF_MONTH);
    }

    private void setDefaultDateFromAndTo() {
        tvDateArrival.setText((thisMonth + 1) + "/" + thisDay + "/" + thisYear);
        tvDateLeave.setText((thisMonth + 1) + "/" + (thisDay + 1) + "/" + thisYear);

        dateFrom = thisYear + "-" + (thisMonth + 1) + "-" + thisDay;
        dateTo = thisYear + "-" + (thisMonth + 1) + "-" + (thisDay + 1);
    }

    private void setCountrySpinnerDropdown() {
        // Each item
        ArrayAdapter<CharSequence> countryArrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.countries, android.R.layout.simple_spinner_item);
        // Drop down
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countryArrayAdapter);
    }

    public static class DatePickerFragmentFrom extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(), this, thisYear, thisMonth, thisDay);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            tvDateArrival.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
            monthOfYear += 1;
            String yearString = Integer.toString(year);
            String monthString = Integer.toString(monthOfYear);
            String dayString = Integer.toString(dayOfMonth);

            dateFrom = yearString + "-" + monthString + "-" + dayString;
        }
    }

    public static class DatePickerFragmentTo extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(), this, thisYear, thisMonth, thisDay);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            tvDateLeave.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
            monthOfYear += 1;
            String yearString = Integer.toString(year);
            String monthString = Integer.toString(monthOfYear);
            String dayString = Integer.toString(dayOfMonth);

            dateTo = yearString + "-" + monthString + "-" + dayString;
        }
    }

    @Override
    public void showProgressDialog() {
        this.progressDialog = Helpers.showProgressDialog(getActivity(), "Posting...");
    }

    @Override
    public void hideProgressDialog() {
        Helpers.hideProgressDialog(progressDialog);
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(getActivity(), UserMainPage.class));
    }

    @Override
    public void showToastError() {
        Helpers.displayToast(getActivity(), "Error");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destinationPresenter.onDestroy();
    }
}