package com.ntu.staizen.EasyTracker.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Malcom Teh on 18/9/2020.
 * <p>
 * This object stores location data.
 */

@IgnoreExtraProperties
@Entity
public class LocationData {

    @Id
    private long id=0;

    private String jobID;
    private long dateTime;
    private double lat;
    private double lon;

    public LocationData() {}

    public LocationData(long dateTimeStamp, double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        this.dateTime = dateTimeStamp;
    }

    public LocationData(String jobID, long dateTimeStamp, double lat, double lon) {
        this.jobID = jobID;
        this.lat = lat;
        this.lon = lon;
        this.dateTime = dateTimeStamp;
    }

    @Exclude
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String toString(){
        return("ID: " + id
                + " JobID: " + jobID
                + " DateTime: " + dateTime
                + " Lat: " + lat
                + " Lon: " + lon);
    }

    @Exclude
    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }
}
