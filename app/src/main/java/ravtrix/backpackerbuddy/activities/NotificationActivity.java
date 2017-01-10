package ravtrix.backpackerbuddy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;

public class NotificationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_notif_llayout) protected LinearLayout linearLayout;
    @BindView(R.id.activity_notif_sNotif) protected SwitchCompat sNotif;
    @BindView(R.id.activity_notif_sComment) protected SwitchCompat sComment;
    @BindView(R.id.activity_notif_statusMessage) protected TextView tvStatusMessage;
    @BindView(R.id.activity_notif_statusComment) protected TextView tvStatusComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setTitle("Notification Settings");
        ButterKnife.bind(this);

        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, linearLayout);

        sNotif.setOnCheckedChangeListener(this);
        sComment.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        switch (compoundButton.getId()) {
            case R.id.activity_notif_sNotif:

                if (isChecked) {
                    tvStatusMessage.setText("ON");
                } else {
                    tvStatusMessage.setText("OFF");
                }
                break;
            case R.id.activity_notif_sComment:

                if (isChecked) {
                    tvStatusComment.setText("ON");
                } else {
                    tvStatusComment.setText("OFF");
                }
                break;
            default:
        }

    }
}
