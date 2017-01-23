package ravtrix.backpackerbuddy.activities.createdestination;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Ravinder on 10/4/16.
 */

interface IDestinationInteractor {

    /**
     * Insert a new travelling destination
     * @param travelSpot                        - HashMap containing information about the travel spot
     * @param onRetrofitDestinationListener     - call back listener when retrofit is completed
     */
    void insertTravelSpotRetrofit(HashMap<String, String> travelSpot,
                                  OnRetrofitDestinationListener onRetrofitDestinationListener);

    /**
     * Check if date is valid
     * @param from                              - from date
     * @param to                                - to date
     * @return                                  - true if valid, else false
     */
    boolean isDateValid(Calendar from, Calendar to);
}
