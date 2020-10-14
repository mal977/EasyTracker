package com.ntu.staizen.EasyTracker.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@IgnoreExtraProperties
@Entity
public class ContractorInfo {

    @Id
    private long id=0;

    private String name;
    private String phoneNo;

    private List<JobData> jobList;

    public ContractorInfo(){}

    public ContractorInfo(long id, String name, String phoneNo, List<JobData> jobList) {
        this.id = id;
        this.name = name;
        this.phoneNo = phoneNo;
        this.jobList = jobList;
    }

    public ContractorInfo(String name, String phoneNo, List<JobData> jobList) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.jobList = jobList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public List<JobData> getJobList() {
        return jobList;
    }

    public void setJobList(List<JobData> jobList) {
        this.jobList = jobList;
    }

    public String toString() {
        return ("ID: " + id
                + " name: " + name
                + " phoneNo: " + phoneNo
        );
    }
}
