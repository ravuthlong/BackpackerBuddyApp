package ravtrix.backpackerbuddy.activities.usermap;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.usermap.model.Location;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitMapSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ravtrix.backpackerbuddy.helpers.RetrofitMapSingleton.getRetrofitMap;

/**
 * Created by Ravinder on 3/5/17.
 */

public class UserMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap googleMap;
    private List<Location> locationArrayList;
    private UserLocalStore userLocalStore;
    private ProgressDialog progressDialog;
    private boolean isOtherUserViewing = false;
    private int otherUserID = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);

        setBundle();
        setMap();
        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;
        final MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.google_map_json);
        googleMap.setMapStyle(style);

        if (!isOtherUserViewing) googleMap.setOnMarkerClickListener(this); // only set listener if owner is viewing
        fetchMapLocations(googleMap, true);
    }

    @Override
    public void onMapClick(final LatLng latLng) {

        if (!isOtherUserViewing) {
            locationArrayList.add(new Location(latLng.latitude, latLng.longitude));
            addMarkers(googleMap, locationArrayList);

            AlertDialog.Builder alertDialog = Helpers.showAlertDialogWithTwoOptions(this, "", "Add this place as your visited place?", "No");

            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    locationArrayList.remove(locationArrayList.size() - 1);
                    removeMarker(googleMap);
                    addMarkers(googleMap, locationArrayList);
                }
            });

            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // finalize here by updating database
                    insertMapRetrofit(latLng);
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        AlertDialog.Builder alertDialog = Helpers.showAlertDialogWithTwoOptions(this, "", "Remove this location?", "No");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // find matching marker in array list
                for (Location location : locationArrayList) {
                    if ((marker.getPosition().latitude == location.getLatitude()) &&
                            (marker.getPosition().longitude == location.getLongitude())) {
                        removeMapLocation(location.getId()); //remove location item
                    }
                }
            }
        });
        alertDialog.show();

        return true;
    }

    private void setBundle() {

        Bundle bundle = getIntent().getExtras();

        // If other user is viewing this map, they wouldn't have the functionality to add or remove map points
        if (bundle != null) {
            this.isOtherUserViewing = bundle.getBoolean("isOtherUser");
            this.otherUserID = bundle.getInt("otherUserID");
        }
    }

    private void setMap() {
        // Set map fragment on the layout
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_user_map_mapFrag);
        mapFragment.getMapAsync(this);
    }

    private void removeMarker(GoogleMap googleMap) {
        googleMap.clear();
    }

    private void addMarkers(GoogleMap googleMap, List<Location> locationArrayList) {

        if (locationArrayList.get(0).getId() != 0) {
            for (Location location : locationArrayList) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),
                        location.getLongitude())));

            }
        }
    }

    // view is set to first item
    private void setView(GoogleMap googleMap, List<Location> locationArrayList) {
        if (locationArrayList.size() > 0) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(locationArrayList.get(0).getLatitude(),
                    locationArrayList.get(0).getLongitude())));
        }
    }

    private void insertMapRetrofit(LatLng latLng) {

        final HashMap<String, String> locationHash = new HashMap<>();
        locationHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        locationHash.put("latitude", Double.toString(latLng.latitude));
        locationHash.put("longitude", Double.toString(latLng.longitude));

        Call<JsonObject> retrofit = getRetrofitMap()
                .insertIntoMap()
                .insertIntoMap(locationHash);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // inserted successfully
                fetchMapLocations(googleMap, false);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                // insert fail so remove from added point
                locationArrayList.remove(locationArrayList.size() - 1);
                removeMarker(googleMap);
                addMarkers(googleMap, locationArrayList);
            }
        });
    }

    private void fetchMapLocations(final GoogleMap googleMap, final boolean shouldSetView) {

        int userID = isOtherUserViewing ? otherUserID : userLocalStore.getLoggedInUser().getUserID();

        Call<List<Location>> retrofit = RetrofitMapSingleton.getRetrofitMap()
                .getUserMap()
                .getUserMap(userID);

        retrofit.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                googleMap.setOnMapClickListener(UserMapActivity.this);
                locationArrayList = response.body();

                addMarkers(googleMap, locationArrayList);

                if (shouldSetView) setView(googleMap, locationArrayList);
                if (progressDialog != null) Helpers.hideProgressDialog(progressDialog);

            }
            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                googleMap.setOnMapClickListener(UserMapActivity.this);
                if (progressDialog != null) Helpers.hideProgressDialog(progressDialog);
            }
        });
    }

    private void removeMapLocation(final int mapID) {

        progressDialog = Helpers.showProgressDialog(this, "Removing");

        Call<JsonObject> retrofit = RetrofitMapSingleton.getRetrofitMap()
                .removeMapLocation()
                .removeMapLocation(mapID);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                removeMarker(googleMap);
                fetchMapLocations(googleMap, false);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayErrorToast(UserMapActivity.this);
            }
        });
    }
}
