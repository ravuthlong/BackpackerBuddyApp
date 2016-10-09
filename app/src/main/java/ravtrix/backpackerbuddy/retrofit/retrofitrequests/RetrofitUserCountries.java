package ravtrix.backpackerbuddy.retrofit.retrofitrequests;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.RetrofitUserCountriesInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 8/31/16.
 */
public class RetrofitUserCountries {

    private Retrofit retrofit;

    public RetrofitUserCountries() {
        retrofit = Helpers.retrofitBuilder(this.retrofit, Helpers.ServerURL.SERVER_URL);
    }

    public RetrofitUserCountriesInterfaces.GetNotLoggedInCountryPosts getNotLoggedInCountryPosts() {
        return retrofit.create(RetrofitUserCountriesInterfaces.GetNotLoggedInCountryPosts.class);
    }

    public RetrofitUserCountriesInterfaces.GetAUserCountryPosts getAUserCountryPosts() {
        return retrofit.create(RetrofitUserCountriesInterfaces.GetAUserCountryPosts.class);
    }

    public RetrofitUserCountriesInterfaces.InsertTravelSpot insertTravelSpot() {
        return retrofit.create(RetrofitUserCountriesInterfaces.InsertTravelSpot.class);
    }

    public RetrofitUserCountriesInterfaces.InsertFavorite insertFavoritePost() {
        return retrofit.create(RetrofitUserCountriesInterfaces.InsertFavorite.class);
    }

    public RetrofitUserCountriesInterfaces.RemoveFavorite removeFavoritePost() {
        return retrofit.create(RetrofitUserCountriesInterfaces.RemoveFavorite.class);
    }

    public RetrofitUserCountriesInterfaces.RemovePost removePost() {
        return retrofit.create(RetrofitUserCountriesInterfaces.RemovePost.class);
    }

    public RetrofitUserCountriesInterfaces.UpdateTravelSpot updateTravelSpot() {
        return retrofit.create(RetrofitUserCountriesInterfaces.UpdateTravelSpot.class);
    }
}
