package ravtrix.backpackerbuddy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ravtrix.backpackerbuddy.ServerRequests.ServerRequests;

public class TravelSelection extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

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
    private ServerRequests serverRequests;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelselection);

        Toolbar toolbarTrav = (Toolbar) findViewById(R.id.toolbarTrav);
        setSupportActionBar(toolbarTrav);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (toolbarTrav != null) {
            toolbarTrav.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        thisYear = c.get(Calendar.YEAR);
        thisMonth = c.get(Calendar.MONTH);
        thisDay = c.get(Calendar.DAY_OF_MONTH);


        tvDateArrival = (TextView) findViewById(R.id.tvDateArrival);
        tvDateLeave = (TextView) findViewById(R.id.tvDateLeave);
        spinnerCountries = (Spinner) findViewById(R.id.spinnerCountries);
        btSubmitCountry = (Button) findViewById(R.id.btSubmitCountry);

        // Each item
        countryArrayAdapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
        // Drop down
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countryArrayAdapter);
        spinnerCountries.setOnItemSelectedListener(this);
        tvDateArrival.setOnClickListener(this);
        tvDateLeave.setOnClickListener(this);
        btSubmitCountry.setOnClickListener(this);
        serverRequests = new ServerRequests(this);
        userLocalStore = new UserLocalStore(this);

        tvDateArrival.setText((thisMonth + 1) + "-" + thisDay + "-" + thisYear);
        tvDateLeave.setText((thisMonth + 1) + "-" + (thisDay + 1) + "-" + thisYear);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDateArrival:
                android.support.v4.app.DialogFragment dateFragArrival = new DatePickerFragmentFrom();
                dateFragArrival.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.tvDateLeave:
                android.support.v4.app.DialogFragment dateFragLeave = new DatePickerFragmentTo();
                dateFragLeave.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.btSubmitCountry:
                TravelSpot travelSpot = new TravelSpot(userLocalStore.getLoggedInUser().getUserID(), selectedCountry, dateFrom, dateTo);
                serverRequests.insertTravelSpotInBackground(travelSpot);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerCountries:
                selectedCountry = parent.getItemAtPosition(position).toString();

                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected..", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
