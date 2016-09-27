package ravtrix.backpackerbuddy.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ravtrix.backpackerbuddy.UserLocation;
import ravtrix.backpackerbuddy.interfacescom.UserLocationInterface;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ravinder on 8/17/16.
 */
public class Helpers {

    private Helpers() {}

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

    public static void showAlertDialogWithTwoOptions(final android.app.Activity activity, Context context, String message,
                                                     String positive, String negative) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Backpacker Buddy");
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        dialogBuilder.setNegativeButton(negative, null);
        dialogBuilder.show();
    }

    public static void displayToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static String cityGeocoder(Context context, double latitude, double longitude) {

        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
            for (Address address : addresses) {
                if (address != null) {

                    String city = address.getLocality();
                    if (city != null && !city.equals("")) {
                        return city;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static float distanceFromAtoBInFeet(double longitude1, double latitude1, double longitude2, double latitude2) {

        Location location1 = new Location("");
        location1.setLatitude(latitude1);
        location1.setLongitude(longitude1);
        Location location2 = new Location("");
        location2.setLatitude(latitude2);
        location2.setLongitude(longitude2);

        return location1.distanceTo(location2);
    }

    public static long timeDifInMinutes(long currentTime, long timeBefore) {
        return TimeUnit.MILLISECONDS.toMinutes(currentTime - timeBefore);
    }

    public static void updateLocationAndTime(Context context, final UserLocalStore userLocalStore, final long currentTime) {
        new UserLocation(context, new UserLocationInterface() {
            @Override
            public void onReceivedLocation(final double latitude, final double longitude) {

                HashMap<String, String> locationHash = new HashMap<>();
                locationHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                locationHash.put("longitude", Double.toString(longitude));
                locationHash.put("latitude", Double.toString(latitude));
                locationHash.put("time", Long.toString(currentTime));

                Call<JsonObject> jsonObjectCall =  RetrofitUserInfoSingleton.getRetrofitUserInfo().updateLocation().updateLocation(locationHash);
                jsonObjectCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        // Reset local storage of user also after server-side update
                        LoggedInUser loggedInUser = new LoggedInUser(userLocalStore.getLoggedInUser().getUserID(),
                                userLocalStore.getLoggedInUser().getEmail(), userLocalStore.getLoggedInUser().getUsername(),
                                userLocalStore.getLoggedInUser().getUserImageURL(), latitude, longitude,
                                currentTime);
                        userLocalStore.clearUserData();
                        userLocalStore.storeUserData(loggedInUser);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });
    }

    public static void setToolbar(final AppCompatActivity appCompatActivity, Toolbar toolbar) {

        appCompatActivity.setSupportActionBar(toolbar);

        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appCompatActivity.onBackPressed();
                }
            });
        }
    }
}
