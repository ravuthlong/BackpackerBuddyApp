package ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.data;

/**
 * Created by Linh on 2/19/2016.
 */

public class FeedItem {
    private int id;
    private String country, fromDate, toDate;

    public FeedItem() {
    }

    public FeedItem(int id, String country, String fromDate, String toDate) {
        this.id = id;
        this.country = country;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
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
}
