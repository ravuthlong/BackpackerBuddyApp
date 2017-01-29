package ravtrix.backpackerbuddy.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.chat.ConversationActivity;
import ravtrix.backpackerbuddy.activities.discussion.discussioncomments.DiscussionComments;

/**
 * Created by Ravinder on 9/21/16.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "MyFMService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
            Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody(), "0", "");
        } else {
            // From server php

            if (remoteMessage.getData().get("type").equals("0")) {
                sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("senderID"),
                        remoteMessage.getData().get("senderImage"));
            } else {
                sendNotificationComment(remoteMessage.getData().get("message"),
                        Integer.parseInt(remoteMessage.getData().get("discussionID")),
                        Integer.parseInt(remoteMessage.getData().get("ownerID")));
            }
        }
    }

    private void sendNotification(String body, String senderID, String senderImage) {

        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra("otherUserID", senderID);
        intent.putExtra("otherUserImage", senderImage);
        intent.putExtra("backpressExit", 0);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // 0 is request code
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Set notification sound
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("New Message")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notifBuilder.build()); // 0 id of notification
    }

    private void sendNotificationComment(String body, int discussionID, int ownerID) {
        Intent intent = new Intent(this, DiscussionComments.class);
        intent.putExtra("discussionID", discussionID);
        intent.putExtra("ownerID", ownerID);
        intent.putExtra("backpressExit", 0);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // 1 is request code
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        // Set notification sound
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("New Comment")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notifBuilder.build()); // 1 id of notification
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }
}
