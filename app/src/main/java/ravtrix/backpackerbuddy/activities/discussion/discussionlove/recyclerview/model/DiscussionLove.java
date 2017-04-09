package ravtrix.backpackerbuddy.activities.discussion.discussionlove.recyclerview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ravinder on 4/8/17.
 */

public class DiscussionLove {

    @SerializedName("success")
    @Expose
    private int success;

    @SerializedName("userID")
    @Expose
    private int userID;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("profileImage")
    @Expose
    private String profileImage;

    @SerializedName("country")
    @Expose
    private String country;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
