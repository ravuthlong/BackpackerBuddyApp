package ravtrix.backpackerbuddy.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ravinder on 8/17/16.
 */
public class Helper {

    public static final class ServerURL {
        public static final String SERVER_URL = "http://backpackerbuddy.net23.net";
    }

    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    public static void hideProgressDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public static Retrofit retrofitBuilder(Retrofit retrofit, String serverURL)  {

        return new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static void showAlertDialog(Context context, String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
}
