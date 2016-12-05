package ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.data;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ravinder on 9/24/16.
 */

public class FeedItemInbox implements Comparable<FeedItemInbox> {

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
    private int status;
    @SerializedName("success")
    @Expose
    private int success;
    private String chatRoom;
    private int isOtherUserClicked;
    private int lastMessageUserID;
    private long timeMilli;
    private transient DataSnapshot snapshot;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

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

    public int getIsOtherUserClicked() {
        return isOtherUserClicked;
    }

    public void setIsOtherUserClicked(int isOtherUserClicked) {
        this.isOtherUserClicked = isOtherUserClicked;
    }

    public int getLastMessageUserID() {
        return lastMessageUserID;
    }

    public void setLastMessageUserID(int lastMessageUserID) {
        this.lastMessageUserID = lastMessageUserID;
    }

    public DataSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(DataSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    private long getTimeMilli() {
        return timeMilli;
    }

    public void setTimeMilli(long timeMilli) {
        this.timeMilli = timeMilli;
    }

    @Override
    public int compareTo(FeedItemInbox another) {

        long anotherTime = another.getTimeMilli();
        //descending order
        return (int) (anotherTime - this.timeMilli);
    }
}
