package ravtrix.backpackerbuddy.fragments.createdestination;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Ravinder on 10/4/16.
 */

class DestinationPresenter implements IDestinationPresenter {

    private IDestinationView view;
    private DestinationInteractor destinationInteractor;

    DestinationPresenter(IDestinationView view) {
        this.view = view;
        this.destinationInteractor = new DestinationInteractor();
    }

    @Override
    public void insertTravelRetrofit(HashMap<String, String> travelSpot) {
        destinationInteractor.insertTravelSpotRetrofit(travelSpot, new OnRetrofitDestinationListener() {
            @Override
            public void onSuccess() {
                view.hideProgressDialog();
                view.startDestinationFrag();
            }

            @Override
            public void onFailure() {
                view.hideProgressDialog();
                view.showToastError();
            }
        });
    }

    @Override
    public boolean isDateValid(Calendar from, Calendar to) {
        return destinationInteractor.isDateValid(from, to);
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
