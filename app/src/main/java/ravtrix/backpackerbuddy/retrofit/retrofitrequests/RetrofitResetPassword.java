package ravtrix.backpackerbuddy.retrofit.retrofitrequests;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.RetrofitResetPasswordInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 3/26/17.
 */

public class RetrofitResetPassword {

    private Retrofit retrofit;

    public RetrofitResetPassword() {
        retrofit = Helpers.retrofitBuilder(Helpers.ServerURL.SERVER_URL);
    }

    public RetrofitResetPasswordInterfaces.UpdateResetToken updateResetToken() {
        return retrofit.create(RetrofitResetPasswordInterfaces.UpdateResetToken.class);
    }

    public RetrofitResetPasswordInterfaces.SendResetEmail sendResetEmail() {
        return retrofit.create(RetrofitResetPasswordInterfaces.SendResetEmail.class);
    }

}
