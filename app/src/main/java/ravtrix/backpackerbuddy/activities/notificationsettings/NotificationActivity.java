package ravtrix.backpackerbuddy.activities.notificationsettings;

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
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class NotificationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        INotificationView {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_notif_llayout) protected LinearLayout linearLayout;
    @BindView(R.id.activity_notif_sNotif) protected SwitchCompat sNotif;
    @BindView(R.id.activity_notif_sComment) protected SwitchCompat sComment;
    @BindView(R.id.activity_notif_statusMessage) protected TextView tvStatusMessage;
    @BindView(R.id.activity_notif_statusComment) protected TextView tvStatusComment;
    private UserLocalStore userLocalStore;
    private NotificationPresenter notificationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setTitle("Notification Settings");
        ButterKnife.bind(this);

        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, linearLayout);
        userLocalStore = new UserLocalStore(this);
        sNotif.setChecked(true);
        sComment.setChecked(true);

        notificationPresenter = new NotificationPresenter(this);

        notificationPresenter.fetchNotificationStatus(userLocalStore.getLoggedInUser().getUserID());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton,final boolean isChecked) {

        switch (compoundButton.getId()) {
            case R.id.activity_notif_sNotif:
                notificationPresenter.updateMessageNotif(userLocalStore.getLoggedInUser().getUserID(), isChecked);
                break;
            case R.id.activity_notif_sComment:
                notificationPresenter.updateCommentNotif(userLocalStore.getLoggedInUser().getUserID(), isChecked);
                break;
            default:
        }
    }

    @Override
    public void setMessageNotifOn() {
        sNotif.setChecked(true);
    }

    @Override
    public void setMessageNotifOff() {
        sNotif.setChecked(false);
    }

    @Override
    public void setCommentNotifOn() {
        sComment.setChecked(true);
    }

    @Override
    public void setCommentNotifOff() {
        sComment.setChecked(false);
    }

    @Override
    public void setTvMessageOn() {
        tvStatusMessage.setText(getString(R.string.on));
    }

    @Override
    public void setTvMessageOff() {
        tvStatusMessage.setText(getString(R.string.off));
    }

    @Override
    public void setTvCommentOn() {
        tvStatusComment.setText(getString(R.string.on));
    }

    @Override
    public void setTvCommentOff() {
        tvStatusComment.setText(getString(R.string.off));
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(this);
    }

    @Override
    public void setButtonListeners() {
        sNotif.setOnCheckedChangeListener(NotificationActivity.this);
        sComment.setOnCheckedChangeListener(NotificationActivity.this);
    }
}
