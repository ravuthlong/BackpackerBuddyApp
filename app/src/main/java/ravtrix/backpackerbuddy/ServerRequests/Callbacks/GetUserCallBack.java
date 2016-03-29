package ravtrix.backpackerbuddy.ServerRequests.Callbacks;

import ravtrix.backpackerbuddy.User;

/**
 * Created by Ravinder on 3/28/16.
 */
public interface GetUserCallBack {
    abstract void done(User returnedUser);
}
