package ravtrix.backpackerbuddy.helpers;

import ravtrix.backpackerbuddy.retrofit.retrofitrequests.RetrofitUserChat;

/**
 * Created by Ravinder on 9/23/16.
 */

public class RetrofitUserChatSingleton {
    private static RetrofitUserChat retrofitUserChat = new RetrofitUserChat();

    public static RetrofitUserChat getRetrofitUserChat() {
        return retrofitUserChat;
    }
}
