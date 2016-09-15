package ravtrix.backpackerbuddy.activities.editinfo;

import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 9/14/16.
 */
public interface IEditInfoInteractor {
    boolean isStringEmpty(String text);
    void editUserInfoRetrofit(String newPost, String type, UserLocalStore userLocalStore,
                         OnRetrofitCompleteListener onRetrofitCompleteListener);
}
