package com.ntu.staizen.EasyTracker.database;

import android.content.Context;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.JobData_;
import com.ntu.staizen.EasyTracker.model.LocationData;
import com.ntu.staizen.EasyTracker.model.LocationData_;
import com.ntu.staizen.EasyTracker.model.MyObjectBox;

import java.time.LocalDate;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Malcom Teh on 18/9/2020.
 * Any question on how to use, ask Malcom
 * Refer to https://docs.objectbox.io/android
 * <p>
 * This class implements database functionality.
 */
public class BoxHelper {
    private static String TAG = BoxHelper.class.getSimpleName();

    protected static BoxStore mBoxStore;
    protected static BoxHelper instance;

    /*
    Call this method to get an instance of the DaoHelper class
    Use the instance to access DaoHelper actions
    This method will generate a new DaoHelper instance, if the instance does not exist.
     */
    public static synchronized BoxHelper getInstance(Context context) {
        if (instance == null) {
            instance = new BoxHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Nullable
    public static synchronized BoxHelper getInstance() {
        return instance;
    }

    public BoxHelper() {
    }

    private BoxHelper(@NonNull Context context) {

        setUpBoxDatabase(context);
    }

    private void setUpBoxDatabase(Context context) {
        mBoxStore = MyObjectBox.builder().androidContext(context).build();
    }

    /**
     * Adds arrayList of LocationData to database
     *
     * @param locationData - ArrayList of LocationData, when ID is null, objectbox automatically assigns an ID
     */
    public void addLocationDataList(ArrayList<LocationData> locationData) {
        Log.d(TAG, "addLocationData(LocationData locationData[])");
        try {

            Box<LocationData> locationDataBox = mBoxStore.boxFor(LocationData.class);
            for (LocationData ld : locationData
            ) {
                locationDataBox.put(ld);
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    public void addLocationData(LocationData locationData) {
        Log.d(TAG, "addLocationData(LocationData locationData)");
        try {

            Box<LocationData> locationDataBox = mBoxStore.boxFor(LocationData.class);
            locationDataBox.put(locationData);

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    public ArrayList<LocationData> getLocationDataMatchingJob(String jobID) {
        Log.d(TAG, "getLocationDataMatchingJob(String jobID)");
        ArrayList<LocationData> locationDataArrayList = new ArrayList<>();

        try {

            Box<LocationData> locationDataBox = mBoxStore.boxFor(LocationData.class);
            locationDataArrayList = new ArrayList<>(locationDataBox.query().equal(LocationData_.jobID, jobID).build().find());


        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return locationDataArrayList;
    }

    /**
     * Gets an arrayList of LocationData from database
     */
    public ArrayList<LocationData> getLocationData() {
        Log.d(TAG, "getLocationData()");
        ArrayList<LocationData> locationDataArrayList = new ArrayList<>();
        try {

            Box<LocationData> locationDataBox = mBoxStore.boxFor(LocationData.class);
            locationDataArrayList.addAll(locationDataBox.getAll());
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        return locationDataArrayList;
    }

    /**
     * Gets the latest location data matching jobID
     */
    public LocationData getLatestLocationDataMatchingJob(String jobUID) {
        Log.d(TAG, "getLatestLocationDataMathingJob(String jobUID)" + jobUID);
        LocationData latestLocationData = null;
        try {

            Box<LocationData> locationDataBox = mBoxStore.boxFor(LocationData.class);
            ArrayList<LocationData> localDataArrayList = new ArrayList<>(locationDataBox.query().equal(LocationData_.jobID, jobUID).orderDesc(LocationData_.dateTime).build().find());
            if (!localDataArrayList.isEmpty()) {
                latestLocationData = localDataArrayList.get(0);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        return latestLocationData;
    }

    public void addJobData(JobData jobData) {
        Log.d(TAG, "addJobData(JobData jobData)" + jobData.toString());

        try {
            Box<JobData> jobDataBox = mBoxStore.boxFor(JobData.class);
            jobDataBox.put(jobData);

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

    }

    @Nullable
    public JobData getJobData(String UID) {
        Log.d(TAG, "getJobData(String UID)" + UID);

        JobData jobData = null;
        try {
            Box<JobData> jobDataBox = mBoxStore.boxFor(JobData.class);
            jobData = jobDataBox.query().equal(JobData_.UID, UID).build().findFirst();

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        return jobData;
    }

    @Nullable
    public ArrayList<JobData> getAllJobData() {
        Log.d(TAG, "getAllJobData()");
        ArrayList<JobData> jobDataArrayList = null;
        try {
            Box<JobData> jobDataBox = mBoxStore.boxFor(JobData.class);
            jobDataArrayList = new ArrayList<>(jobDataBox.getAll());

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        return jobDataArrayList;
    }

    public void updateJobData(JobData jobData) {
        Log.d(TAG, "updateJobData(JobData jobData)");

        try {
            Box<JobData> jobDataBox = mBoxStore.boxFor(JobData.class);
            jobDataBox.put(jobData);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }
}
