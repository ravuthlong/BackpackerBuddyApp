package ravtrix.backpackerbuddy.activities.chat;

/**
 * Created by Ravinder on 11/22/16.
 */

class ConversationPresentor implements IConversationPresenter {

    private IConversationView iConversationView;
    private ConversationInteractor conversationInteractor;

    ConversationPresentor(IConversationView iConversationView) {
        this.iConversationView = iConversationView;
        this.conversationInteractor = new ConversationInteractor();
    }

    @Override
    public void notifyOtherUser(String message, String otherUserID, String username, int userID) {
        conversationInteractor.notifyOtherUser(message, otherUserID, username, userID);
    }
}
