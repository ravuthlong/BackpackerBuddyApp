package ravtrix.backpackerbuddy.activities.editpost;

import java.util.HashMap;

/**
 * Created by Ravinder on 9/14/16.
 */
public interface IEditPostInteractor {

    void editPostRetrofit(HashMap<String, String> travelSpotHash,
                          OnRetrofitEditPostListener onRetrofitEditPostListener);
}
