package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

import java.util.List;

import ravtrix.backpackerbuddy.models.UserLocationInfo;

/**
 * Created by Ravinder on 10/4/16.
 */

interface OnFindBuddyNearListener {
    void onFailure();
    void onSuccess(List<UserLocationInfo> userList);
}
