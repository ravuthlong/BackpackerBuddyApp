package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

/**
 * Created by Ravinder on 10/4/16.
 */

interface IFindBuddyNearInteractor {

    void fetchNearbyUsersRetrofit(int userID, int distance, OnFindBuddyNearListener onFindBuddyNearListener);
}
