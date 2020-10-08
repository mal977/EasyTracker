package com.ntu.staizen.EasyTracker.location;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ntu.staizen.EasyTracker.Utilities;
import com.ntu.staizen.EasyTracker.services.LocationChangedReceiver;

import java.util.concurrent.Executor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


/**
 * Created by Malcom Teh on 82/10/2020.
 */

public class LocationCollectingImplementation implements LocationCollectingInterface, ResultCallback<LocationSettingsResult> {

    private static final String TAG = LocationCollectingImplementation.class.getSimpleName();

    Context mCallBack;

    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private PendingIntent mLocationPendingIntent;

    private FusedLocationProviderClient mFusedLocationClient = null;

    private int gpsTrackingInterval = 5000;     //In milliseconds

    private boolean isTracking = false;

    public LocationCollectingImplementation(Context context) {
        this.mCallBack = context;
    }

    @Override
    public void onStarts() {
        Log.d(TAG, "onStarts");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mCallBack);
    }


    @Override
    public void onDestroys() {
        Log.d(TAG, "onDestroys");
            mFusedLocationClient = null;
    }

    @Override
    public void createLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(gpsTrackingInterval);
        locationRequest.setFastestInterval(gpsTrackingInterval);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void createLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    @Override
    public void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates");
        if (mFusedLocationClient == null ) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mCallBack);
        }
        if (mFusedLocationClient != null) {
            stopLocationUpdates();
            Intent intent = new Intent(mCallBack, LocationChangedReceiver.class);
            mLocationPendingIntent = PendingIntent.getService(mCallBack, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (!isTracking) {      //If FusedLocation is not tracking
                if (ActivityCompat.checkSelfPermission(mCallBack, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mCallBack, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //Permission request should already be handled
                    return;
                }
                mFusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        mLocationPendingIntent);
                isTracking = true;
            }
        }
    }

    @Override
    public Location getLastKnownLocation() {
        final Location[] lastLocation = {null};
        if (Utilities.checkPermission((AppCompatActivity) mCallBack, Manifest.permission.ACCESS_FINE_LOCATION)) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Executor) this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(final Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                lastLocation[0] = location;
                            }

                        }
                    });
        }
        return lastLocation[lastLocation.length - 1];
    }

    @Override
    public void stopLocationUpdates() {
        if (isTracking) {
            isTracking = false;
            mFusedLocationClient.removeLocationUpdates(mLocationPendingIntent);
        }
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

}
