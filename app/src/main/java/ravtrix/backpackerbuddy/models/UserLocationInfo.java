package ravtrix.backpackerbuddy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ravinder on 9/16/16.
 */
public class UserLocationInfo {

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("userID")
    @Expose
    private int userID;
    @SerializedName("distance")
    @Expose
    private double distance;

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    @SerializedName("userpic")
    @Expose
    private String userpic;

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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}
