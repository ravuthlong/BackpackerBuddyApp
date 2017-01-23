package ravtrix.backpackerbuddy.activities.notificationsettings;

/**
 * Created by Ravinder on 1/22/17.
 */

interface INotificationPresenter {

    void fetchNotificationStatus(int userID);
    void updateCommentNotif(int userID, boolean isChecked);
    void updateMessageNotif(int userID, boolean isChecked);
}
