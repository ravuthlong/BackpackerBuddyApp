package ravtrix.backpackerbuddy.retrofit.retrofitrequests.retrofituserinforequests;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.userinfointerfaces.RetrofitUserInfoInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 8/17/16.
 */
public class RetrofitUserInfo {

    private Retrofit retrofit;

    public RetrofitUserInfo() {
        retrofit = Helpers.retrofitBuilder(this.retrofit, Helpers.ServerURL.SERVER_URL);
    }

    public RetrofitUserInfoInterfaces.LogUserIn loggedInUser() {
        return this.retrofit.create(RetrofitUserInfoInterfaces.LogUserIn.class);
    }

    public RetrofitUserInfoInterfaces.SignUserUpPart1 signUserUpPart1() {
        return this.retrofit.create(RetrofitUserInfoInterfaces.SignUserUpPart1.class);
    }

    public RetrofitUserInfoInterfaces.SignUserUpPart2 signUserUpPart2() {
        return this.retrofit.create(RetrofitUserInfoInterfaces.SignUserUpPart2.class);
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
}
