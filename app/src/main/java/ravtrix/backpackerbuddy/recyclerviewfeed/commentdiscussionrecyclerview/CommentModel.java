package ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ravinder on 12/24/16.
 */

public class CommentModel {

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("userID")
    @Expose
    private int userID;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("userpic")
    @Expose
    private String userpic;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("commentID")
    @Expose
    private int commentID;
    @SerializedName("discussionID")
    @Expose
    private int discussionID;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("loveNum")
    @Expose
    private int loveNum;
    @SerializedName("isClicked")
    @Expose
    private int isClicked = 0;

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public int getIsClicked() {
        return isClicked;
    }

    public void setIsClicked(int isClicked) {
        this.isClicked = isClicked;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLoveNum() {
        return loveNum;
    }

    public void setLoveNum(int loveNum) {
        this.loveNum = loveNum;
    }

    public long getTime() {
        return time;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDiscussionID() {
        return discussionID;
    }

    public void setDiscussionID(int discussionID) {
        this.discussionID = discussionID;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
