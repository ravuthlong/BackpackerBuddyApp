package ravtrix.backpackerbuddy.fcm.model;

/**
 * Created by Ravinder on 9/21/16.
 */

public class Message {

    private String text;
    private String photoUrl;
    private long time;
    private int userID;
    private int isOtherUserClicked;

    public Message() {
    }

    public Message(int userID, String text, String photoUrl, long time, int isOtherUserClicked) {
        this.userID = userID;
        this.text = text;
        this.photoUrl = photoUrl;
        this.time = time;
        this.isOtherUserClicked = isOtherUserClicked;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getIsOtherUserClicked() {
        return isOtherUserClicked;
    }

    public void setIsOtherUserClicked(int isOtherUserClicked) {
        this.isOtherUserClicked = isOtherUserClicked;
    }
}
