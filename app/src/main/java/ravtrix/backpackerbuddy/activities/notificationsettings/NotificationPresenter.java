package ravtrix.backpackerbuddy.activities.notificationsettings;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/22/17.
 */

class NotificationPresenter implements INotificationPresenter {
    private INotificationView iNotificationView;
    private NotificationInteractor notificationInteractor;

    NotificationPresenter(INotificationView iNotificationView) {
        this.iNotificationView = iNotificationView;
        this.notificationInteractor = new NotificationInteractor();
    }

    @Override
    public void fetchNotificationStatus(int userID) {

        this.notificationInteractor.fetchNotificationStatus(userID, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("messageNotif").getAsInt() == 1) {
                    // User chose message notification to be turned on
                    iNotificationView.setMessageNotifOn();
                    iNotificationView.setTvMessageOn();
                } else {
                    // User chose message notification to be turned off
                    iNotificationView.setMessageNotifOff();
                    iNotificationView.setTvMessageOff();
                }

                if (jsonObject.get("commentNotif").getAsInt() == 1) {
                    // User chose comment notification to be turned on
                    iNotificationView.setCommentNotifOn();
                    iNotificationView.setTvCommentOn();
                } else {
                    // Off
                    iNotificationView.setCommentNotifOff();
                    iNotificationView.setTvCommentOff();
                }

                iNotificationView.setButtonListeners();
            }

            @Override
            public void onError() {
                iNotificationView.displayErrorToast();
            }
        });
    }

    @Override
    public void updateCommentNotif(int userID, final boolean isChecked) {

        this.notificationInteractor.updateCommentNotif(userID, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (isChecked) {
                    iNotificationView.setTvCommentOn();
                } else {
                    iNotificationView.setTvCommentOff();
                }
            }

            @Override
            public void onError() {
                iNotificationView.displayErrorToast();
            }
        });
    }

    @Override
    public void updateMessageNotif(int userID, final boolean isChecked) {

        this.notificationInteractor.updateMessageNotif(userID, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (isChecked) {
                    iNotificationView.setTvMessageOn();
                } else {
                    iNotificationView.setTvMessageOff();
                }
            }

            @Override
            public void onError() {
                iNotificationView.displayErrorToast();
            }
        });
    }
}
