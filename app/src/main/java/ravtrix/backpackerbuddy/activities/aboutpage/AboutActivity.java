package ravtrix.backpackerbuddy.activities.aboutpage;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;

public class AboutActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.tvAbout) protected TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        this.setTitle("About");
        Typeface font = Typeface.createFromAsset(getAssets(), "Text.ttf");
        tvAbout.setTypeface(font);
    }
}
