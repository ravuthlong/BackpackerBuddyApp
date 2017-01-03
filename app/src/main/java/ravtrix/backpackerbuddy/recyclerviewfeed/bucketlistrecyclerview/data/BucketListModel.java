package ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ravinder on 1/1/17.
 */

public class BucketListModel {

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("bucketID")
    @Expose
    private int bucketID;
    @SerializedName("userID")
    @Expose
    private int userID;
    @SerializedName("post")
    @Expose
    private String post;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("status")
    @Expose
    private int status;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getBucketID() {
        return bucketID;
    }

    public void setBucketID(int bucketID) {
        this.bucketID = bucketID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
