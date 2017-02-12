package ravtrix.backpackerbuddy.activities.notificationsettings;

/**
 * Created by Ravinder on 1/22/17.
 */

interface INotificationView {

    void setMessageNotifOn();
    void setMessageNotifOff();
    void setCommentNotifOn();
    void setCommentNotifOff();
    void setTvMessageOn();
    void setTvMessageOff();
    void setTvCommentOn();
    void setTvCommentOff();
    void displayErrorToast();
    void setButtonListeners();
}
