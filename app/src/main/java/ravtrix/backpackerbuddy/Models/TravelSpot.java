package ravtrix.backpackerbuddy.models;

/**
 * Created by Ravinder on 4/1/16.
 */
public class TravelSpot {
    private int userID;
    private String country, from, to;

    public TravelSpot(int userID, String country, String from, String to) {
        this.userID = userID;
        this.country = country;
        this.from = from;
        this.to = to;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setFromDate(String from) {
        this.from = from;
    }public void setToDate(String to) {
        this.to = to;
    }
    public int getUserID() { return userID; }
    public String getCountry() { return country; }
    public String getFromDate() { return from; }
    public String getToDate() { return to; }

}
