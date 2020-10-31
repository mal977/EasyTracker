package com.ntu.staizen.EasyTracker.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Malcom Teh on 18/9/2020.
 * Any question on how to use, ask Malcom
 */

public class BaseResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
