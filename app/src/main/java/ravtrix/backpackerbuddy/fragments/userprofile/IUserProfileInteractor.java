package ravtrix.backpackerbuddy.fragments.userprofile;

/**
 * Created by Ravinder on 9/14/16.
 */
public interface IUserProfileInteractor {

    void getUserIntoRetrofit(int userID, String userImageURL, RetrofitProfileListener retrofitProfileListener);
}