package ravtrix.backpackerbuddy.recyclerviewfeed.perfectphotorecyclerview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ravinder on 1/12/17.
 */

public class PerfectPhotoModel {

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
    @SerializedName("perfectPhotoID")
    @Expose
    private int perfectPhotoID;
    @SerializedName("path")
    @Expose
    private String path;
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

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPerfectPhotoID() {
        return perfectPhotoID;
    }

    public void setPerfectPhotoID(int perfectPhotoID) {
        this.perfectPhotoID = perfectPhotoID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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
}
