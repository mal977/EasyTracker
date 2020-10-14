package com.ntu.staizen.EasyTracker.model;

import android.os.Build;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

import androidx.annotation.RequiresApi;
import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;

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

//    @Exclude
//    @Convert(converter = DateTimeConverter.class, dbType = Long.class)
//    public LocalDateTime dateTime;

    private long dateTimeStamp;
    private double lat;
    private double lon;

    public LocationData() {}

    public LocationData(long dateTimeStamp, double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        this.dateTimeStamp = dateTimeStamp;
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

    public long getDateTimeStamp() {
        return this.dateTimeStamp;
    }

    public void setDateTimeStamp(long dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }
//    public LocalDateTime getDateTime() {
//        return dateTime;
//    }
//
//    public void setDateTime(LocalDateTime dateTime) {
//        this.dateTime = dateTime;
//    }

//    public static class DateTimeConverter implements PropertyConverter<LocalDateTime, Long> {
//
//        @Override
//        public LocalDateTime convertToEntityProperty(Long databaseValue) {
//            if (databaseValue == null) {
//                return null;
//            }
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(databaseValue), TimeZone.getDefault().toZoneId());
//                return localDateTime;
//            } else {
//                return null;
//            }
//        }
//
//        @Override
//        public Long convertToDatabaseValue(LocalDateTime entityProperty) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                return entityProperty.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//            } else {
//                return null;
//            }
//        }
//    }

    public String toString(){
        return("ID: " + id
                + " DateTime: " + dateTimeStamp
                + " Lat: " + lat
                + " Lon: " + lon);
    }

}
