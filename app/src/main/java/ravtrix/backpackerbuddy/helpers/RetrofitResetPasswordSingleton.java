package ravtrix.backpackerbuddy.helpers;

import ravtrix.backpackerbuddy.retrofit.retrofitrequests.RetrofitResetPassword;

/**
 * Created by Ravinder on 3/26/17.
 */

public class RetrofitResetPasswordSingleton {
    private static RetrofitResetPassword retrofitResetPassword = new RetrofitResetPassword();

    public static RetrofitResetPassword getRetrofitResetPassword() {
        return retrofitResetPassword;
    }
}
