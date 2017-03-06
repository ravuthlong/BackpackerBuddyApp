package ravtrix.backpackerbuddy.activities.signup1;

import java.util.HashMap;

/**
 * Created by Ravinder on 9/27/16.
 */

 interface ISignUpPart1Presenter {
    void inputValidation(boolean isPhotoUploaded, String username, String password, String email, String etConfirmPassword);
    void retrofitStoreUser(HashMap<String, String> userInfo);
    void updateCountry(String username, String country);
}
