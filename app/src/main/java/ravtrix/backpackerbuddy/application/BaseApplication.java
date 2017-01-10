package ravtrix.backpackerbuddy.application;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.helpers.RetrofitAppInfoSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/8/17.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void checkForRequiredUpdate(final Activity activity) {

        Call<JsonObject> retrofitCall = RetrofitAppInfoSingleton.getRetrofitAppInfo()
                .fetchMinVersion()
                .fetchMinVersion();
        retrofitCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                int success = response.body().get("success").getAsInt();
                double versionNum = Double.parseDouble(response.body().get("versionMin").getAsString());

                // If current version of this app is less than or equal to the set minimum version
                // on the server side, prompt an update dialog
                if ((success == 1) && (Double.parseDouble(getVersionInfo()) <= versionNum)) {
                    showUpdateDialog(activity);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Get the current version name of the app
     * @return                  the current version name (Ex. 1.2)
     */
    private String getVersionInfo() {
        String versionName = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * Alert the user that their app requires an update to continue using
     * @param activity                  the activity to display the dialog
     */
    private void showUpdateDialog(final Activity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle("Important Update");
        dialogBuilder.setMessage("An essential update must be performed in order to continue using the app");
        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                }
                catch (android.content.ActivityNotFoundException e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        dialogBuilder.setCancelable(false);
        dialogBuilder.show();
    }


}
