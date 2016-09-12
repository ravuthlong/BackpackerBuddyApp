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
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("clicked")
    @Expose
    private int clicked;
    private boolean isFavorite;


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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public FeedItem() {
    }

    public FeedItem(int userID, int id, String firstname, String lastname,
                    String username, String country, String fromDate, String toDate) {
        this.id = id;
        this.country = country;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.userID = userID;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;

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
