package ravtrix.backpackerbuddy.helpers;

/**
 * Created by Ravinder on 1/17/17.
 */

public interface OnCountryGeoRetrievedListener {
    void onCountryReceived(String country);
    void onIOException();
}
