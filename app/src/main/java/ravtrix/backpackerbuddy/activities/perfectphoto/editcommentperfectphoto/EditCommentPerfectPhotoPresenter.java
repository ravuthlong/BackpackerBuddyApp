package ravtrix.backpackerbuddy.activities.perfectphoto.editcommentperfectphoto;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

public class EditCommentPerfectPhotoPresenter implements IEditCommentPerfectPhotoPresenter {

    private IEditCommentPerfectPhotoView iEditCommentPerfectPhotoView;
    private EditCommentPerfectPhotoInteractor editCommentPerfectPhotoInteractor;
    private static final int REQUEST_REFRESH = 1;

    EditCommentPerfectPhotoPresenter(IEditCommentPerfectPhotoView iEditCommentPerfectPhotoView) {
        this.iEditCommentPerfectPhotoView = iEditCommentPerfectPhotoView;
        this.editCommentPerfectPhotoInteractor = new EditCommentPerfectPhotoInteractor();
    }

    @Override
    public void updateComment(int commentID, String newComment) {

        editCommentPerfectPhotoInteractor.updateCommentRetrofit(commentID, newComment, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 1) {
                    // Hide soft keyboard
                    iEditCommentPerfectPhotoView.hideKeyboard();
                    iEditCommentPerfectPhotoView.setResultCode(REQUEST_REFRESH);
                    iEditCommentPerfectPhotoView.finished();
                } else {
                    iEditCommentPerfectPhotoView.showErrorToast();
                }
            }

            @Override
            public void onError() {
                iEditCommentPerfectPhotoView.showErrorToast();
            }
        });
    }
}
