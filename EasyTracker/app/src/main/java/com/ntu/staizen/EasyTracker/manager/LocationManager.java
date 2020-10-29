package com.ntu.staizen.EasyTracker.manager;

import android.content.Context;
import android.util.Log;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.firebase.Authentication;
import com.ntu.staizen.EasyTracker.firebase.FireStore;
import com.ntu.staizen.EasyTracker.database.BoxHelper;
import com.ntu.staizen.EasyTracker.location.LocationCollectingImplementation;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.LocationData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.annotation.Nullable;

/**
 * Created by Malcom on 11 Oct 2020
 * This class is responsible for managing location data and sending it to the appropriate jobs.
 */
public class LocationManager {

    private static final String TAG = LocationManager.class.getSimpleName();


    private Context mContext;
    private static LocationManager instance;
    private Authentication mAuthentication;
    private FireStore mFireStore;
    private BoxHelper mBoxHelper;
    private boolean tracking = false;

    private DatabaseReference currentRunningJobReference = null;

    public static synchronized LocationManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocationManager(context.getApplicationContext());
        }
        return instance;
    }

    @Nullable
    public static synchronized LocationManager getInstance() {
        return instance;
    }

    public LocationManager(Context context) {
        this.mContext = context;
        mAuthentication = Authentication.getInstance(mContext);
        mFireStore = FireStore.getInstance(mContext);
        mBoxHelper = BoxHelper.getInstance(mContext);
        EventBus.getDefault().register(this);


    }

    public void startLocationUpdates(Context context) {
        if(!tracking) {
            LocationCollectingImplementation locationCollectingImplementation = new LocationCollectingImplementation(context);
            locationCollectingImplementation.createLocationRequest();
            locationCollectingImplementation.createLocationSettingsRequest();
            locationCollectingImplementation.startLocationUpdates();
        }else{
            Log.d(TAG,"Already Tracking");
        }
    }

    public String startNewJob(JobData jobData) {
        if (mAuthentication != null) {
            if (currentRunningJobReference == null) {       //Prevents user from starting new job while a currentJob is running
                currentRunningJobReference = mFireStore.sendNewJobToFireStore(mAuthentication.getmAuth().getUid(), jobData);
                jobData.setUID(currentRunningJobReference.getKey());
                mBoxHelper.addJobData(jobData);
//                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Utilities.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("Current_Running_Job", currentRunningJobReference.getKey());
//                editor.apply();
                startLocationUpdates(mContext);
            }
        }
        return currentRunningJobReference.getKey();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLocationChangedEvent(LocationChangedEvent event) {
        Log.d(TAG, "LocationChangedEvent Success");
        if (event != null && event.getNewLocation() != null) {
            Log.d(TAG, "New Location MainActivity: " + event.getNewLocation().toString());
        }
        if (event.getNewLocation() != null) {
            LatLng loc = new LatLng(event.getNewLocation().getLatitude(), event.getNewLocation().getLongitude());
            LocationData locationData = new LocationData(System.currentTimeMillis(), loc.latitude, loc.longitude);
            if (currentRunningJobReference == null) {
//                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Utilities.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
//                String storedRunningJobReference = sharedPreferences.getString("Current_Running_Job", "");
//                if (storedRunningJobReference != null & !storedRunningJobReference.isEmpty()) {
//                    Log.d(TAG, storedRunningJobReference);
//                    mFireStore.sendLocationUpdateToFireStore(mAuthentication.getmAuth().getUid(), storedRunningJobReference, locationData);
//                }
            } else {
                mFireStore.sendLocationUpdateToFireStore(currentRunningJobReference, locationData);
            }
        }
    }

}

