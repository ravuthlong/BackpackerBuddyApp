package ravtrix.backpackerbuddy.RetrofitRequests;

import ravtrix.backpackerbuddy.Helper.APIAccess;
import ravtrix.backpackerbuddy.Interfaces.LogUserIn;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ravinder on 8/17/16.
 */
public class RetrofitUserInfo {

    private Retrofit retrofit;

    public LogUserIn loggedInUser() {

        buildRetroFit(APIAccess.ServerURL.SERVER_URL);
        LogUserIn service =  retrofit.create(LogUserIn.class);
        return service;
    }

    private void buildRetroFit(String serverURL) {
        retrofit = new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
