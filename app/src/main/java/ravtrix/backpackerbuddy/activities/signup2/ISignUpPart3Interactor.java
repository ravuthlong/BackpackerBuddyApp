package ravtrix.backpackerbuddy.activities.signup2;

import java.util.HashMap;

/**
 * Created by Ravinder on 9/28/16.
 */

interface ISignUpPart3Interactor {

    void signUserUpRetrofit(HashMap<String, String> userInfo, OnRetrofitSignUp3 onRetrofitSignUp3);
    void updateCountry(String username, String country, OnRetrofitSignUp3 onRetrofitSignUp3);
}
