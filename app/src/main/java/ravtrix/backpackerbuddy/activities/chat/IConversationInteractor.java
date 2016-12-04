package ravtrix.backpackerbuddy.activities.chat;


/**
 * Created by Ravinder on 11/22/16.
 */

interface IConversationInteractor {

    /**
     * Notify the other user the message has been sent to them
     * @param message           the message
     */
    void notifyOtherUser(String message, String otherUserID, String username, int userID);
}
