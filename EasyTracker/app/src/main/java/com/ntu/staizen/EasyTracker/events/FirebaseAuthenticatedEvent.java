package com.ntu.staizen.EasyTracker.events;

import android.location.Location;

/**
 * Created by Malcom Teh on 28/6/2017.
 * Broadcasts when Firebase has authenticated user
 */

public class FirebaseAuthenticatedEvent {

    private Integer authenticatedStatus;    //0=FAILED, 1 = Sucesss
    private String errorMessage;

    public FirebaseAuthenticatedEvent(Integer authenticatedStatus){
        this.authenticatedStatus = authenticatedStatus;
    }

    public FirebaseAuthenticatedEvent(Integer authenticatedStatus, String errorMessage){
        this.authenticatedStatus = authenticatedStatus;
        this.errorMessage = errorMessage;
    }

    public Integer getAuthenticatedStatus() {
        return authenticatedStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
