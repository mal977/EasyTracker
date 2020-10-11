package com.ntu.staizen.EasyTracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ntu.staizen.EasyTracker.events.FirebaseAuthenticatedEvent;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.firebase.Authentication;
import com.ntu.staizen.EasyTracker.firebase.FireStore;
import com.ntu.staizen.EasyTracker.greendao.BoxHelper;
import com.ntu.staizen.EasyTracker.location.LocationCollectingImplementation;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.LocationData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static String TAG = MapsActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted = false;
    private GoogleMap mMap;

    private Authentication mAuthentication;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BoxHelper boxHelper = BoxHelper.getInstance(this);
        LocationData locationData = new LocationData();
        LocalDateTime localDateTime = LocalDateTime.now();
        ArrayList<LocationData> locationDataArrayList = new ArrayList<>();
        locationDataArrayList.add(locationData);
        boxHelper.addLocationData(locationDataArrayList);
        locationDataArrayList = boxHelper.getLocationData();
        for (LocationData loc : locationDataArrayList
        ) {
            Log.d(TAG, loc.toString());
        }

        if (!Utilities.isLocationEnabled(this)) {
        }
        getLocationPermission();
        mAuthentication = Authentication.getInstance(this);
        mAuthentication.signInAnonymously(this);

    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;

        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                LocationCollectingImplementation locationCollectingImplementation = new LocationCollectingImplementation(this);
                locationCollectingImplementation.createLocationRequest();
                locationCollectingImplementation.createLocationSettingsRequest();
                locationCollectingImplementation.startLocationUpdates();
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(10f);

        mMap.moveCamera(CameraUpdateFactory.zoomTo(12f));
        mMap.setMaxZoomPreference(18f);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(1.347621, 103.683530);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        updateLocationUI();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }


    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLocationChangedEvent(LocationChangedEvent event) {
        Log.d(TAG, "LocationChangedEvent Success");
        if (event != null && event.getNewLocation() != null) {
            Log.d(TAG, "New Location MainActivity: " + event.getNewLocation().toString());
        }
        if (event.getNewLocation() != null) {
            LatLng loc = new LatLng(event.getNewLocation().getLatitude(), event.getNewLocation().getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleFirebaseAuthenticatedEvent(FirebaseAuthenticatedEvent event) {
        Log.d(TAG, "FirebaseAuthenticatedEvent Success");
        JobData jobData = new JobData("MalcomJob",
                "MalcomCompany","69", System.currentTimeMillis(),System.currentTimeMillis());
        List<LocationData> locationDataList = new ArrayList<>();
        locationDataList.add(new LocationData(System.currentTimeMillis(),1.4544f,6.9999f));
        jobData.setLocationDataList(locationDataList);
//        FireStore.getInstance(this).sendNewJobToFireStore(mAuthentication.getUID(),jobData);
        FireStore.getInstance(this).sendLocationUpdateToFireStore(mAuthentication.getUID(),new LocationData(System.currentTimeMillis(),2.0f,6.9999f));

    }
}