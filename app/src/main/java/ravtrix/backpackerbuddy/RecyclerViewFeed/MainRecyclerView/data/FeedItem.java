package ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.data;

/**
 * Created by Linh on 2/19/2016.
 */

public class FeedItem {
    private int id, userID;
    private String country, fromDate, toDate, name, username;

    public FeedItem() {
    }

    public FeedItem(int userID, int id, String name, String username, String country, String fromDate, String toDate) {
        this.id = id;
        this.country = country;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.userID = userID;
        this.username = username;
        this.name = name;

    }

    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
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
    public String getName() { return name; }
    public String getUsername() { return username; }
}
