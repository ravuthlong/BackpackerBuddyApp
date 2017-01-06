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
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("traveling")
    @Expose
    private int traveling;
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


    public LoggedInUser() {}

    public LoggedInUser(int userID, String email, String username, String userImageURL, String gender, int traveling,
                        int bucketStatus, double latitude, double longitude, long currentTime) {
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.userpic = userImageURL;
        this.gender = gender;
        this.traveling = traveling;
        this.bucketStatus = bucketStatus;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = currentTime;
    }

    public int getBucketStatus() {
        return bucketStatus;
    }

    public void setBucketStatus(int bucketStatus) {
        this.bucketStatus = bucketStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getTraveling() {
        return traveling;
    }

    public void setTraveling(int traveling) {
        this.traveling = traveling;
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
}
