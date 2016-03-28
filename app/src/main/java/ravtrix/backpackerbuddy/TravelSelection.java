package ravtrix.backpackerbuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;

public class TravelSelection extends AppCompatActivity {

    private Spinner spinnerCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelselection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTrav);
        setSupportActionBar(toolbar);

        spinnerCountries = (Spinner) findViewById(R.id.spinnerCountries);



    }

}
