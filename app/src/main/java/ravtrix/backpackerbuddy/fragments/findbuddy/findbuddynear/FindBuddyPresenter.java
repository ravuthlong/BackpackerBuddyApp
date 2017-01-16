package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

import android.content.Context;

import java.util.List;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.models.UserLocationInfo;

/**
 * Created by Ravinder on 10/4/16.
 */

class FindBuddyPresenter implements IFindBuddyNearPresenter {

    private IFindBuddyNearView view;
    private FindBuddyNearInteractor findBuddyNearInteractor;
    private Context context;
    private UserLocalStore userLocalStore;

    FindBuddyPresenter(IFindBuddyNearView view, Context context, UserLocalStore userLocalStore) {
        this.view = view;
        this.context = context;
        this.userLocalStore = userLocalStore;
        findBuddyNearInteractor = new FindBuddyNearInteractor();
    }

    @Override
    public void fetchBuddyNearRetrofit(int userID, int distance) {
        view.setViewInvisible();
        findBuddyNearInteractor.fetchNearbyUsersRetrofit(userID, distance, new OnFindBuddyNearListener() {

            @Override
            public void onSuccess(List<UserLocationInfo> userList) {

                if (userList.get(0).getSuccess() == 0) {
                    view.setNoNearbyVisible();
                } else {
                    view.hideNoNearby();
                    view.setCustomGridView(userList);
                }
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
    public void fetchBuddyNearGuestRetrofit(final int radius) {
        view.setViewInvisible();
        // User is a guest. They don't have a longitude and latitude in localstore, fetch it
        Helpers.fetchLatAndLong(context, userLocalStore, new OnLocationReceivedGuest() {
            @Override
            public void onLocationReceivedGuest(String longitude, String latitude) {
                findBuddyNearInteractor.fetchNearbyUsersGuestRetrofit(latitude, longitude, radius, new OnFindBuddyNearListener() {

                    @Override
                    public void onSuccess(List<UserLocationInfo> userList) {
                        if (userList.get(0).getSuccess() == 0) {
                            view.setNoNearbyVisible();
                        } else {
                            view.hideNoNearby();
                            view.setCustomGridView(userList);
                        }
                        view.setCityText();
                        view.setViewVisible();
                        view.hideProgressbar();
                    }

                    @Override
                    public void onFailure() {
                        view.hideProgressbar();
                        view.setViewVisible();
                        view.showErrorToast();
                    }
                });


            }
        });
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
