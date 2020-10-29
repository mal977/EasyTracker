package com.ntu.staizen.EasyTracker.ui.jobList;

import com.ntu.staizen.EasyTracker.model.JobData;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JobListViewModel extends ViewModel {
    private static String TAG = JobListViewModel.class.getSimpleName();

    MutableLiveData<ArrayList<JobData>> pastJobDataHistory = new MutableLiveData<>();

    LiveData<ArrayList<JobData>> getJobDataState(){
        return pastJobDataHistory;
    }

    public void updatePastJobHistory(){
        ArrayList<JobData> jobDataArrayList = new ArrayList<>();
        jobDataArrayList.add(new JobData("MalCo1", System.currentTimeMillis(), System.currentTimeMillis() + 1000));
        JobData j = new JobData("MalCo2", System.currentTimeMillis(), System.currentTimeMillis() + 1000);
        j.setUID("123124sad12sad");
        jobDataArrayList.add(j);
//        jobDataArrayList.add(j);
//        jobDataArrayList.add(j);
//        jobDataArrayList.add(j);
//        jobDataArrayList.add(j);
        jobDataArrayList.add(new JobData("MalCo3", System.currentTimeMillis(), System.currentTimeMillis() + 1000));

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
