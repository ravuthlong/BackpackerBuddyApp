package ravtrix.backpackerbuddy.activities.notificationsettings;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.helpers.RetrofitNotificationSingleton;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/22/17.
 */

class NotificationInteractor implements INotificationInteractor {


    @Override
    public void fetchNotificationStatus(int userID, final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {
        Call<JsonObject> retrofit = RetrofitNotificationSingleton.getRetrofitNotification()
                .getNotificationStatus()
                .getNotificationStatus(userID);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                onFinishedListenerRetrofit.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onFinishedListenerRetrofit.onError();
            }
        });
    }

    @Override
    public void updateCommentNotif(int userID, final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {
        Call<JsonObject> retrofit = RetrofitNotificationSingleton.getRetrofitNotification()
                .updateCommentNotification()
                .updateCommentNotification(userID);
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                onFinishedListenerRetrofit.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onFinishedListenerRetrofit.onError();
            }
        });
    }

    @Override
    public void updateMessageNotif(int userID, final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {
        Call<JsonObject> retrofit = RetrofitNotificationSingleton.getRetrofitNotification()
                .updateMessageNotification()
                .updateMessageNotification(userID);
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                onFinishedListenerRetrofit.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onFinishedListenerRetrofit.onError();
            }
        });
    }
}
