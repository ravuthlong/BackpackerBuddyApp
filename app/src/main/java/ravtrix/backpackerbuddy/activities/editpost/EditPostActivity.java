package ravtrix.backpackerbuddy.activities.editpost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.BackgroundImage;

/**
 * Created by Ravinder on 9/12/16.
 */
public class EditPostActivity extends OptionMenuSaveBaseActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.activity_travelSelection_spinnerCountries) protected Spinner spinnerCountries;
    @BindView(R.id.activity_travelSelection_tvDateArrival) protected TextView tvDateArrival;
    @BindView(R.id.activity_travelSelection_tvDateLeave) protected TextView tvDateLeave;
    private ArrayAdapter<CharSequence> countryArrayAdapter;
    private BackgroundImage backgroundImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelselection);
        ButterKnife.bind(this);

        // Each item
        countryArrayAdapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
        // Drop down
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countryArrayAdapter);
        spinnerCountries.setOnItemSelectedListener(this);

        backgroundImage = new BackgroundImage();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            spinnerCountries.setPrompt(bundle.getString("country"));
            bundle.getString("from");
            bundle.getString("until");
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

