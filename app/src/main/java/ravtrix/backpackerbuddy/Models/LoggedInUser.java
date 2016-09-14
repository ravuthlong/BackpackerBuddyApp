package ravtrix.backpackerbuddy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ravinder on 8/17/16.
 */

public class LoggedInUser {

    @SerializedName("userID")
    @Expose
    private int userID;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("userpic")
    @Expose
    private String userpic;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;

    public LoggedInUser() {}

    public LoggedInUser(int userID, String email, String username, String userImageURL,
                        double latitude, double longitude) {
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.userpic = userImageURL;
        this.latitude = latitude;
        this.longitude = longitude;

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
