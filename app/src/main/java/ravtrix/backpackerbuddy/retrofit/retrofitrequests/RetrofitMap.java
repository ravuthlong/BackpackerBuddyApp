package ravtrix.backpackerbuddy.retrofit.retrofitrequests;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.RetrofitUserMapInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 3/6/17.
 */

public class RetrofitMap {
    private Retrofit retrofit;

    public RetrofitMap() {
        retrofit = Helpers.retrofitBuilder(Helpers.ServerURL.SERVER_URL);
    }

    public RetrofitUserMapInterfaces.InsertIntoMap insertIntoMap() {
        return retrofit.create(RetrofitUserMapInterfaces.InsertIntoMap.class);
    }

    public RetrofitUserMapInterfaces.GetUserMap getUserMap() {
        return retrofit.create(RetrofitUserMapInterfaces.GetUserMap.class);
    }

    public RetrofitUserMapInterfaces.RemoveMapLocation removeMapLocation() {
        return retrofit.create(RetrofitUserMapInterfaces.RemoveMapLocation.class);
    }

    public RetrofitUserMapInterfaces.CheckHasMap checkHasMap() {
        return retrofit.create(RetrofitUserMapInterfaces.CheckHasMap.class);
    }
}
