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
    private long id=0;

    private String companyName;
    private long dateTimeStart;
    private long dateTimeEnd;
    private List<LocationData> locationDataList;

    public JobData() {
    }

    public JobData( String companyName,  long dateTimeStart, long dateTimeEnd) {
        this.companyName = companyName;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
    }

    public JobData( String companyName, long dateTimeStart, long dateTimeEnd, List<LocationData> locationDataList) {
        this.companyName = companyName;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.locationDataList = locationDataList;
    }



    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
        return ("ID: " + id
                + " companyName: " + companyName
                + " dateTimeStart: " + dateTimeStart
                + " dateTimeEnd: " + dateTimeEnd
        );
    }

    @Exclude
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
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
