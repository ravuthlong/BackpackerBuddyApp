package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

/**
 * Created by Ravinder on 10/4/16.
 */

interface IFindBuddyNearPresenter {

    void fetchBuddyNearRetrofit(int userID, int distance);
    void onDestroy();
}
