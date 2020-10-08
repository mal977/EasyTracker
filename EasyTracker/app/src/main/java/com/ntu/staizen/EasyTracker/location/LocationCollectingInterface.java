package com.ntu.staizen.EasyTracker.location;

import android.location.Location;

/**
 * Created by Malcom Teh on 82/10/2020.
 */

public interface LocationCollectingInterface {

    void createLocationRequest();

    void createLocationSettingsRequest();

    void startLocationUpdates();

    void stopLocationUpdates();

    void onStarts();

    void onDestroys();

    Location getLastKnownLocation();
}
