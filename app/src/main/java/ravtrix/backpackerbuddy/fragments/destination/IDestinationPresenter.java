package ravtrix.backpackerbuddy.fragments.destination;

import java.util.HashMap;

/**
 * Created by Ravinder on 10/4/16.
 */

interface IDestinationPresenter {

    void insertTravelRetrofit(HashMap<String, String> travelSpot);
    void onDestroy();
}