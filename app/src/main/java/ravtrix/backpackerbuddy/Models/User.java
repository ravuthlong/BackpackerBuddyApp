package ravtrix.backpackerbuddy.models;

/**
 * Created by Ravinder on 3/28/16.
 */
public class User {
    private String email;
    private String username;
    private String password;
    private String userImageURL;
    private int userID;

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    // User for signing up 1
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
    // User for current user storage
    public User(int userID, String email, String username, String userImageURL) {
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.userImageURL = userImageURL;
    }
    // User for logging in
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Retrieve the data field of the class
    public String getEmail() {
        return email;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public int getUserID() {
        return userID;
    }


   public void setEmail(String email) {
        this.email = email;
    } public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void serUserID(int userID) {
        this.userID = userID;
    }

}
