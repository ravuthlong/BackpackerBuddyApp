package ravtrix.backpackerbuddy.baseActivitiesAndFragments;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import ravtrix.backpackerbuddy.R;

/**
 * Created by Ravinder on 9/9/16.
 */
public class OptionMenuBaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_send, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
