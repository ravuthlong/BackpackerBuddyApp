package ravtrix.backpackerbuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class TravelSelection extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerCountries;
    private ArrayAdapter<CharSequence> countryArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelselection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTrav);
        setSupportActionBar(toolbar);

        spinnerCountries = (Spinner) findViewById(R.id.spinnerCountries);

        // Each item
        countryArrayAdapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
        // Drop down
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countryArrayAdapter);
        spinnerCountries.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerCountries:
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected..", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
