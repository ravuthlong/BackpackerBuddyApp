package ravtrix.backpackerbuddy.activities.perfectphoto.postperfectphoto;

/**
 * Created by Ravinder on 1/24/17.
 */

public interface IPostPerfectPhotoView {
    void showProgressBar();
    void hideProgressBar();
    void setResultCode(int code);
    void finished();
    void showErrorToast();
}
