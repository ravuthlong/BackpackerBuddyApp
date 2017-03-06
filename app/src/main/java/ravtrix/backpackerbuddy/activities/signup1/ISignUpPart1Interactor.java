package ravtrix.backpackerbuddy.activities.signup1;

import java.util.HashMap;

/**
 * Created by Ravinder on 9/28/16.
 */

interface ISignUpPart1Interactor {

    void isUsernameTaken(String username, String email, OnRetrofitSignUp1 onRetrofitSignUp1);
    void signUserUpRetrofit(HashMap<String, String> userInfo, OnRetrofitSignUp1SuccessFail onRetrofitSignUp1);
    void updateCountry(String username, String country, OnRetrofitSignUp1SuccessFail onRetrofitSignUp1);
}
