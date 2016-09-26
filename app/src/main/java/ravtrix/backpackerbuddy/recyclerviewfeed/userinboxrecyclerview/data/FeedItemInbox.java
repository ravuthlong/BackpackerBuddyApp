package ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ravinder on 9/24/16.
 */

public class FeedItemInbox {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("latestDate")
    @Expose
    private String latestDate;
    @SerializedName("latestMessage")
    @Expose
    private String latestMessage;
    @SerializedName("userID")
    @Expose
    private int userID;
    @SerializedName("status")
    @Expose
    int status;
    private String chatRoom;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(String latestDate) {
        this.latestDate = latestDate;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
