package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddyrecentlyonline;

import java.util.List;

import ravtrix.backpackerbuddy.models.UserLocationInfo;

/**
 * Created by Ravinder on 1/26/17.
 */

interface IFindBuddyRecentlyView {

    void setOnlineUserModels(List<UserLocationInfo> userModels);
    void setGridView();
    void showProgressBar();
    void hideProgressBar();
    void showGridView();
    void hideGridView();


}
