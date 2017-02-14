package ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data;

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
    @SerializedName("userpic")
    @Expose
    private String userpic;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("countryTag")
    @Expose
    private String countryTag;
    @SerializedName("discussionID")
    @Expose
    private int discussionID;
    @SerializedName("post")
    @Expose
    private String post;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("loveNum")
    @Expose
    private int loveNum;
    @SerializedName("commentNum")
    @Expose
    private int commentNum;
    @SerializedName("isClicked")
    @Expose
    private int isClicked = 0;
    private int tagColor;

    public int getTagColor() {
        return tagColor;
    }

    public void setTagColor(int tagColor) {
        this.tagColor = tagColor;
    }

    public String getCountryTag() {
        return countryTag;
    }

    public void setCountryTag(String countryTag) {
        this.countryTag = countryTag;
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

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
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

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }
}
