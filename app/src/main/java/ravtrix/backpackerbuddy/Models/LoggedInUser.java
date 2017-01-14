package ravtrix.backpackerbuddy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ravinder on 8/17/16.
 */

public class LoggedInUser {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("userID")
    @Expose
    private int userID;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("userpic")
    @Expose
    private String userpic;
    @SerializedName("bucketStatus")
    @Expose
    private int bucketStatus;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("time")
    @Expose
    private Long time;
    @SerializedName("isFacebook")
    @Expose
    private int isFacebook;

    public LoggedInUser() {}

    public LoggedInUser(int userID, String email, String username, String userImageURL,
                        int bucketStatus, double latitude, double longitude, long currentTime,
                        int isFacebook) {
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.userpic = userImageURL;
        this.bucketStatus = bucketStatus;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = currentTime;
        this.isFacebook = isFacebook;
    }

    public int getBucketStatus() {
        return bucketStatus;
    }

    public void setBucketStatus(int bucketStatus) {
        this.bucketStatus = bucketStatus;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getUserImageURL() {
        return userpic;
    }

    public void setUserImageURL(String userpic) {
        this.userpic = userpic;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIsFacebook() {
        return isFacebook;
    }

    public void setIsFacebook(int isFacebook) {
        this.isFacebook = isFacebook;
    }
}
