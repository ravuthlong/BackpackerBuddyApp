package ravtrix.backpackerbuddy.activities.chat;

/**
 * Created by Ravinder on 11/22/16.
 */

interface IConversationPresenter {

    void notifyOtherUser(String message, String otherUserID, String username, int userID);
}
