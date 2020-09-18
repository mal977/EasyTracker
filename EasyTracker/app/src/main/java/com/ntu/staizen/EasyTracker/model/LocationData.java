package com.ntu.staizen.EasyTracker.model;

import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;

/**
 * Created by Malcom Teh on 18/9/2020.
 * <p>
 * This object stores location data.
 */

@Entity
public class LocationData {

    @Id
    private Long id;

    @Convert(converter = DateTimeConverter.class, dbType = Long.class)
    public LocalDateTime dateTime;

//    private LatLng latLng;

    public LocationData() {

    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public LatLng getLatLng() {
//        return latLng;
//    }
//
//    public void setLatLng(LatLng latLng) {
//        this.latLng = latLng;
//    }

    public static class DateTimeConverter implements PropertyConverter<LocalDateTime, Long> {

        @Override
        public LocalDateTime convertToEntityProperty(Long databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(databaseValue), TimeZone.getDefault().toZoneId());
                return localDateTime;
            } else {
                return null;
            }
        }

        @Override
        public Long convertToDatabaseValue(LocalDateTime entityProperty) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return entityProperty.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            } else {
                return null;
            }
        }
    }

}
