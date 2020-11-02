package com.ntu.staizen.EasyTracker.ui.jobList;

import com.ntu.staizen.EasyTracker.database.BoxHelper;
import com.ntu.staizen.EasyTracker.model.JobData;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by Malcom Teh on 01/11/2020
 * View Model holding jobList data
 */
public class JobListViewModel extends ViewModel {
    private static String TAG = JobListViewModel.class.getSimpleName();

    MutableLiveData<ArrayList<JobData>> pastJobDataHistory = new MutableLiveData<>();

    LiveData<ArrayList<JobData>> getJobDataState(){
        return pastJobDataHistory;
    }

    public void updatePastJobHistory(){
        ArrayList<JobData> jobDataArrayList = BoxHelper.getInstance().getAllJobData();
        pastJobDataHistory.setValue(jobDataArrayList);
    }

    public void addNewJobData(JobData jobData){
        if(jobData==null){
            return;
        }

        ArrayList<JobData> existingData = pastJobDataHistory.getValue();
        existingData.add(jobData);
        pastJobDataHistory.setValue(existingData);
    }
}
