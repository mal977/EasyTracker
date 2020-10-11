package com.ntu.staizen.EasyTracker.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@IgnoreExtraProperties
@Entity
public class JobData {

    @Id
    private Long id;

    private String jobName;
    private String companyName;
    private String phoneNo;
    private long dateTimeStart;
    private long dateTimeEnd;
    private List<LocationData> locationDataList;

    public JobData() {
    }

    public JobData(String jobName, String companyName, String phoneNo, long dateTimeStart, long dateTimeEnd) {
        this.jobName = jobName;
        this.companyName = companyName;
        this.phoneNo = phoneNo;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
    }

    public JobData(String jobName, String companyName, String phoneNo, long dateTimeStart, long dateTimeEnd, List<LocationData> locationDataList) {
        this.jobName = jobName;
        this.companyName = companyName;
        this.phoneNo = phoneNo;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.locationDataList = locationDataList;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public long getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(long dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public long getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(long dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public String toString() {
        return ("ID: " + (id == null ? "0" : id.toString())
                + " jobName: " + jobName.toString()
                + " companyName: " + jobName.toString()
                + " phoneNo: " + jobName.toString()
                + " dateTimeStart: " + jobName.toString()
                + " dateTimeEnd: " + jobName.toString()
        );
    }

    @Exclude
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LocationData> getLocationDataList() {
        return locationDataList;
    }

    public void setLocationDataList(List<LocationData> locationDataList) {
        this.locationDataList = locationDataList;
    }

    public void addLocationData(LocationData locationData) {
        if (this.locationDataList == null) {
            locationDataList = new ArrayList<LocationData>();
        }
        locationDataList.add(locationData);
    }
}
