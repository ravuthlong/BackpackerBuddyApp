package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Ravinder on 1/8/17.
 */

public interface RetrofitAppInfoInterfaces {

    public interface FetchMinVersion {
        @GET("/fetchVersionMin.php")
        Call<JsonObject> fetchMinVersion();
    }
}
