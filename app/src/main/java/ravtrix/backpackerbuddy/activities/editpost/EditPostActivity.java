package ravtrix.backpackerbuddy.activities.editpost;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
 * Created by Ravinder on 9/12/16.
 */
public class EditPostActivity extends OptionMenuSaveBaseActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, DatePickerListenerFrom, DatePickerListenerTo, IEditPostView {
    @BindView(R.id.spinnerCountries) protected Spinner spinnerCountries;
    @BindView(R.id.tvDateArrival) protected TextView tvDateArrival;
    @BindView(R.id.tvDateLeave) protected TextView tvDateLeave;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.layout_travelSelection) protected LinearLayout layout_travSelection;
    private static String[] fromDateArray, toDateArray;
    private String chosenDateFrom, chosenDateTo, chosenCountry;
    private int postID, returnActivity;
    private UserLocalStore userLocalStore;
    private EditPostPresenter editPostPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelselection);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, layout_travSelection);
        setTitle("Edit Info");

        setCountryAdapter();
        setSelectedCountryAndDate();
        editPostPresenter = new EditPostPresenter(this);
        spinnerCountries.setOnItemSelectedListener(this);
        tvDateArrival.setOnClickListener(this);
        tvDateLeave.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tvDateLeave:
                DialogFragment dateFragLeave = new DatePickerFragmentFrom();
                dateFragLeave.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.tvDateArrival:
                DialogFragment dateFragTo = new DatePickerFragmentTo();
                dateFragTo.show(getSupportFragmentManager(), "datePicker");
                break;
            default:
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerCountries:
                chosenCountry = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:

                HashMap<String, String> travelSpotHash = new HashMap<>();
                travelSpotHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                travelSpotHash.put("country", chosenCountry);
                travelSpotHash.put("from", chosenDateFrom);
                travelSpotHash.put("until", chosenDateTo);
                travelSpotHash.put("postID", Integer.toString(postID));
                editPostPresenter.editPost(travelSpotHash, returnActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class DatePickerFragmentFrom extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private DatePickerListenerFrom datePickerListenerFrom;

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Year, month day
            this.datePickerListenerFrom = (DatePickerListenerFrom) getActivity();

            return new DatePickerDialog(getActivity(), this, Integer.parseInt(fromDateArray[2]),
                    Integer.parseInt(fromDateArray[0]) - 1, Integer.parseInt(fromDateArray[1]));
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
            // Year, month day
            return new DatePickerDialog(getActivity(), this, Integer.parseInt(toDateArray[2]),
                    Integer.parseInt(toDateArray[0]) - 1, Integer.parseInt(toDateArray[1]));
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            datePickerListenerTo.returnDateTo(year, monthOfYear, dayOfMonth);
        }
    }

    private void setCountryAdapter() {
        // Each item
        ArrayAdapter<CharSequence> countryArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.countries, android.R.layout.simple_spinner_item);
        // Drop down
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countryArrayAdapter);
        spinnerCountries.setOnItemSelectedListener(this);
    }

    private void setSelectedCountryAndDate() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String[] country_array = getResources().getStringArray(R.array.countries);


            for (int i = 0; i < country_array.length; i++) {
                if (country_array[i].equals(bundle.getString("country"))) {
                    spinnerCountries.setSelection(i);
                }
            }
            String fromDate = bundle.getString("from");
            String toDate = bundle.getString("until");

            if (fromDate != null) {
                fromDateArray = fromDate.split("/");
                this.chosenDateFrom = fromDateArray[2] + "-" + fromDateArray[0] + "-" + fromDateArray[1];
            }
            if (toDate != null) {
                toDateArray = toDate.split("/");
                this.chosenDateTo = toDateArray[2] + "-" + toDateArray[0] + "-" + toDateArray[1];
            }

            this.chosenCountry = bundle.getString("country");

            tvDateLeave.setText(fromDate);
            tvDateArrival.setText(toDate);
            this.postID = bundle.getInt("postID");
            this.returnActivity = bundle.getInt("returnActivity");
        }
    }

    @Override
    public void returnDateFrom(int year, int month, int day) {
        tvDateLeave.setText((month + 1) + "/" + day + "/" + year);

        String yearString = Integer.toString(year);
        String monthString = Integer.toString(month + 1);
        String dayString = Integer.toString(day);
        chosenDateFrom = yearString + "-" + monthString + "-" + dayString;
    }

    @Override
    public void returnDateTo(int year, int month, int day) {
        tvDateArrival.setText((month + 1) + "/" + day + "/" + year);

        String yearString = Integer.toString(year);
        String monthString = Integer.toString(month + 1);
        String dayString = Integer.toString(day);
        chosenDateTo = yearString + "-" + monthString + "-" + dayString;
    }

    @Override
    public void startMainPageActivity() {
        Intent returnIntent = new Intent();
        setResult(1, returnIntent);
        finish();
    }

    @Override
    public void displayTryAgainToast() {
        Helpers.displayErrorToast(this);
    }
}

