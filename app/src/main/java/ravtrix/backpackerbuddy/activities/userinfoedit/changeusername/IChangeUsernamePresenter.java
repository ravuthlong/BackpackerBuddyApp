package ravtrix.backpackerbuddy.activities.userinfoedit.changeusername;

import java.util.HashMap;

/**
 * Created by Ravinder on 1/24/17.
 */

interface IChangeUsernamePresenter {

    void checkUsernameTaken(final String newUsername);
    void changeUsername(final HashMap<String, String> userInfo);
    void changeUsernameFacebook(final HashMap<String, String> userInfo);
}
