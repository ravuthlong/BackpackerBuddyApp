package ravtrix.backpackerbuddy.retrofit.retrofitrequests.retrofitusercountriesrequests;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.usercountriesinterfaces.RetrofitUserCountriesInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 8/31/16.
 */
public class RetrofitUserCountries {

    private Retrofit retrofit;

    public RetrofitUserCountries() {
        retrofit = Helpers.retrofitBuilder(this.retrofit, Helpers.ServerURL.SERVER_URL);
    }

    public RetrofitUserCountriesInterfaces.getNotLoggedInCountryPosts getNotLoggedInCountryPosts() {
        return retrofit.create(RetrofitUserCountriesInterfaces.getNotLoggedInCountryPosts.class);
    }

    public RetrofitUserCountriesInterfaces.insertTravelSpot insertTravelSpot() {
        return retrofit.create(RetrofitUserCountriesInterfaces.insertTravelSpot.class);
    }

    public RetrofitUserCountriesInterfaces.InsertFavorite insertFavoritePost() {
        return retrofit.create(RetrofitUserCountriesInterfaces.InsertFavorite.class);
    }

    public RetrofitUserCountriesInterfaces.RemoveFavorite removeFavoritePost() {
        return retrofit.create(RetrofitUserCountriesInterfaces.RemoveFavorite.class);
    }
}
