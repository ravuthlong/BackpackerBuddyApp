package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

/**
 * Created by Ravinder on 1/6/17.
 */

/**
 * Used by Helpers class when fetching location for guest log in
 */
public interface OnLocationReceivedGuest {
    void onLocationReceivedGuest(String longitude, String latitude);
}
