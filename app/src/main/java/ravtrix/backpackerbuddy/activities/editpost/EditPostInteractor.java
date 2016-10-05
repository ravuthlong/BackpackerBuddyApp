package ravtrix.backpackerbuddy.activities.editpost;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.RetrofitUserCountrySingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/14/16.
 */
public class EditPostInteractor implements IEditPostInteractor {

    public EditPostInteractor() {}


    @Override
    public void editPostRetrofit(HashMap<String, String> travelSpotHash,
                                 final OnRetrofitEditPostListener onRetrofitEditPostListener) {
        Call<JsonObject> objectCall = RetrofitUserCountrySingleton.getRetrofitUserCountry()
                .updateTravelSpot().updateTravelSpot(travelSpotHash);

        objectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 1) {
                    onRetrofitEditPostListener.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onRetrofitEditPostListener.onError();
            }
        });
    }
}
