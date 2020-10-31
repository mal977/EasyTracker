package com.ntu.staizen.EasyTracker.database;

import android.content.Context;
import android.util.Log;

import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.JobData_;
import com.ntu.staizen.EasyTracker.model.LocationData;
import com.ntu.staizen.EasyTracker.model.MyObjectBox;

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

    private Context mContext;
    private static BoxStore mBoxStore;
    private static BoxHelper instance;

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

    private BoxHelper(@NonNull Context context) {

        setUpBoxDatabase(context);
    }

    private void setUpBoxDatabase(Context context) {
        this.mContext = context;
        mBoxStore = MyObjectBox.builder().androidContext(mContext).build();
    }

    /**
     * Adds arrayList of LocationData to database
     *
     * @param locationData - ArrayList of LocationData, when ID is null, objectbox automatically assigns an ID
     */
    public void addLocationData(ArrayList<LocationData> locationData) {
        Log.d(TAG, "addLocationData(LocationData locationData[])");
        try {

            Box<LocationData> locationDataBox = mBoxStore.boxFor(LocationData.class);
            for (LocationData ld : locationData
            ) {
                locationDataBox.put(ld);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
        }

        return locationDataArrayList;
    }

    public void addJobData(JobData jobData) {
        Log.d(TAG, "addJobData(JobData jobData)" + jobData.toString());

        try {
            Box<JobData> jobDataBox = mBoxStore.boxFor(JobData.class);
            jobDataBox.put(jobData);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Nullable
    public JobData getJobData(String UID) {
        Log.d(TAG, "getJobData(String UID)" + UID);

        JobData jobData = null;
        try {
            Box<JobData> jobDataBox = mBoxStore.boxFor(JobData.class);
            jobData = jobDataBox.query().equal(JobData_.UID, UID).build().find().get(0);

        } catch (Exception e) {
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
            e.printStackTrace();
        }

        return jobDataArrayList;
    }

    public void updateJobData(JobData jobData){
        Log.d(TAG, "updateJobData(JobData jobData)");

        try{
            Box<JobData> jobDataBox = mBoxStore.boxFor(JobData.class);
            jobDataBox.put(jobData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
