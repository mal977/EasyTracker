package com.ntu.staizen.myapplication.api.param;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class ExampleParam {

    @SerializedName("date_time")
    private LocalDateTime dateTime;

    public String getDateTime() {
        // we need to truncate from 2020-02-22T14:27:15.976195 to 2020-02-22T14:27:15
        return dateTime.toString().substring(0,19);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public ExampleParam(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
