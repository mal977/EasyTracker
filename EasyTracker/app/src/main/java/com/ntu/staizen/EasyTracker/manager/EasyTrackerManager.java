package com.ntu.staizen.EasyTracker.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.SharedPreferenceHelper;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.firebase.Authentication;
import com.ntu.staizen.EasyTracker.firebase.FireStore;
import com.ntu.staizen.EasyTracker.database.BoxHelper;
import com.ntu.staizen.EasyTracker.location.LocationCollectingImplementation;
import com.ntu.staizen.EasyTracker.location.TrackingService;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.LocationData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static com.ntu.staizen.EasyTracker.Utilities.TRACKING_NOTIFICATION_CHANNEL_ID;

/**
 * Created by Malcom on 11 Oct 2020
 * This class is responsible for managing location data and sending it to the appropriate jobs.
 */
public class EasyTrackerManager {
    private static final String TAG = EasyTrackerManager.class.getSimpleName();


    private Context mContext;
    private static EasyTrackerManager instance;
    private Authentication mAuthentication;
    private FireStore mFireStore;
    private BoxHelper mBoxHelper;
    private LocationCollectingImplementation locationCollectingImplementation;
    private boolean tracking = false;

    private DatabaseReference currentRunningJobReference = null;

    public static synchronized EasyTrackerManager getInstance(Context context) {
        if (instance == null) {
            instance = new EasyTrackerManager(context.getApplicationContext());
        }
        return instance;
    }

    @Nullable
    public static synchronized EasyTrackerManager getInstance() {
        return instance;
    }

    public EasyTrackerManager(Context context) {
        this.mContext = context;
        mAuthentication = Authentication.getInstance(mContext);
        mFireStore = FireStore.getInstance(mContext);
        mBoxHelper = BoxHelper.getInstance(mContext);
        locationCollectingImplementation = new LocationCollectingImplementation(context);


        if (SharedPreferenceHelper.doesValueExist(SharedPreferenceHelper.KEY_RUNNING_JOB, mContext)) {
            String reference = SharedPreferenceHelper.getPreference(SharedPreferenceHelper.KEY_RUNNING_JOB, mContext);
            currentRunningJobReference = mFireStore.getReference().child("contractors/" + mAuthentication.getUID()).child("jobList/" + reference);
        }
    }

    //Starts a foreground service to collect location updates.
    public void startLocationUpdates(Context context) {
        Log.d(TAG, "startLocationUpdates");
        if (!tracking) {
            Intent intent = new Intent(mContext,TrackingService.class);
            mContext.startService(intent);
            tracking = true;

        } else {
            Log.d(TAG, "Already Tracking");
        }
    }

    //Uses the locationCollectionImplementation to do short burst location collection.
    public void startLocationUpdates(Context context, int interval) {
        Log.d(TAG, "startLocationUpdates");
        if (!tracking) {
            locationCollectingImplementation.createLocationRequest(interval);
            locationCollectingImplementation.createLocationSettingsRequest();
            locationCollectingImplementation.startLocationUpdates();
            tracking = true;
        } else {
            Log.d(TAG, "Already Tracking");
        }
    }

    public void stopLocationUpdates() {
        Log.d(TAG, "stopLocationUpdates()");
        if (tracking) {
            locationCollectingImplementation.stopLocationUpdates();
            tracking = false;
        }
    }

    @Nullable
    public String startNewJob(JobData jobData) {
        if (mAuthentication != null) {
            if (currentRunningJobReference == null) {       //Prevents user from starting new job while a currentJob is running
                currentRunningJobReference = mFireStore.sendNewJobToFireStore(mAuthentication.getmAuth().getUid(), jobData);
                if (currentRunningJobReference != null) {
                    jobData.setUID(currentRunningJobReference.getKey());
                    mBoxHelper.addJobData(jobData);
                    startLocationUpdates(mContext);
                    SharedPreferenceHelper.setPreferences(SharedPreferenceHelper.KEY_RUNNING_JOB, currentRunningJobReference.getKey(), mContext);
                }
            }
        }
        return currentRunningJobReference.getKey();
    }

    public void endCurrentRunningJob(JobData jobData) {
        if (mAuthentication != null) {
            if (currentRunningJobReference == null) {
                String reference = SharedPreferenceHelper.getPreference(SharedPreferenceHelper.KEY_RUNNING_JOB, mContext);
                mFireStore.sendEndJobToFireStore(Authentication.getInstance(mContext).getmAuth().getUid(), reference, jobData.getDateTimeEnd());
            } else {
                mFireStore.sendEndJobToFireStore(currentRunningJobReference, jobData.getDateTimeEnd());
            }
            mBoxHelper.updateJobData(jobData);
            SharedPreferenceHelper.removeValue(SharedPreferenceHelper.KEY_RUNNING_JOB, mContext);
            stopLocationUpdates();
        }
    }

    public boolean isCurrentJobTracking() {
        return SharedPreferenceHelper.doesValueExist(SharedPreferenceHelper.KEY_RUNNING_JOB, mContext);
    }


    @Nullable
    public JobData checkAndResumeTrackingJob() {
        Log.d(TAG, "checkAndResumeTrackingJob");
        JobData jobData = null;
        if (isCurrentJobTracking()) {
            String uid = SharedPreferenceHelper.getPreference(SharedPreferenceHelper.KEY_RUNNING_JOB, mContext);
            jobData = BoxHelper.getInstance().getJobData(uid);
            if (!tracking) {
                startLocationUpdates(mContext);
            }
        }
        return jobData;
    }


}

