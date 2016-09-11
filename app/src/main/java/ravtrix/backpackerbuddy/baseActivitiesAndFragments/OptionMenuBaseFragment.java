package ravtrix.backpackerbuddy.baseActivitiesAndFragments;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

import ravtrix.backpackerbuddy.R;

/**
 * Created by Ravinder on 9/9/16.
 */
public class OptionMenuBaseFragment extends Fragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_send, menu);
    }
}
