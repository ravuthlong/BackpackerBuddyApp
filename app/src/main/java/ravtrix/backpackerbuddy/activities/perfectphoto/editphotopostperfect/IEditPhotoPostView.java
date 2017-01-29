package ravtrix.backpackerbuddy.activities.perfectphoto.editphotopostperfect;

/**
 * Created by Ravinder on 1/24/17.
 */

interface IEditPhotoPostView {

    void hideKeyboard();
    void setResultCode(int code);
    void finished();
    void showErrorToast();
}
