package ravtrix.backpackerbuddy.activities.otheruserprofile;

import com.google.gson.JsonObject;

/**
 * Created by Ravinder on 9/27/16.
 */

interface OnRFOtherUserProfileListener {
    void onSuccess(JsonObject otherUserJSON);
    void onFailure();
}
