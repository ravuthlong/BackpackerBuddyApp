package ravtrix.backpackerbuddy.fragments.findbuddy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.fragments.message.MessagesFragment;
import ravtrix.backpackerbuddy.helpers.Helpers;

/**
 * Created by Ravinder on 9/12/16.
 */
public class FindBuddyFragment extends Fragment {

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_find_buddy, container, false);


        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
                .add(R.string.random, MessagesFragment.class)
                .add(R.string.nearby, MessagesFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                // Extract the address of the user
                Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
                    for (Address address : addresses) {
                        if (address != null) {

                            String city = address.getLocality();
                            if (city != null && !city.equals("")) {
                                System.out.println("CITY:  " + city);
                            } else {
                                //
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                Helpers.displayToast(getContext(), "Longitude: " + location.getLongitude() +
                        " Latitude: " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                // When GPS setting is turned off
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        // Check for user's SDK Version. SDK version >= Marshmallow need permission access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 1);
            }
        } else {
            configureButton();
        }


        return v;
    }

    // Case for users with grant access needed. Location access granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configureButton();
                }
                break;
        }
    }

    // User with granted access can access the location now
    private void configureButton() {
        try {
            // Refresh every 5 seconds .. moved 0 meters from previous location
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            locationManager.removeUpdates(locationListener);
        } catch (SecurityException e) {}
    }
}
