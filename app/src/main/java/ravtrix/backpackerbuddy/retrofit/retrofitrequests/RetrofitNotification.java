package ravtrix.backpackerbuddy.retrofit.retrofitrequests;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.RetrofitNotificationInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 1/10/17.
 */

public class RetrofitNotification {

    private Retrofit retrofit;

    public RetrofitNotification() {
        retrofit = Helpers.retrofitBuilder(Helpers.ServerURL.SERVER_URL);
    }

    public RetrofitNotificationInterfaces.GetNotificationStatus getNotificationStatus() {
        return this.retrofit.create(RetrofitNotificationInterfaces.GetNotificationStatus.class);
    }

    public RetrofitNotificationInterfaces.UpdateCommentNotification updateCommentNotification() {
        return this.retrofit.create(RetrofitNotificationInterfaces.UpdateCommentNotification.class);
    }

    public RetrofitNotificationInterfaces.UpdateMessageNotification updateMessageNotification() {
        return this.retrofit.create(RetrofitNotificationInterfaces.UpdateMessageNotification.class);
    }
}
