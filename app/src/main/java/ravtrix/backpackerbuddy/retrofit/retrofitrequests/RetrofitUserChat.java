package ravtrix.backpackerbuddy.retrofit.retrofitrequests;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.RetrofitUserChatInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 9/23/16.
 */

public class RetrofitUserChat {
    private Retrofit retrofit;

    public RetrofitUserChat() {
        retrofit = Helpers.retrofitBuilder(this.retrofit, Helpers.ServerURL.SERVER_URL);
    }

    public RetrofitUserChatInterfaces.InsertNewChat insertNewChat() {
        return retrofit.create(RetrofitUserChatInterfaces.InsertNewChat.class);
    }

}
