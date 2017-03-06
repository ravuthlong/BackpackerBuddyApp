package ravtrix.backpackerbuddy.activities.signup1;

import com.google.gson.JsonObject;

/**
 * Created by Ravinder on 2/25/17.
 */

public interface OnRetrofitSignUp1SuccessFail {
    void onSuccess(JsonObject jsonObject);
    void onFailure();
}
