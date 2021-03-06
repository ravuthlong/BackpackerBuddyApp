package ravtrix.backpackerbuddy.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import ravtrix.backpackerbuddy.UserLocation;
import ravtrix.backpackerbuddy.activities.signup2.OnCountryReceived;
import ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear.OnLocationReceivedGuest;
import ravtrix.backpackerbuddy.interfacescom.OnGeocoderFinished;
import ravtrix.backpackerbuddy.interfacescom.UserLocationInterface;
import ravtrix.backpackerbuddy.models.LocationUpdateSharedPreference;
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
        public static final String SERVER_URL = "http://backpackerbuddy.net";
    }

    /**
     * Create a progress dialog object
     * @param context       the context to display the dialog
     * @param message       the message to be displayed
     * @return              the progress dialog object
     */
    public static ProgressDialog showProgressDialog(Context context, String message) {
        if (context != null) {
            ProgressDialog pDialog = new ProgressDialog(context);
            pDialog.setMessage(message);
            pDialog.setCancelable(false);
            pDialog.show();
            return pDialog;
        }
        return null;
    }

    /**
     * Hide progress dialog
     * @param pDialog       the progress dialog to be hidden
     */
    public static void hideProgressDialog(ProgressDialog pDialog) {
        if (pDialog != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    public static void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
    }

    /**
     * Create a retrofit object
     * @param serverURL       the url to the server
     */
    public static Retrofit retrofitBuilder(String serverURL)  {

        return new Retrofit.Builder()
                .baseUrl(serverURL)
                .client(okClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Show an alert dialog
     * @param context       the context to show alert dialog
     * @param message       the message to be displayed
     */
    public static void showAlertDialog(Context context, String message) {
        if (context != null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setMessage(message);
            dialogBuilder.setPositiveButton("Ok", null);
            dialogBuilder.show();
        }
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
     * @param string        the message to be displayed
     */
    public static void displayToast(Context context, String string) {
        if (context != null) {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fetch the country and city of the user
     * @param latitude      the latitude of location
     * @param longitude     the longitude of location
     * @return              string of country/city of user
     */
    public static String getLocationInfo(String latitude, String longitude) {
        URL url;
        HttpURLConnection urlConnection = null;
        JSONObject jsonObject;
        String city = "";
        String country = "";
        String urlAddress = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                latitude + "," + longitude + "&key=AIzaSyAmTO0JZ99D42Ja0XXahi-dKLLsV-2mLRI&language=en-US";
        try {
            url = new URL(urlAddress);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                try {
                    jsonObject = new JSONObject(readStream(urlConnection.getInputStream()));

                    JSONArray address_components = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                    for (int i = 0; i < address_components.length(); i++) {
                        JSONObject component = address_components.getJSONObject(i);

                        String long_name = component.getString("long_name");
                        JSONArray typeArray = component.getJSONArray("types");
                        String addressType = typeArray.getString(0);

                        if (null != long_name && long_name.length() > 0) {
                            switch (addressType) {
                                case "locality":
                                    city += long_name + ", ";
                                    break;
                                case "country":
                                    country += long_name;
                                    break;
                                default:
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (city + country);
    }

    /**
     * Get country of the user
     * @param latitude          the latitude of location
     * @param longitude         the longitude of location
     * @return                  the country
     */
    public static String getCountry(String latitude, String longitude) {
        URL url;
        HttpURLConnection urlConnection = null;
        JSONObject jsonObject;
        String country = "";
        String urlAddress = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                latitude + "," + longitude + "&key=AIzaSyAmTO0JZ99D42Ja0XXahi-dKLLsV-2mLRI&language=en-US";
        try {
            url = new URL(urlAddress);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                try {
                    jsonObject = new JSONObject(readStream(urlConnection.getInputStream()));

                    JSONArray address_components = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                    for (int i = 0; i < address_components.length(); i++) {
                        JSONObject component = address_components.getJSONObject(i);

                        String long_name = component.getString("long_name");
                        JSONArray typeArray = component.getJSONArray("types");
                        String addressType = typeArray.getString(0);

                        if (null != long_name && long_name.length() > 0) {
                            switch (addressType) {
                                case "country":
                                    country += long_name;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return country;
    }

    /**
     * Read the input stream into a String object
     * @param in        the input stream
     * @return          the string of information
     */
    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public static String getCountryGeocoder(Activity context, double latitude,
                                            double longitude, OnCountryReceived onCountryReceived) throws IOException {
        if (context != null) {
            Geocoder geoCoder = new Geocoder(context, Locale.ENGLISH);
            List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
            for (Address address : addresses) {
                if (address != null) {
                    String country = "";
                    country = address.getCountryName();
                    onCountryReceived.onCountryReceived(country);
                    return country;
                }
            }
            onCountryReceived.onCountryReceived(null);
        }
        return null;
    }

    /**
     * Find the city name, given the latitude and longitude
     * @param context       the context of the class
     * @param latitude      the latitude of the location
     * @param longitude     the longitude of the location
     */
    public static void cityGeocoder(final Context context, final double latitude,
                                    final double longitude, final OnGeocoderFinished onGeocoderFinished) {

        if (context != null) {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    String location = "";
                    List<Address> addresses = null;

                    Geocoder geoCoder = new Geocoder(context, Locale.getDefault());

                    // Geocoder doesn't always find location on first try, try 3 times
                    try {
                        addresses = geoCoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        return null;
                    }

                    if (addresses != null) {
                        for (Address address : addresses) {
                            if (address != null) {
                                // Search for city and country
                                location = "";
                                String city = address.getLocality();
                                String country = address.getCountryName();

                                if (city != null && !city.equals("")) {
                                    location = city;
                                }
                                if (country != null && !country.equals("")) {
                                    location += ", " + country;
                                }

                                // Only return when there is some location result or loop max hit
                                if (location.length() > 0) {
                                    // Return if
                                    return location;
                                }
                            }
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    onGeocoderFinished.onFinished(s);
                }
            }.execute();

        }
    }

    /**
     * Calculate the time difference between two given times
     * @param currentTime       the current time
     * @param timeBefore        the time before
     * @return                  the difference
     */
    private static long timeDifInMinutes(long currentTime, long timeBefore) {
        return TimeUnit.MILLISECONDS.toMinutes(currentTime - timeBefore);
    }

    /**
     * Update the location and time for a user in the backend
     * @param activity           the activity of the class
     * @param userLocalStore    the local information about the user
     * @param currentTime       the current time
     */
    private static void updateLocationAndTime(final Activity activity, final UserLocalStore userLocalStore, final long currentTime) {

        if (activity != null && isConnectedToInternet(activity) && (userLocalStore.getLoggedInUser().getUserID() != 0)) {
            UserLocation userLocation = new UserLocation(activity);
            userLocation.startLocationService(new UserLocationInterface() {
                @Override
                public void onReceivedLocation(final double latitude, final double longitude) {
                    final HashMap<String, String> locationHash = new HashMap<>();
                    locationHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                    locationHash.put("longitude", Double.toString(longitude));
                    locationHash.put("latitude", Double.toString(latitude));
                    locationHash.put("time", Long.toString(currentTime));

                    new RetrieveCountryGeoTask(activity, longitude, latitude, new OnCountryGeoRetrievedListener() {
                        @Override
                        public void onCountryReceived(String country) {
                            locationHash.put("country", country);
                            retrofitUpdateLocation(userLocalStore, currentTime, locationHash, latitude, longitude, activity);
                        }
                        @Override
                        public void onIOException() {
                            new RetrieveCountryTask(Double.toString(latitude), Double.toString(longitude), new OnCountryRetrievedListener() {
                                @Override
                                public void onCountryRetrieved(String country) {
                                    locationHash.put("country", country);
                                    retrofitUpdateLocation(userLocalStore, currentTime, locationHash, latitude, longitude, activity);
                                }
                            }).execute();
                        }
                    }).execute();
                }
            });
        }
    }


    public static void fetchLatAndLong(final Context context, UserLocalStore userLocalStore, final OnLocationReceivedGuest onLocationReceivedGuest) {
        if (isConnectedToInternet(context)) {
            UserLocation userLocation = new UserLocation((Activity) context);
            userLocation.startLocationService(new UserLocationInterface() {
                @Override
                public void onReceivedLocation(final double latitude, final double longitude) {
                    onLocationReceivedGuest.onLocationReceivedGuest(Double.toString(longitude), Double.toString(latitude));
                }
            });
        }
    }

    private static void retrofitUpdateLocation(final UserLocalStore userLocalStore, final long currentTime,
                                               HashMap<String, String> locationHash, final double latitude, final double longitude,
                                               final Activity activity) {
        Call<JsonObject> jsonObjectCall =  RetrofitUserInfoSingleton.getRetrofitUserInfo().updateLocation().updateLocation(locationHash);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Reset local storage of user also after server-side update
                userLocalStore.changelatitude(latitude);
                userLocalStore.changeLongitude(longitude);
                userLocalStore.changeTime(currentTime);

                LocationUpdateSharedPreference locationUpdateSharedPreference = new LocationUpdateSharedPreference(activity);
                locationUpdateSharedPreference.setLocationNotUpdating();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {}
        });
    }

    /*
    * Check if location update is needed. If needed, update local store and server
    */
    public static void checkLocationUpdate(Activity activity, UserLocalStore userLocalStore) {
        long currentTime = System.currentTimeMillis();

        LocationUpdateSharedPreference locationUpdateSharedPreference = new LocationUpdateSharedPreference(activity);

        // Check to make sure the app is not already running location update
        if (!locationUpdateSharedPreference.isLocationUpdating()) {
            // If it's been 5 minute since last location update, do the update
            // userLocalStore.getLoggedInUser().getTime() > currentTime shows rare case when user manually changed their mobile
            // time last time they access the app
            if ((Helpers.timeDifInMinutes(currentTime,
                    userLocalStore.getLoggedInUser().getTime()) > 5) || (userLocalStore.getLoggedInUser().getTime() > currentTime)) {
                // User is updating on this fragment. Prevent other fragments from updating the location
                locationUpdateSharedPreference.setLocationUpdating();

                Helpers.updateLocationAndTime(activity, userLocalStore, currentTime);
            }
        }
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


    public static void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Text.ttf"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Scale image
     * @param selectedImage     the image selected by user
     * @return                  scaled version of bitmap
     * @throws FileNotFoundException
     */
    public static Bitmap decodeBitmap(Activity activity, Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 300;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    /**
     * Rotate image 90 degree
     * @param bitmap        the image selected by user
     * @return              rotated version of image
     */
    public static Bitmap rotateBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        return Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }


    /**
     * Check network connection
     * @param context       the context of network check
     * @return              true is the user is connected to the internet, false otherwise
     */
    private static boolean isConnectedToInternet(Context context) {

        NetworkInfo activeNetwork = null;

        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null;
    }

    public static void displayErrorToast(Context context) {
        if (!isConnectedToInternet(context)) {
            Helpers.displayToast(context, "Not connected to the internet");
        } else {
            Helpers.displayToast(context, "Problem loading...");
        }
    }

    private static class RetrieveCountryGeoTask extends AsyncTask<Void, Void, String> {

        private Context context;
        private double longitude, latitude;
        private OnCountryGeoRetrievedListener onCountryGeoRetrievedListener;

        RetrieveCountryGeoTask(Context context, double longitude, double latitude,
                                      OnCountryGeoRetrievedListener onCountryGeoRetrievedListener) {
            this.context = context;
            this.longitude = longitude;
            this.latitude = latitude;
            this.onCountryGeoRetrievedListener = onCountryGeoRetrievedListener;
        }
        @Override
        protected String doInBackground(Void... voids) {
            if (context != null) {
                Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
                    for (Address address : addresses) {
                        if (address != null) {
                            String country = "";
                            country = address.getCountryName();
                            return country;
                        }
                    }
                } catch (IOException e) {
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                onCountryGeoRetrievedListener.onIOException();
            } else {
                onCountryGeoRetrievedListener.onCountryReceived(s);
            }
        }
    }

    private static class RetrieveCountryTask extends AsyncTask<Void, Void, String> {
        String latitude;
        String longitude;
        OnCountryRetrievedListener onCountryRetrievedListener;

        RetrieveCountryTask(String latitude, String longitude, OnCountryRetrievedListener onCountryRetrievedListener) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.onCountryRetrievedListener = onCountryRetrievedListener;

        }
        @Override
        protected String doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection = null;
            JSONObject jsonObject;
            String country = "";
            String urlAddress = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                    latitude + "," + longitude + "&key=AIzaSyAmTO0JZ99D42Ja0XXahi-dKLLsV-2mLRI&language=en-US";
            try {
                url = new URL(urlAddress);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK) {
                    try {
                        jsonObject = new JSONObject(readStream(urlConnection.getInputStream()));

                        JSONArray address_components = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                        for (int i = 0; i < address_components.length(); i++) {
                            JSONObject component = address_components.getJSONObject(i);

                            String long_name = component.getString("long_name");
                            JSONArray typeArray = component.getJSONArray("types");
                            String addressType = typeArray.getString(0);

                            if (null != long_name && long_name.length() > 0) {
                                switch (addressType) {
                                    case "country":
                                        country += long_name;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return country;
        }

        @Override
        protected void onPostExecute(String s) {
            onCountryRetrievedListener.onCountryRetrieved(s);
        }
    }
}
