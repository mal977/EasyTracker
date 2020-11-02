package com.ntu.staizen.EasyTracker.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.ntu.staizen.EasyTracker.Utilities;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

/**
 * Created by Malcom Teh on 8/10/2020.
 */

public class LocationChangedReceiver extends IntentService {

    private static final String TAG = LocationChangedReceiver.class.getSimpleName();

    public LocationChangedReceiver() {
        super("LocationChangedReceiverIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (null == intent) {
            return;
        }

        Log.d(TAG, intent.getAction() + "Action Name");
        Bundle bundle = intent.getExtras();

        if (null == bundle) {
            return;
        }

        Location location = bundle.getParcelable("com.google.android.location.LOCATION");

        if (location != null) {
            Log.d(TAG, "Location Changed " + location.toString());
            EventBus.getDefault().post(new LocationChangedEvent(location));
        } else {
            Log.d(TAG, "Location Changed Null ");

        }

    }
}
