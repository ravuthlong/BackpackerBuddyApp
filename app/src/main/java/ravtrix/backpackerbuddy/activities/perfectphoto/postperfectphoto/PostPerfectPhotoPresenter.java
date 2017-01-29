package ravtrix.backpackerbuddy.activities.perfectphoto.postperfectphoto;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

public class PostPerfectPhotoPresenter implements IPostPerfectPhotoPresenter {

    private IPostPerfectPhotoView iPostPerfectPhotoView;
    private PostPerfectPhotoInteractor postPerfectPhotoInteractor;
    private static final int RESULT_REFRESH = 1;

    PostPerfectPhotoPresenter(IPostPerfectPhotoView iPostPerfectPhotoView) {
        this.iPostPerfectPhotoView = iPostPerfectPhotoView;
        this.postPerfectPhotoInteractor = new PostPerfectPhotoInteractor();
    }

    @Override
    public void postNewPerfectPhoto(HashMap<String, String> perfectPhotoInfo) {

        postPerfectPhotoInteractor.postNewPerfectPhotoRetrofit(perfectPhotoInfo, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {

                iPostPerfectPhotoView.hideProgressBar();
                iPostPerfectPhotoView.setResultCode(RESULT_REFRESH); // Send result 1 back to Fragment perfect photo to do refresh
                iPostPerfectPhotoView.finished();
            }

            @Override
            public void onError() {
                iPostPerfectPhotoView.showErrorToast();
            }
        });
    }
}
