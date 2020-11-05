package com.ntu.staizen.EasyTracker.ui.newJobDetails;

import com.ntu.staizen.EasyTracker.model.JobData;

import androidx.annotation.Nullable;

/**
 * Created by Malcom
 */
public class JobDetailState {

    @Nullable
    private String UID;
    @Nullable
    private String companyName;
    @Nullable
    private long start;
    @Nullable
    private long end;

    public JobDetailState(String uid, String companyName, long start, long end) {
        this.UID = uid;
        this.companyName = companyName;
        this.start = start;
        this.end = end;
    }

    public JobDetailState(JobData jobData) {
        if (jobData == null) {
            return;
        }
        this.UID = jobData.getUID();
        this.companyName = jobData.getCompany();
        this.start = jobData.getDateTimeStart();
        this.end = jobData.getDateTimeEnd();
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

    public long getEnd() {
        return end;
    }
}
