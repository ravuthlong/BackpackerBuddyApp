package ravtrix.backpackerbuddy.retrofit.retrofitrequests.retrofituserinforequests;
import ravtrix.backpackerbuddy.helper.Helper;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.userinfointerfaces.RetrofitUserInfoInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 8/17/16.
 */
public class RetrofitUserInfo {

    private Retrofit retrofit;

    public RetrofitUserInfo() {
        retrofit = Helper.retrofitBuilder(this.retrofit, Helper.ServerURL.SERVER_URL);
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
}
