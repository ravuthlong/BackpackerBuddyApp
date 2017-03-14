package ravtrix.backpackerbuddy.activities.editinfo;

import android.app.Activity;
import android.content.Intent;

import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 9/14/16.
 */
class EditInfoPresenter implements IEditPresenter {

    private IEditInfoView view;
    private EditInfoInteractor infoInteractor;
    private UserLocalStore userLocalStore;
    private Intent intent;
    private String detailType;

    EditInfoPresenter(IEditInfoView editInfoView) {
        this.view = editInfoView;
        infoInteractor = new EditInfoInteractor();
        userLocalStore = new UserLocalStore((Activity) editInfoView);

        this.intent = ((Activity) editInfoView).getIntent();
        setTextFields();
    }

    @Override
    public void setTextFields() {
        view.setTitle(intent.getStringExtra("title"));
        if (isHint()) {
            view.setHint(intent.getStringExtra("detail"));
        } else {
            view.setText(intent.getStringExtra("detail"));
        }
        this.detailType = intent.getStringExtra("detailType");
    }

    @Override
    public boolean checkEmptyString(String text) {
        return infoInteractor.isStringEmpty(text);
    }

    @Override
    public void editUserInfo(String newPost, String editType) {
        infoInteractor.editUserInfoRetrofit(newPost, editType, userLocalStore, new OnRetrofitCompleteListener() {
            @Override
            public void onSuccess() {
                view.finishActivity();
            }
            @Override
            public void onError() {
                view.displayErrorToast();
            }
        });
    }

    @Override
    public String getDetailType() {
        return detailType;
    }

    @Override
    public boolean isHint() {
        return intent.getBooleanExtra("isHint", true);
    }

    /*
    @Override
    public void onDestroy() {
        view = null;
    } */
}
