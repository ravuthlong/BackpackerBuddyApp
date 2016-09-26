package ravtrix.backpackerbuddy.models;

/**
 * Created by Ravinder on 9/25/16.
 */

public class UserInbox {


    private int userID;
    private int status; // 0 means user started the chat. 1 means the user didn't start the chat
    private String chatRoom;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }
}
