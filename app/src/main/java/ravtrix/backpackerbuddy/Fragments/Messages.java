package ravtrix.backpackerbuddy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ravtrix.backpackerbuddy.R;

/**
 * Created by Ravinder on 7/29/16.
 */
public class Messages extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_userinbox, container, false);

        return v;
    }
}
