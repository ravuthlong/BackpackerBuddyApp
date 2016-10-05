package ravtrix.backpackerbuddy.activities.editphoto;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/28/16.
 */

public class EditPhotoInteractor {


    private void retrofitUploadProfileImg(int userID, String encodedImage,
                                          OnRetrofitEditPhotoListener onRetrofitEditPhotoListener) {

        HashMap<String, String> profileImageInfo = new HashMap<>();
        profileImageInfo.put("image", encodedImage);
        profileImageInfo.put("userID", Integer.toString(userID));

        Call<JsonObject> jsonObjectCall = RetrofitUserInfoSingleton
                .getRetrofitUserInfo()
                .updateProfilePic()
                .updateProfilePic(profileImageInfo);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 1) {

                    /*
                    LoggedInUser replaceUser = new LoggedInUser();
                    replaceUser.setUserID(userLocalStore.getLoggedInUser().getUserID());
                    replaceUser.setEmail(userLocalStore.getLoggedInUser().getEmail());
                    replaceUser.setUsername(userLocalStore.getLoggedInUser().getUsername());
                    replaceUser.setUserImageURL(response.body().get("userImageURL").getAsString());

                    userLocalStore.clearUserData();
                    userLocalStore.storeUserData(replaceUser);
                    */

                    // After uploaded
                    //isNewPhotoSet = false;
                    //Helpers.hideProgressDialog(progressDialog);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Helpers.displayToast(EditPhotoActivity.this, "Upload Error");
            }
        });
    }
}
