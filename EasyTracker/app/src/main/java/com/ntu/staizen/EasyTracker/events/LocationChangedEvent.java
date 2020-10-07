package com.ntu.staizen.EasyTracker.events;

import android.location.Location;

/**
 * Created by Malcom Teh on 28/6/2017.
 */

public class LocationChangedEvent {

    private Location newLocation;

    public LocationChangedEvent(Location newLocation){
        this.newLocation = newLocation;
    }

    public Location getNewLocation(){
        return newLocation;
    }
}
