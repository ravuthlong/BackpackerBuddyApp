package ravtrix.backpackerbuddy.activities.editinfo;

/**
 * Created by Ravinder on 9/14/16.
 */
public interface IEditPresenter {

    boolean checkEmptyString(String text);
    void editUserInfo(String newPost, String editType);
    boolean isHint();
    void setTextFields();
    String getDetailType();
    void onDestroy();

}
