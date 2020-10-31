package com.ntu.staizen.EasyTracker.ui.newJobDetails;

import android.util.Log;

import com.ntu.staizen.EasyTracker.database.BoxHelper;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.manager.EasyTrackerManager;
import com.ntu.staizen.EasyTracker.model.JobData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.annotation.Nullable;

public class JobDetailsViewModel extends ViewModel {
    private static String TAG = JobDetailsViewModel.class.getSimpleName();

    private MutableLiveData<LocationChangedEvent> currentLocationEvent = new MutableLiveData<>();

    public MutableLiveData<LocationChangedEvent> getCurrentLocationEvent() {
        return currentLocationEvent;
    }

    public void setCurrentLocationEvent(MutableLiveData<LocationChangedEvent> currentLocationEvent) {
        this.currentLocationEvent = currentLocationEvent;
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

    public void setCurrentLocationEvent(LocationChangedEvent locationEvent) {
        this.currentLocationEvent.setValue(locationEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLocationChangedEvent(LocationChangedEvent event) {
        Log.d(TAG, "LocationChangedEvent Success");
        if (event != null && event.getNewLocation() != null) {
            Log.d(TAG, "New Location MainActivity: " + event.getNewLocation().toString());
        }
        if (event.getNewLocation() != null) {
            if(currentLocationEvent.getValue()==null){
                EasyTrackerManager.getInstance().stopLocationUpdates();
            }
            currentLocationEvent.setValue(event);
        }
    }

    public void setJobDetails(JobData jobData) {
        jobDetailStateMutableLiveData.postValue(new JobDetailState(jobData));
    }
}