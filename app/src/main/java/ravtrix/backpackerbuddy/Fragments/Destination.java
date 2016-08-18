package ravtrix.backpackerbuddy.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.Models.TravelSpot;
import ravtrix.backpackerbuddy.Models.UserLocalStore;
import ravtrix.backpackerbuddy.VolleyServerConnections.VolleyDestination;

/**
 * Created by Ravinder on 7/29/16.
 */
public class Destination extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner spinnerCountries;
    private ArrayAdapter<CharSequence> countryArrayAdapter;
    private String selectedCountry;
    private static TextView tvDateArrival, tvDateLeave;
    private static final int DIALOG_ID = 0;
    private static int thisYear;
    private static int thisDay;
    private static int thisMonth;
    private static String dateFrom;
    private static String dateTo;
    private Button btSubmitCountry;
    private VolleyDestination volleyDestination;
    //private ServerRequests serverRequests;
    private UserLocalStore userLocalStore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_travelselection, container, false);


        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        thisYear = c.get(Calendar.YEAR);
        thisMonth = c.get(Calendar.MONTH);
        thisDay = c.get(Calendar.DAY_OF_MONTH);


        tvDateArrival = (TextView) v.findViewById(R.id.tvDateArrival);
        tvDateLeave = (TextView) v.findViewById(R.id.tvDateLeave);
        spinnerCountries = (Spinner) v.findViewById(R.id.spinnerCountries);
        btSubmitCountry = (Button) v.findViewById(R.id.btSubmitCountry);

        // Each item
        countryArrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.countries, android.R.layout.simple_spinner_item);
        // Drop down
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countryArrayAdapter);


        spinnerCountries.setOnItemSelectedListener(this);
        tvDateArrival.setOnClickListener(this);
        tvDateLeave.setOnClickListener(this);
        btSubmitCountry.setOnClickListener(this);
        //serverRequests = new ServerRequests(this);
        volleyDestination = new VolleyDestination(getActivity());
        userLocalStore = new UserLocalStore(getActivity());

        tvDateArrival.setText((thisMonth + 1) + "-" + thisDay + "-" + thisYear);
        tvDateLeave.setText((thisMonth + 1) + "-" + (thisDay + 1) + "-" + thisYear);


        dateFrom = thisYear + "-" + (thisMonth + 1) + "-" + thisDay;
        dateTo = thisYear + "-" + (thisMonth + 1) + "-" + (thisDay + 1);

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerCountries:
                selectedCountry = parent.getItemAtPosition(position).toString();

                Toast.makeText(getActivity().getBaseContext(), parent.getItemAtPosition(position) + " selected..", Toast.LENGTH_SHORT).show();
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
            case R.id.btSubmitCountry:

                TravelSpot travelSpot = new TravelSpot(userLocalStore.getLoggedInUser().getUserID(), selectedCountry, dateFrom, dateTo);
                //serverRequests.insertTravelSpotInBackground(travelSpot);
                volleyDestination.createDestinationVolley(travelSpot);
                break;
        }
    }


    public static class DatePickerFragmentFrom extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(), this, thisYear, thisMonth, thisDay);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            tvDateArrival.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
            monthOfYear += 1;
            String yearString = Integer.toString(year);
            String monthString = Integer.toString(monthOfYear);
            String dayString = Integer.toString(dayOfMonth);

            dateFrom = yearString + "-" + monthString + "-" + dayString;
        }
    }


    public static class DatePickerFragmentTo extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(), this, thisYear, thisMonth, thisDay);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            tvDateLeave.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
            monthOfYear += 1;
            String yearString = Integer.toString(year);
            String monthString = Integer.toString(monthOfYear);
            String dayString = Integer.toString(dayOfMonth);

            dateTo = yearString + "-" + monthString + "-" + dayString;
        }
    }
}