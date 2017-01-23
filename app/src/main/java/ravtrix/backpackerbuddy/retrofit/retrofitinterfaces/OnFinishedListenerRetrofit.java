package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

/**
 * Created by Ravinder on 1/10/17.
 */

public interface OnFinishedListenerRetrofit {

    void onFinished(JsonObject jsonObject);
    void onError();
}
