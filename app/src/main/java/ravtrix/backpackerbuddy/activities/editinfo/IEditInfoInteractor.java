package ravtrix.backpackerbuddy.activities.editinfo;

import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 9/14/16.
 */
interface IEditInfoInteractor {

    /**
     * Checks is string is empty
     * @param text                  string to be checked
     * @return                      true is empty, else false
     */
    boolean isStringEmpty(String text);

    /**
     * Edit user information
     * @param newPost                           the new user post
     * @param type                              what type of post it is
     * @param userLocalStore                    the current user
     * @param onRetrofitCompleteListener        listens for completion of retrofit
     */
    void editUserInfoRetrofit(String newPost, String type, UserLocalStore userLocalStore,
                         OnRetrofitCompleteListener onRetrofitCompleteListener);
}
