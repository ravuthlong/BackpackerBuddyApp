package ravtrix.backpackerbuddy.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Create a progress dialog object
     * @param context       the context to display the dialog
     * @param message       the message to be displayed
     * @return              the progress dialog object
     */
    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    /**
     * Hide progress dialog
     * @param pDialog       the progress dialog to be hidden
     */
    public static void hideProgressDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * Create a retrofit object
     * @param serverURL       the url to the server
     */
    public static Retrofit retrofitBuilder(String serverURL)  {

        return new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Show an alert dialog
     * @param context       the context to show alert dialog
     * @param message       the message to be displayed
     */
    public static void showAlertDialog(Context context, String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    /**
     * Create an alert dialog with positive buttons
     * @param activity      the activity for the dialog
     * @param message       the message to be displayed
     * @param negative      the text for negative button
     */
    public static AlertDialog.Builder showAlertDialogWithTwoOptions(final android.app.Activity activity, String title, String message,
                                                     String negative) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setNegativeButton(negative, null);
        return dialogBuilder;
    }

    /**
     * Display a toast on the screen
     * @param context       the context to display the toast
     * @param string        the message to be diaplyed
     */
    public static void displayToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * Find the city name, given the latitude and longitude
     * @param context       the context of the class
     * @param latitude      the latitude of the location
     * @param longitude     the longitude of the location
     * @return              the city name
     */
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

    /**
     * Calculate the time difference between two given times
     * @param currentTime       the current time
     * @param timeBefore        the time before
     * @return                  the difference
     */
    public static long timeDifInMinutes(long currentTime, long timeBefore) {
        return TimeUnit.MILLISECONDS.toMinutes(currentTime - timeBefore);
    }

    /**
     * Update the location and time for a user in the backend
     * @param context           the context of the class
     * @param userLocalStore    the local information about the user
     * @param currentTime       the current time
     */
    public static void updateLocationAndTime(Context context, final UserLocalStore userLocalStore, final long currentTime) {

        UserLocation userLocation = new UserLocation((Activity) context);
        userLocation.startLocationService(new UserLocationInterface() {
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
                                userLocalStore.getLoggedInUser().getUserImageURL(), userLocalStore.getLoggedInUser().getTraveling(),
                                latitude, longitude,
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

    /**
     * Set the toolbar to an activity ot fragment
     * @param appCompatActivity         the activity
     * @param toolbar                   the toolbar for activity/fragment
     */
    public static void setToolbar(final AppCompatActivity appCompatActivity, Toolbar toolbar) {

        appCompatActivity.setSupportActionBar(toolbar);

        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);
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

    /**
     * Check if a bundle of a given context is null
     * @param context       the context of the bundle
     * @return              true is bundle is null, false if not
     */
    public static boolean isBundleNull(Activity context) {
        boolean isBundleNull = false;
        if (context.getIntent().getExtras() == null) {
            isBundleNull = true;
        }
        return isBundleNull;
    }

    /**
     * Check to see if the email is valid
     * @param email         the email to be checked
     * @return              true if email is valid, false if not
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Check to see if google play services is available on the device
     * @param activity          the activity to check play services availability
     * @return                  true if available, false if not
     */
    public static boolean checkPlayServices(Activity activity) {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                activity.finish();
            }
            return false;
        }
        return true;
    }
}
