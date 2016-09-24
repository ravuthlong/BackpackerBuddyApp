package ravtrix.backpackerbuddy.helpers;

import ravtrix.backpackerbuddy.retrofit.retrofitrequests.RetrofitUserCountries;

/**
 * Created by Ravinder on 9/10/16.
 */
public class RetrofitUserCountrySingleton {
    private static RetrofitUserCountries retrofitUserCountry = new RetrofitUserCountries();

    public static RetrofitUserCountries getRetrofitUserCountry() {
        return retrofitUserCountry;
    }

}
