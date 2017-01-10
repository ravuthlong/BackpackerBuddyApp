package ravtrix.backpackerbuddy.fragments.userprofile;

import java.util.HashMap;

import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 9/14/16.
 */
class UserProfilePresenter implements  IUserProfilePresenter {

    private IUserProfileView view;
    private UserProfileInteractor userProfileInteractor;

    UserProfilePresenter(IUserProfileView view) {
        this.view = view;
        userProfileInteractor = new UserProfileInteractor();
    }

    @Override
    public void updateTravelStatus(HashMap<String, String> userInfo, UserLocalStore userLocalStore) {
        userProfileInteractor.updateTravelStatus(userInfo, userLocalStore, new RetrofitTravelListener() {
            @Override
            public void onTravel() {
                // User is now traveling
                view.hideImageNotTravel();
                view.hideTextNotTravel();
                view.showImageTravel();
                view.showTextTravel();
            }

            @Override
            public void onNotTravel() {
                // User is not traveling
                view.showImageNotTravel();
                view.showTextNotTravel();
                view.hideImageTravel();
                view.hideTextTravel();
            }

            @Override
            public void onError() {
                view.hideProgressDialog();
                view.displayError();
            }

            @Override
            public void onHideProgressDialog() {
                view.hideProgressDialog();
            }
        });
    }

    @Override
    public void getUserInfo(int userID, String userImgURL) {
        userProfileInteractor.getUserIntoRetrofit(userID, userImgURL, new RetrofitProfileListener() {

            @Override
            public void onError() {
            }

            @Override
            public void onSetUsername(String username) {
                view.setUsername(username);
            }

            @Override
            public void onSetDetailOneText(String text) {
                view.setDetailOneText(text);
            }

            @Override
            public void onSetDetailOneHint(String hint) {
                view.setDetailOneHint(hint);
            }

            @Override
            public void onSetDetailTwoText(String text) {
                view.setDetailTwoText(text);
            }

            @Override
            public void onSetDetailTwoHint(String hint) {
                view.setDetailTwoHint(hint);
            }

            @Override
            public void onSetDetailThreeText(String text) {
                view.setDetailThreeText(text);
            }

            @Override
            public void onSetDetailThreeHint(String hint) {
                view.setDetailThreeHint(hint);
            }

            @Override
            public void onSetDetailFourText(String text) {
                view.setDetailFourText(text);
            }

            @Override
            public void onSetDetailFourHint(String hint) {
                view.setDetailFourHint(hint);
            }

            @Override
            public void onSetDetailOneColor() {
                view.setDetailOneColor();
            }

            @Override
            public void onSetDetailTwoColor() {
                view.setDetailTwoColor();
            }

            @Override
            public void onSetDetailThreeColor() {
                view.setDetailThreeColor();
            }

            @Override
            public void onSetDetailFourColor() {
                view.setDetailFourColor();
            }

            @Override
            public void onDetailOneAHint(boolean hint) {
                view.isDetailOneAHint(hint);
            }

            @Override
            public void onDetailTwoAHint(boolean hint) {
                view.isDetailTwoAHint(hint);
            }

            @Override
            public void onDetailThreeAHint(boolean hint) {
                view.isDetailThreeAHint(hint);
            }

            @Override
            public void onDetailFourAHint(boolean hint) {
                view.isDetailFourAHint(hint);
            }

            @Override
            public void onSetTravelStatus(int status) {
                if (status == 0) { // not traveling
                    view.showImageNotTravel();
                    view.showTextNotTravel();
                } else {
                    view.showImageTravel();
                    view.showTextTravel();
                }
            }

        });
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
