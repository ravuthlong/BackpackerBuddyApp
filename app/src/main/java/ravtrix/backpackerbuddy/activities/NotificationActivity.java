package ravtrix.backpackerbuddy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitNotificationSingleton;
import ravtrix.backpackerbuddy.interfacescom.OnFinishedListenerRetrofit;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_notif_llayout) protected LinearLayout linearLayout;
    @BindView(R.id.activity_notif_sNotif) protected SwitchCompat sNotif;
    @BindView(R.id.activity_notif_sComment) protected SwitchCompat sComment;
    @BindView(R.id.activity_notif_statusMessage) protected TextView tvStatusMessage;
    @BindView(R.id.activity_notif_statusComment) protected TextView tvStatusComment;
    private UserLocalStore userLocalStore;

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

        fetchNotificationStatus(new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("success").getAsInt() == 1) {

                    if (jsonObject.get("messageNotif").getAsInt() == 1) {
                        // User chose message notification to be turned on
                        sNotif.setChecked(true);
                        tvStatusMessage.setText(getString(R.string.on));
                    } else {
                        // Off
                        sNotif.setChecked(false);
                        tvStatusMessage.setText(getString(R.string.off));
                    }

                    if (jsonObject.get("commentNotif").getAsInt() == 1) {
                        // User chose comment notification to be turned on
                        sComment.setChecked(true);
                        tvStatusComment.setText(getString(R.string.on));
                    } else {
                        // Off
                        sComment.setChecked(false);
                        tvStatusComment.setText(getString(R.string.off));
                    }
                }
                sNotif.setOnCheckedChangeListener(NotificationActivity.this);
                sComment.setOnCheckedChangeListener(NotificationActivity.this);
            }
            @Override
            public void onError() {
                Helpers.displayErrorToast(NotificationActivity.this);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton,final boolean isChecked) {

        switch (compoundButton.getId()) {
            case R.id.activity_notif_sNotif:

                updateMessageNotif(new OnFinishedListenerRetrofit() {
                    @Override
                    public void onFinished(JsonObject jsonObject) {
                        if (isChecked) {
                            tvStatusMessage.setText(getString(R.string.on));
                        } else {
                            tvStatusMessage.setText(getString(R.string.off));
                        }
                    }
                    @Override
                    public void onError() {
                        Helpers.displayErrorToast(NotificationActivity.this);
                    }
                });
                break;
            case R.id.activity_notif_sComment:

                updateCommentNotif(new OnFinishedListenerRetrofit() {
                    @Override
                    public void onFinished(JsonObject jsonObject) {
                        if (isChecked) {
                            tvStatusComment.setText(getString(R.string.on));
                        } else {
                            tvStatusComment.setText(getString(R.string.off));
                        }
                    }
                    @Override
                    public void onError() {
                        Helpers.displayErrorToast(NotificationActivity.this);
                    }
                });
                break;
            default:
        }
    }

    private void fetchNotificationStatus(final OnFinishedListenerRetrofit onFinishedListener) {

        Call<JsonObject> retrofit = RetrofitNotificationSingleton.getRetrofitNotification()
                .getNotificationStatus()
                .getNotificationStatus(userLocalStore.getLoggedInUser().getUserID());

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onFinishedListener.onError();
            }
        });
    }

    private void updateCommentNotif(final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {

        Call<JsonObject> retrofit = RetrofitNotificationSingleton.getRetrofitNotification()
                .updateCommentNotification()
                .updateCommentNotification(userLocalStore.getLoggedInUser().getUserID());
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                onFinishedListenerRetrofit.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onFinishedListenerRetrofit.onError();
            }
        });
    }

    private void updateMessageNotif(final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {

        Call<JsonObject> retrofit = RetrofitNotificationSingleton.getRetrofitNotification()
                .updateMessageNotification()
                .updateMessageNotification(userLocalStore.getLoggedInUser().getUserID());
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                onFinishedListenerRetrofit.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onFinishedListenerRetrofit.onError();
            }
        });

    }
}
