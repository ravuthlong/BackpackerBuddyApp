package ravtrix.backpackerbuddy.retrofit.retrofitrequests;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.RetrofitAppInfoInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 1/8/17.
 */

public class RetrofitAppInfo {
    private Retrofit retrofit;

    public RetrofitAppInfo() {
        retrofit = Helpers.retrofitBuilder(Helpers.ServerURL.SERVER_URL);
    }
    public RetrofitAppInfoInterfaces.FetchMinVersion fetchMinVersion() {
        return retrofit.create(RetrofitAppInfoInterfaces.FetchMinVersion.class);
    }
}
