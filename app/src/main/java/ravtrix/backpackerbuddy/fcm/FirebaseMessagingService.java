package ravtrix.backpackerbuddy.fcm;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Ravinder on 9/21/16.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "MyFMService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
    }
}
