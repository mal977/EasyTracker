package com.ntu.staizen.EasyTracker.ui.newJobDetails;

import android.util.Log;

import com.ntu.staizen.EasyTracker.database.BoxHelper;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.manager.LocationManager;
import com.ntu.staizen.EasyTracker.model.JobData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JobDetailsModel extends ViewModel {
    private static String TAG = JobDetailsModel.class.getSimpleName();

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

    public JobDetailsModel() {
        super();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus.getDefault().unregister(this);

    }

    public String startNewJob(String companyName, Date dateTime) {
        JobData jobData = new JobData();
        jobData.setCompany(companyName);
        jobData.setDateTimeStart(dateTime.getTime());
        return LocationManager.getInstance().startNewJob(jobData);
    }

    public void getJobDetails(String UID){
        BoxHelper boxHelper = BoxHelper.getInstance();
        JobData jobData = boxHelper.getJobData(UID);

        jobDetailStateMutableLiveData.setValue(new JobDetailState(jobData));
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
            currentLocationEvent.setValue(event);
        }
    }

}