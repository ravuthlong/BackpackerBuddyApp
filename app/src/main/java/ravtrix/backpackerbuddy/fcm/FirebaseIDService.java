package ravtrix.backpackerbuddy.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Ravinder on 9/21/16.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static final String FRIENDLY_ENGAGE_TOPIC = "friendly_engage";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(token);
        // register this token
    }

    private void sendRegistrationToServer(String token) {
        // If you need to handle the generation of a token, initially or
        // after a refresh this is where you should do that.
        Log.d(TAG, "FCM Token: " + token);
    }
}
