package ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Linh on 2/19/2016.
 */

public class FeedItem {
    @SerializedName("travelID")
    @Expose
    private int id;
    @SerializedName("userID")
    @Expose
    private int userID;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("from")
    @Expose
    private String fromDate;
    @SerializedName("until")
    @Expose
    private String toDate;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("clicked")
    @Expose
    private int clicked;
    private boolean isFavorite;
    @SerializedName("success")
    @Expose
    private int success;

    public int isSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public FeedItem() {
    }

    public FeedItem(int userID, int id,
                    String username, String country, String fromDate, String toDate) {
        this.id = id;
        this.country = country;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.userID = userID;
        this.username = username;

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
