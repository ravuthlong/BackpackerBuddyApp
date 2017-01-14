package ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Linh on 2/19/2016.
 */

public class FeedItem {

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
    @SerializedName("travelID")
    @Expose
    private int id;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("from")
    @Expose
    private String fromDate;
    @SerializedName("until")
    @Expose
    private String toDate;
    private boolean isFavorite;

    public int isSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public FeedItem() {
    }

    public int getSuccess() {
        return success;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public void setUsername(String username) { this.username = username; }
    public void setUserID(int userID) {this.userID = userID; }
    public void setId(int id) {
        this.id = id;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
    public int getId() {
        return id;
    }
    public String getCountry() {
        return country;
    }
    public String getToDate() {
        return toDate;
    }
    public String getFromDate() {
        return fromDate;
    }
    public int getUserID() { return userID; }
    public String getUsername() { return username; }
}
