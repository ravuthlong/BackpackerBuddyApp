package ravtrix.backpackerbuddy.fragments.destination;

import java.util.HashMap;

/**
 * Created by Ravinder on 10/4/16.
 */

interface IDestinationInteractor {

    void insertTravelSpotRetrofit(HashMap<String, String> travelSpot,
                                  OnRetrofitDestinationListener onRetrofitDestinationListener);
}
