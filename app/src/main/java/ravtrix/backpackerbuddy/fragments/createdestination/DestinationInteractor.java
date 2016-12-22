package ravtrix.backpackerbuddy.fragments.createdestination;

import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.RetrofitUserCountrySingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 10/4/16.
 */

class DestinationInteractor implements IDestinationInteractor {

    @Override
    public void insertTravelSpotRetrofit(HashMap<String, String> travelSpot,
                                         final OnRetrofitDestinationListener onRetrofitDestinationListener) {

        Call<JsonObject> insertSpot = RetrofitUserCountrySingleton.getRetrofitUserCountry()
                                                                    .insertTravelSpot()
                                                                    .travelSpot(travelSpot);
        insertSpot.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject status = response.body();
                if (status.get("status").getAsInt() == 1) {
                    onRetrofitDestinationListener.onSuccess();
                } else {
                    onRetrofitDestinationListener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onRetrofitDestinationListener.onFailure();
            }
        });
    }

    @Override
    public boolean isDateValid(Calendar from, Calendar to) {

        Date dateFrom = from.getTime();
        Date dateUntil = to.getTime();

        return dateUntil.after(dateFrom);
    }
}
