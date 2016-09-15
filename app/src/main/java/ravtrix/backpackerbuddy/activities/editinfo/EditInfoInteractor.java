package ravtrix.backpackerbuddy.activities.editinfo;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/14/16.
 */
public class EditInfoInteractor implements IEditInfoInteractor {

    public EditInfoInteractor() {
    }

    @Override
    public boolean isStringEmpty(String text) {
        return text.isEmpty();
    }

    @Override
    public void editUserInfoRetrofit(String newPost, String editType, UserLocalStore userLocalStore,
                                final OnRetrofitCompleteListener onRetrofitCompleteListener) {
        // User information and detail to update in the database
        HashMap<String, String> userDetail = new HashMap<>();
        userDetail.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        userDetail.put("detail", newPost);
        userDetail.put("detailType", editType);

        Call<JsonObject> updateDetail = RetrofitUserInfoSingleton.getRetrofitUserInfo().updateUserDetail().updateUserDetail(userDetail);
        updateDetail.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject returnedJSON = response.body();

                // Success update. Return user to user profile activity
                if (returnedJSON.get("status").getAsInt() == 1) {
                    onRetrofitCompleteListener.onSuccess();
                } else {
                    onRetrofitCompleteListener.onError();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                 onRetrofitCompleteListener.onError();
            }
        });

    }
}
