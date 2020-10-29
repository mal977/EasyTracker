package com.ntu.staizen.EasyTracker.ui.newJobDetails;

import android.location.Location;

import com.ntu.staizen.EasyTracker.model.JobData;

import androidx.annotation.Nullable;

public class JobDetailState {

    @Nullable
    private String UID;
    @Nullable
    private String companyName;
    @Nullable
    private long start;

    public JobDetailState(String uid, String companyName, long start) {
        this.UID = uid;
        this.companyName = companyName;
        this.start = start;

    }

    public JobDetailState(JobData jobData) {
        this.UID = jobData.getUID();
        this.companyName = jobData.getCompany();
        this.start = jobData.getDateTimeStart();
    }

    @Nullable
    public String getUID() {
        return UID;
    }

    @Nullable
    public String getCompanyName() {
        return companyName;
    }

    public long getStart() {
        return start;
    }
}
