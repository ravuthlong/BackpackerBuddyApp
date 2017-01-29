package ravtrix.backpackerbuddy.activities.editpost;

import java.util.HashMap;

/**
 * Created by Ravinder on 9/14/16.
 */
interface IEditPostPresenter {

    void editPost(HashMap<String, String> travelSpotHash, int returnActivity);
    void onDestroy();
}
