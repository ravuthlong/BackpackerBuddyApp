package ravtrix.backpackerbuddy.activities.editpost;

import java.util.HashMap;

/**
 * Created by Ravinder on 9/14/16.
 */
public interface IEditPostPresenter {

    void editPost(HashMap<String, String> travelSpotHash);
    void onDestroy();
}
