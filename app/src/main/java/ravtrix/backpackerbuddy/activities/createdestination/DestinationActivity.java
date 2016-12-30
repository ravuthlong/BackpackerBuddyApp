package ravtrix.backpackerbuddy.activities.createdestination;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.interfacescom.DatePickerListenerFrom;
import ravtrix.backpackerbuddy.interfacescom.DatePickerListenerTo;
import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 7/29/16.
 */
public class DestinationActivity extends OptionMenuSaveBaseActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, IDestinationView, DatePickerListenerFrom, DatePickerListenerTo {

    @BindView(R.id.spinnerCountries) protected Spinner spinnerCountries;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.layout_travelSelection) protected LinearLayout layout_travSelection;
    private TextView tvDateArrival;
    private TextView tvDateLeave;
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
    protected static Calendar dateTypeFrom, dateTypeUntil;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelselection);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, layout_travSelection);
        setTitle("Destination");

        setTodayDate();
        tvDateArrival = (TextView) findViewById(R.id.tvDateArrival);
        tvDateLeave = (TextView) findViewById(R.id.tvDateLeave);

        setCountrySpinnerDropdown();
        spinnerCountries.setOnItemSelectedListener(this);
        tvDateArrival.setOnClickListener(this);
        tvDateLeave.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        destinationPresenter = new DestinationPresenter(this);
        setDefaultDateFromAndTo();
        setDefaultDateObjects();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Prevents double clicking
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (item.getItemId()) {
            case R.id.submitSave:
                // Submit destination

                if (destinationPresenter.isDateValid(dateTypeFrom, dateTypeUntil)) {
                    // If date is valid. First date is before second date.
                    showProgressDialog();
                    HashMap<String, String> travelSpot = new HashMap<>();
                    travelSpot.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                    travelSpot.put("country", selectedCountry);
                    travelSpot.put("from", dateFrom);
                    travelSpot.put("until", dateTo);

                    // call retrofit to insert travel spot
                    destinationPresenter.insertTravelRetrofit(travelSpot);
                } else {
                    Toast.makeText(this, "Date from must be before Date until", Toast.LENGTH_SHORT).show();
                }

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

                android.support.v4.app.DialogFragment dateFragLeave = new DatePickerFragmentTo();
                dateFragLeave.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.tvDateLeave:
                android.support.v4.app.DialogFragment dateFragArrival = new DatePickerFragmentFrom();
                dateFragArrival.show(getSupportFragmentManager(), "datePicker");
                break;
        }
    }

    // Use the current date as the default date in the picker
    private void setTodayDate() {
        thisYear = c.get(Calendar.YEAR);
        thisMonth = c.get(Calendar.MONTH);
        thisDay = c.get(Calendar.DAY_OF_MONTH);
    }

    private void setDefaultDateObjects() {
        dateTypeFrom = Calendar.getInstance();
        dateTypeFrom.set(Calendar.DATE, thisDay);
        dateTypeFrom.set(Calendar.MONTH, thisMonth);
        dateTypeFrom.set(Calendar.YEAR, thisYear);

        dateTypeUntil = Calendar.getInstance();
        dateTypeUntil.set(Calendar.DATE, ++thisDay);
        dateTypeUntil.set(Calendar.MONTH, thisMonth);
        dateTypeUntil.set(Calendar.YEAR, thisYear);
    }

    private void setDefaultDateFromAndTo() {
        tvDateLeave.setText((thisMonth + 1) + "/" + thisDay + "/" + thisYear);
        tvDateArrival.setText((thisMonth + 1) + "/" + (thisDay + 1) + "/" + thisYear);

        dateFrom = thisYear + "-" + (thisMonth + 1) + "-" + thisDay;
        dateTo = thisYear + "-" + (thisMonth + 1) + "-" + (thisDay + 1);
    }

    private void setCountrySpinnerDropdown() {
        // Each item
        ArrayAdapter<CharSequence> countryArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.countries, android.R.layout.simple_spinner_item);
        // Drop down
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countryArrayAdapter);
    }

    public static class DatePickerFragmentFrom extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        private DatePickerListenerFrom datePickerListenerFrom;

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            datePickerListenerFrom = (DatePickerListenerFrom) getActivity();

            return new DatePickerDialog(getActivity(), this, thisYear, thisMonth, thisDay - 1);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            datePickerListenerFrom.returnDateFrom(year, monthOfYear, dayOfMonth);
        }
    }

    public static class DatePickerFragmentTo extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        private DatePickerListenerTo datePickerListenerTo;

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            datePickerListenerTo = (DatePickerListenerTo) getActivity();
            return new DatePickerDialog(getActivity(), this, thisYear, thisMonth, thisDay);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            datePickerListenerTo.returnDateTo(year, monthOfYear, dayOfMonth);
        }
    }

    @Override
    public void showProgressDialog() {
        this.progressDialog = Helpers.showProgressDialog(this, "Posting...");
    }

    @Override
    public void hideProgressDialog() {
        Helpers.hideProgressDialog(progressDialog);
    }

    @Override
    public void startDestinationFrag() {
        Intent returnIntent = new Intent();
        setResult(1, returnIntent); // pass result intent to main destination fragment tab
        finish();
    }

    @Override
    public void showToastError() {
        Helpers.displayToast(this, "Error");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destinationPresenter.onDestroy();
    }

    @Override
    public void returnDateFrom(int year, int month, int day) {
        tvDateLeave.setText((month + 1) + "/" + day + "/" + year);
        String yearString = Integer.toString(year);
        String monthString = Integer.toString(month + 1);
        String dayString = Integer.toString(day);

        dateTypeFrom = Calendar.getInstance();
        dateTypeFrom.set(Calendar.DATE, Integer.valueOf(dayString));
        dateTypeFrom.set(Calendar.MONTH, Integer.valueOf(monthString));
        dateTypeFrom.set(Calendar.YEAR, Integer.valueOf(yearString));
        dateFrom = yearString + "-" + monthString + "-" + dayString;
    }

    @Override
    public void returnDateTo(int year, int month, int day) {
        tvDateArrival.setText((month + 1) + "/" + day + "/" + year);
        String yearString = Integer.toString(year);
        String monthString = Integer.toString(month + 1);
        String dayString = Integer.toString(day);

        dateTypeUntil = Calendar.getInstance();
        dateTypeUntil.set(Calendar.DATE, Integer.valueOf(dayString));
        dateTypeUntil.set(Calendar.MONTH, Integer.valueOf(monthString));
        dateTypeUntil.set(Calendar.YEAR, Integer.valueOf(yearString));
        dateTo = yearString + "-" + monthString + "-" + dayString;
    }
}