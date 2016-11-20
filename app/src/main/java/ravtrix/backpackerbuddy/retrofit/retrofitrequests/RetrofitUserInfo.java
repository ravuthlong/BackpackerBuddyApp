package ravtrix.backpackerbuddy.retrofit.retrofitrequests;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.RetrofitUserInfoInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 8/17/16.
 */
public class RetrofitUserInfo {

    private Retrofit retrofit;

    public RetrofitUserInfo() {
        retrofit = Helpers.retrofitBuilder(Helpers.ServerURL.SERVER_URL);
    }

    public RetrofitUserInfoInterfaces.LogUserIn loggedInUser() {
        return this.retrofit.create(RetrofitUserInfoInterfaces.LogUserIn.class);
    }

    public RetrofitUserInfoInterfaces.SignUserUpPart1 signUserUpPart1() {
        return this.retrofit.create(RetrofitUserInfoInterfaces.SignUserUpPart1.class);
    }

    public RetrofitUserInfoInterfaces.GetUserDetails getUserDetails() {
        return retrofit.create(RetrofitUserInfoInterfaces.GetUserDetails.class);
    }

    public RetrofitUserInfoInterfaces.UpdateUserDetail updateUserDetail() {
        return this.retrofit.create(RetrofitUserInfoInterfaces.UpdateUserDetail.class);
    }

    public RetrofitUserInfoInterfaces.UpdateProfilePic updateProfilePic() {
        return retrofit.create(RetrofitUserInfoInterfaces.UpdateProfilePic.class);
    }

    public RetrofitUserInfoInterfaces.UpdateLocation updateLocation() {
        return retrofit.create(RetrofitUserInfoInterfaces.UpdateLocation.class);
    }

    public RetrofitUserInfoInterfaces.GetNearbyUsers getNearbyUsers() {
        return retrofit.create(RetrofitUserInfoInterfaces.GetNearbyUsers.class);
    }

    public RetrofitUserInfoInterfaces.GetRecentlyOnlineUsers getRecentlyOnlineUsers() {
        return retrofit.create(RetrofitUserInfoInterfaces.GetRecentlyOnlineUsers.class);
    }

    public RetrofitUserInfoInterfaces.IsUsernameTaken isUsernameTaken() {
        return retrofit.create(RetrofitUserInfoInterfaces.IsUsernameTaken.class);
    }

    public RetrofitUserInfoInterfaces.ChangePassword changePassword() {
        return retrofit.create(RetrofitUserInfoInterfaces.ChangePassword.class);
    }

    public RetrofitUserInfoInterfaces.ChangeEmail changeEmail() {
        return retrofit.create(RetrofitUserInfoInterfaces.ChangeEmail.class);
    }

    public RetrofitUserInfoInterfaces.InsertNotificationToken insertNotificationToken() {
        return retrofit.create(RetrofitUserInfoInterfaces.InsertNotificationToken.class);
    }
}
