package ravtrix.backpackerbuddy.activities.perfectphoto.editphotopostperfect;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

class EditPhotoPostPresenter implements IEditPhotoPostPresenter {

    private IEditPhotoPostView iEditPhotoPostView;
    private EditPhotoPostInteractor editPhotoPostInteractor;
    private static final int REQUEST_REFRESH = 1;

    EditPhotoPostPresenter(IEditPhotoPostView iEditPhotoPostView) {
        this.iEditPhotoPostView = iEditPhotoPostView;
        this.editPhotoPostInteractor = new EditPhotoPostInteractor();
    }

    @Override
    public void updatePhotoPost(int photoID, String post) {
        editPhotoPostInteractor.updatePhotoPostRetrofit(photoID, post, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 1) {

                    iEditPhotoPostView.hideKeyboard();
                    iEditPhotoPostView.setResultCode(REQUEST_REFRESH);
                    iEditPhotoPostView.finished();
                } else {
                    iEditPhotoPostView.showErrorToast();
                }
            }

            @Override
            public void onError() {
                iEditPhotoPostView.showErrorToast();
            }
        });
    }
}
