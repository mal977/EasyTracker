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

    private String UID;
    private String company; //This is actually companyName, but nicholas is a dumbass and named the variable in firestore as comapny
    private long dateTimeStart;
    private long dateTimeEnd;
    private List<LocationData> location;

    public JobData() {
    }

    public JobData( String companyName,  long dateTimeStart, long dateTimeEnd) {
        this.company = companyName;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
    }

    public JobData( String companyName, long dateTimeStart, long dateTimeEnd, List<LocationData> locationDataList) {
        this.company = companyName;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.location = locationDataList;
    }

    @Exclude
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Exclude
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public List<LocationData> getLocation() {
        return location;
    }

    public void setLocation(List<LocationData> location) {
        this.location = location;
    }

    public void addLocationData(LocationData locationData) {
        if (this.location == null) {
            location = new ArrayList<LocationData>();
        }
        location.add(locationData);
    }

    public String toString() {
        return ("ID: " + id
                + " UID: " + UID
                + " companyName: " + company
                + " dateTimeStart: " + dateTimeStart
                + " dateTimeEnd: " + dateTimeEnd
        );
    }

}
