package ravtrix.backpackerbuddy.fragments.mainfrag.countrybyfilter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ravtrix.backpackerbuddy.R;

/**
 * Created by Ravinder on 10/9/16.
 */

public class CountryFilterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_usercountries, container, false);


        return view;
    }
}
