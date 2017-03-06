package ravtrix.backpackerbuddy.activities.usermap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import ravtrix.backpackerbuddy.R;

/**
 * Created by Ravinder on 3/5/17.
 */

public class UserMapActivity extends AppCompatActivity implements OnMapReadyCallback {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_user_map_mapFrag);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.add(new LatLng(50.537435, 10.118408));
        polylineOptions.add(new LatLng(50.003767, 14.556885));
        polylineOptions.add(new LatLng(47.542700, 14.842529));
        polylineOptions.add(new LatLng(47.379289, 19.171143));
        polylineOptions.add(new LatLng(39.933960, -3.581543));
        polylineOptions
                .width(5)
                .color(Color.BLUE);

        Polyline line = googleMap.addPolyline(polylineOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(50.537435, 10.118408)));

        /*
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
}
