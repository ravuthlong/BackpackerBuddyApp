package ravtrix.backpackerbuddy.activities.editpost;

import java.util.HashMap;

/**
 * Created by Ravinder on 9/14/16.
 */
public class EditPostPresenter implements IEditPostPresenter {

    private IEditPostView editPostView;
    private EditPostInteractor editPostInteractor;

    public EditPostPresenter(IEditPostView editView) {
        this.editPostView = editView;
        this.editPostInteractor = new EditPostInteractor();
    }

    @Override
    public void editPost(HashMap<String, String> travelSpotHash) {
        editPostInteractor.editPostRetrofit(travelSpotHash, new OnRetrofitEditPostListener() {
            @Override
            public void onSuccess() {
                editPostView.startMainPageActivity();
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
