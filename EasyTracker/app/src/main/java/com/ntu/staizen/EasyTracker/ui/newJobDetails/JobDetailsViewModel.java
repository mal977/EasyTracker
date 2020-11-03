package com.ntu.staizen.EasyTracker.ui.newJobDetails;

import android.util.Log;

import com.ntu.staizen.EasyTracker.database.BoxHelper;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.manager.EasyTrackerManager;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.LocationData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.annotation.Nullable;

public class JobDetailsViewModel extends ViewModel {
    private static String TAG = JobDetailsViewModel.class.getSimpleName();

    private MutableLiveData<LocationData> currentLocationData = new MutableLiveData<>();

    public MutableLiveData<LocationData> getCurrentLocationData() {
        return currentLocationData;
    }

    private MutableLiveData<JobDetailState> jobDetailStateMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<JobDetailState> getJobDetailStateMutableLiveData() {
        return jobDetailStateMutableLiveData;
    }

    private JobData mJobData;

    public JobDetailsViewModel() {
        super();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus.getDefault().unregister(this);

    }

    public String startNewJob(String companyName, Date dateTime) {
        mJobData = new JobData();
        mJobData.setCompany(companyName);
        mJobData.setDateTimeStart(dateTime.getTime());
        return EasyTrackerManager.getInstance().startNewJob(mJobData);
    }

    public void getJobDetails(String UID){
        BoxHelper boxHelper = BoxHelper.getInstance();
        mJobData = boxHelper.getJobData(UID);
        if(mJobData!=null){
            if(boxHelper.getLatestLocationDataMatchingJob(UID)!=null) {
                currentLocationData.setValue(boxHelper.getLatestLocationDataMatchingJob(UID));
            }
        }
        jobDetailStateMutableLiveData.postValue(new JobDetailState(mJobData));
    }

    public void endJob(long dateTimeEnd){
        mJobData.setDateTimeEnd(dateTimeEnd);
        jobDetailStateMutableLiveData.postValue(new JobDetailState(mJobData));
        EasyTrackerManager easyTrackerManager = EasyTrackerManager.getInstance();
        easyTrackerManager.endCurrentRunningJob(mJobData);
    }

    @Nullable
    public String getUID(){
        return jobDetailStateMutableLiveData.getValue().getUID();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLocationChangedEvent(LocationChangedEvent event) {
        Log.d(TAG, "LocationChangedEvent Success");
        if (event != null && event.getNewLocation() != null) {
            Log.d(TAG, "New Location MainActivity: " + event.getNewLocation().toString());
        }
        if (event.getNewLocation() != null) {

            // If we are not tracking for a job, and we receive a location update, update the current location, and stop location updates.
            // Used to get location for new Job Fragment
            if(!EasyTrackerManager.getInstance().isCurrentJobTracking()){
                EasyTrackerManager.getInstance().stopLocationUpdates();
            }
            currentLocationData.setValue(new LocationData(System.currentTimeMillis(),event.getNewLocation().getLatitude(),event.getNewLocation().getLongitude()));
        }
    }

    public void setJobDetails(JobData jobData) {
        jobDetailStateMutableLiveData.postValue(new JobDetailState(jobData));
    }
}