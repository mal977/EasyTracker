package com.ntu.staizen.EasyTracker.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.firebase.Authentication;
import com.ntu.staizen.EasyTracker.firebase.FireStore;
import com.ntu.staizen.EasyTracker.model.LocationData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 *  Created by Malcom on 11 Oct 2020
 *  This class is responsible for managing location data and sending it to the appropriate jobs.
 */
public class LocationManager {

    private static final String TAG = LocationManager.class.getSimpleName();


    private Context mContext;
    private static LocationManager instance;
    private Authentication mAuthentication;
    private FireStore mFireStore;

    public static synchronized LocationManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocationManager(context.getApplicationContext());
        }
        return instance;
    }

    public LocationManager(Context context) {
        this.mContext = context;
        mAuthentication = Authentication.getInstance(mContext);
        mFireStore = FireStore.getInstance(mContext);
        EventBus.getDefault().register(this);


    }

    public void newLocationUpdate(LocationData locationData){
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLocationChangedEvent(LocationChangedEvent event) {
        Log.d(TAG, "LocationChangedEvent Success");
        if (event != null && event.getNewLocation() != null) {
            Log.d(TAG, "New Location MainActivity: " + event.getNewLocation().toString());
        }
        if (event.getNewLocation() != null) {
            LatLng loc = new LatLng(event.getNewLocation().getLatitude(), event.getNewLocation().getLongitude());
            LocationData locationData = new LocationData(System.currentTimeMillis(), loc.latitude,loc.longitude);
            mFireStore.sendLocationUpdateToFireStore(mAuthentication.getUID(),locationData);
        }
    }

}

