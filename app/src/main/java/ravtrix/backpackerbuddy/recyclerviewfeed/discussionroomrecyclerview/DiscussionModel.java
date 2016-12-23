package ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ravinder on 12/21/16.
 */

public class DiscussionModel {

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("userID")
    @Expose
    private int userID;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("discussionID")
    @Expose
    private int discussionID;
    @SerializedName("post")
    @Expose
    private String post;
    @SerializedName("time")
    @Expose
    private long time;
    private int totalLikes;
    private int totalComments;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String text) {
        this.post = post;
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
