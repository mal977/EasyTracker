package com.ntu.staizen.EasyTracker.ui.newJobDetails;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.manager.LocationManager;
import com.ntu.staizen.EasyTracker.model.JobData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewJobDetailsModel extends ViewModel {
    private static String TAG = NewJobDetailsModel.class.getSimpleName();

    private MutableLiveData<LocationChangedEvent> currentLocationEvent = new MutableLiveData<>();

    public MutableLiveData<LocationChangedEvent> getCurrentLocationEvent() {return currentLocationEvent;}

    public void setCurrentLocationEvent(MutableLiveData<LocationChangedEvent> currentLocationEvent) {this.currentLocationEvent = currentLocationEvent;}

    public NewJobDetailsModel() {
        super();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus.getDefault().unregister(this);

    }

    public void startNewJob(String companyName, Date dateTime){
        JobData jobData = new JobData();
        jobData.setCompany(companyName);
        jobData.setDateTimeStart(dateTime.getTime());
        LocationManager.getInstance().startNewJob(jobData);
    }

    public void setCurrentLocationEvent(LocationChangedEvent locationEvent){
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