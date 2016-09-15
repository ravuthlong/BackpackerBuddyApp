package ravtrix.backpackerbuddy.activities.editinfo;

/**
 * Created by Ravinder on 9/14/16.
 */
public interface IEditInfoView {

    void displayErrorToast();
    void finishActivity();
    void setTitle(String text);
    void setText(String text);
    void setHint(String text);
}
