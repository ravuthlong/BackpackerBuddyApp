package ravtrix.backpackerbuddy.activities.editpost;

import android.app.Activity;
import android.app.ProgressDialog;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.Helpers;

/**
 * Created by Ravinder on 9/14/16.
 */
class EditPostPresenter implements IEditPostPresenter {

    private IEditPostView editPostView;
    private EditPostInteractor editPostInteractor;
    private ProgressDialog progressDialog;

    EditPostPresenter(IEditPostView editView) {
        this.editPostView = editView;
        this.editPostInteractor = new EditPostInteractor();
    }

    @Override
    public void editPost(HashMap<String, String> travelSpotHash, final int returnActivity) {

        progressDialog = Helpers.showProgressDialog((Activity) editPostView, "Loading...");
        editPostInteractor.editPostRetrofit(travelSpotHash, new OnRetrofitEditPostListener() {
            @Override
            public void onSuccess() {
                Helpers.hideProgressDialog(progressDialog);

                if (returnActivity == 1) {
                    editPostView.startMainPageActivity();
                } else {
                    editPostView.startMainPageActivity();
                }
            }

            @Override
            public void onError() {
                editPostView.displayTryAgainToast();
            }
        });
    }

    @Override
    public void onDestroy() {
        editPostView = null;
    }
}
