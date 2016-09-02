package ravtrix.backpackerbuddy.retrofit.retrofitrequests.retrofitusercountriesrequests;

import ravtrix.backpackerbuddy.helper.Helper;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.usercountriesinterfaces.RetrofitUserCountriesInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 8/31/16.
 */
public class RetrofitUserCountries {

    private Retrofit retrofit;

    public RetrofitUserCountries() {
        retrofit = Helper.retrofitBuilder(this.retrofit, Helper.ServerURL.SERVER_URL);
    }

    public RetrofitUserCountriesInterfaces.getNotLoggedInCountryPosts getNotLoggedInCountryPosts() {
        return retrofit.create(RetrofitUserCountriesInterfaces.getNotLoggedInCountryPosts.class);
    }

    public RetrofitUserCountriesInterfaces.insertTravelSpot insertTravelSpot() {
        return retrofit.create(RetrofitUserCountriesInterfaces.insertTravelSpot.class);
    }
}
