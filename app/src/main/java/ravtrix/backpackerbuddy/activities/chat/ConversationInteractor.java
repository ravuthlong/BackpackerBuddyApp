package ravtrix.backpackerbuddy.activities.chat;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.helpers.RetrofitUserChatSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 11/22/16.
 */

class ConversationInteractor implements IConversationInteractor {

    @Override
    public void notifyOtherUser(String message, String otherUserID, String username, int userID) {
        Call<JsonObject> retrofit = RetrofitUserChatSingleton.getRetrofitUserChat()
                .sendNotification()
                .sendNotification(Integer.parseInt(otherUserID),
                        username + ": " + message, userID);
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {}
        });
    }
}
