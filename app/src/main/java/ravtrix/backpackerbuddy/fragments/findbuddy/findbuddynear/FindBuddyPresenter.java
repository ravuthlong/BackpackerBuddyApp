package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

import java.util.List;

import ravtrix.backpackerbuddy.models.UserLocationInfo;

/**
 * Created by Ravinder on 10/4/16.
 */

class FindBuddyPresenter implements IFindBuddyNearPresenter {

    private IFindBuddyNearView view;
    private FindBuddyNearInteractor findBuddyNearInteractor;

    FindBuddyPresenter(IFindBuddyNearView view) {
        this.view = view;
        findBuddyNearInteractor = new FindBuddyNearInteractor();
    }

    @Override
    public void fetchBuddyNearRetrofit(int userID, int distance) {
        findBuddyNearInteractor.fetchNearbyUsersRetrofit(userID, distance, new OnFindBuddyNearListener() {

            @Override
            public void onSuccess(List<UserLocationInfo> userList) {
                view.setCustomGridView(userList);
                view.setCityText();
            }

            @Override
            public void onFailure() {
                view.hideProgressbar();
                view.setViewVisible();
                view.showErrorToast();
            }
        });
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
