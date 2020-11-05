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

/**
 * Created by Malcom Teh
 * Shared view model for JobDetailsFragment and NewJobDetailsFragment
 *
 * If you are unsure how this view model works
 * @see <a href https://developer.android.com/topic/libraries/architecture/viewmodel></a>
 */
public class JobDetailsViewModel extends ViewModel {
    private static String TAG = JobDetailsViewModel.class.getSimpleName();

    protected MutableLiveData<LocationData> currentLocationData = new MutableLiveData<>();
    public MutableLiveData<LocationData> getCurrentLocationData() {
        return currentLocationData;
    }
    protected MutableLiveData<JobDetailState> jobDetailStateMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<JobDetailState> getJobDetailStateMutableLiveData() {return jobDetailStateMutableLiveData;
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

    /**
     * Creates a new JobData based of input data from NewJobDetail fragment
     * Sends the created JobData to EasyManager to start new job
     * @see com.ntu.staizen.EasyTracker.manager.EasyTrackerManager
     *
     * @param companyName
     * @param dateTime start Date of Job, uses System.currentTimeMillis
     * @return
     */
    public String startNewJob(String companyName, Date dateTime) {
        mJobData = new JobData();
        mJobData.setCompany(companyName);
        mJobData.setDateTimeStart(dateTime.getTime());
        return EasyTrackerManager.getInstance().startNewJob(mJobData);
    }

    /**
     * Fetches job details from BoxHelper with matching UID and updates mutable mJobData
     * Also updates the currentLocationData with matching uid
     * @param UID String UID to match to
     */
    public void getJobDetails(String UID){
        BoxHelper boxHelper = BoxHelper.getInstance();
        mJobData = boxHelper.getJobData(UID);
        if(mJobData!=null){
            if(boxHelper.getLatestLocationDataMatchingJob(UID)!=null) {
                //Get matching currentLocationData
                currentLocationData.setValue(boxHelper.getLatestLocationDataMatchingJob(UID));
            }
        }
        jobDetailStateMutableLiveData.postValue(new JobDetailState(mJobData));
    }

    /**
     * Sets the dateTimeEnd for current running job, and updates EasyTrackerManager
     * @param dateTimeEnd end date/time of job
     */
    public void endJob(long dateTimeEnd){
        mJobData.setDateTimeEnd(dateTimeEnd);
        jobDetailStateMutableLiveData.postValue(new JobDetailState(mJobData));
        EasyTrackerManager easyTrackerManager = EasyTrackerManager.getInstance();
        easyTrackerManager.endCurrentRunningJob(mJobData);
    }

    /**
     * Get UID of current job
     * @return String uid of job
     */
    @Nullable
    public String getUID(){
        return jobDetailStateMutableLiveData.getValue().getUID();
    }

    /**
     * Mainly used to get the first location for NewJobDetails
     * Thats why there is check for a currentJobTracking
     *
     * if there is no job tracking, means we are only looking for a starting location, so stop location updates
     * as they are not needed anymore.
     * @param event
     */
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