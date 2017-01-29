package ravtrix.backpackerbuddy.activities.editpost;

import java.util.HashMap;

/**
 * Created by Ravinder on 9/14/16.
 */
interface IEditPostInteractor {

    /**
     * Edit the user's travel post
     * @param travelSpotHash                    - new travel post information in hash map
     * @param onRetrofitEditPostListener        - listener for retrofit completion
     */
    void editPostRetrofit(HashMap<String, String> travelSpotHash,
                          OnRetrofitEditPostListener onRetrofitEditPostListener);
}
