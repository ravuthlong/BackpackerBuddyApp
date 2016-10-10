package ravtrix.backpackerbuddy.fragments.userprofile;

/**
 * Created by Ravinder on 9/14/16.
 */
interface RetrofitProfileListener {

    void onError();
    void onSetUsername(String username);
    void onSetDetailOneText(String text);
    void onSetDetailOneHint(String hint);
    void onSetDetailTwoText(String text);
    void onSetDetailTwoHint(String hint);
    void onSetDetailThreeText(String text);
    void onSetDetailThreeHint(String hint);
    void onSetDetailFourText(String text);
    void onSetDetailFourHint(String hint);
    void onSetProfilePic(String pic);
    void onSetDetailOneColor();
    void onSetDetailTwoColor();
    void onSetDetailThreeColor();
    void onSetDetailFourColor();
    void onDetailOneAHint(boolean hint);
    void onDetailTwoAHint(boolean hint);
    void onDetailThreeAHint(boolean hint);
    void onDetailFourAHint(boolean hint);

}
