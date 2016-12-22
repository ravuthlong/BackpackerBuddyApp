package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

import java.util.List;

import ravtrix.backpackerbuddy.models.UserLocationInfo;

/**
 * Created by Ravinder on 10/4/16.
 */

interface IFindBuddyNearView {

    void setCustomGridView(List<UserLocationInfo> userList);
    void setCityText();
    void showErrorToast();
    void setViewVisible();
    void setViewInvisible();
    void hideProgressbar();
    void setNoNearbyVisible();
    void hideNoNearby();
}
