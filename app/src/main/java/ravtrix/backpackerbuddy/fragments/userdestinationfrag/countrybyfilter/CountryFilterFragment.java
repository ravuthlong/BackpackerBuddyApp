package ravtrix.backpackerbuddy.fragments.userdestinationfrag.countrybyfilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;

/**
 * Created by Ravinder on 10/9/16.
 */

public class CountryFilterFragment extends Fragment {
    @BindView(R.id.frag_filter_spinnerCountry) protected Spinner spinnerCountry;
    @BindView(R.id.frag_filter_spinnerMonth) protected Spinner spinnerMonth;
    private Bundle queryBundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_usercountries_filter, container, false);

        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        ArrayAdapter spinnerAdapterCountry = ArrayAdapter.createFromResource(getContext(),
                R.array.countries,
                android.R.layout.simple_spinner_item);
        spinnerAdapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(spinnerAdapterCountry);

        ArrayAdapter spinnerAdapterMonth = ArrayAdapter.createFromResource(getContext(),
                R.array.month_DropDown,
                android.R.layout.simple_spinner_item);
        spinnerAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(spinnerAdapterMonth);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_submit_filter, menu);
    }

    /*
     *  Pass query info to the UserMainPage activity so it can communicate with
     *  CountryRecentFragment and start it there
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit_filter:

                // Query info provided by the user based on selected spinner item
                this.queryBundle = new Bundle();

                int month;

                switch (spinnerMonth.getSelectedItem().toString()) {
                    case "January":
                        month = 1;
                        break;
                    case "February":
                        month = 2;
                        break;
                    case "March":
                        month = 3;
                        break;
                    case "April":
                        month = 4;
                        break;
                    case "May":
                        month = 5;
                        break;
                    case "June":
                        month = 6;
                        break;
                    case "July":
                        month = 7;
                        break;
                    case "August":
                        month = 8;
                        break;
                    case "September":
                        month = 9;
                        break;
                    case "October":
                        month = 10;
                        break;
                    case "November":
                        month = 11;
                        break;
                    case "December":
                        month = 12;
                        break;
                    default:
                        month = 1;
                        break;
                }

                Intent intent = new Intent(getContext(), UserMainPage.class);
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("country", spinnerCountry.getSelectedItem().toString());
                intent.putExtra("month", month);
                startActivity(intent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
