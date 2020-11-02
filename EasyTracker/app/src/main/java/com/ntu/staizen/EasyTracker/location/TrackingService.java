package com.ntu.staizen.EasyTracker.location;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.SharedPreferenceHelper;
import com.ntu.staizen.EasyTracker.Utilities;
import com.ntu.staizen.EasyTracker.database.BoxHelper;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.firebase.Authentication;
import com.ntu.staizen.EasyTracker.firebase.FireStore;
import com.ntu.staizen.EasyTracker.manager.EasyTrackerManager;
import com.ntu.staizen.EasyTracker.model.LocationData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import static com.ntu.staizen.EasyTracker.Utilities.TRACKING_NOTIFICATION_CHANNEL_ID;

public class TrackingService extends Service implements ResultCallback<LocationSettingsResult> {
    private static final String TAG = TrackingService.class.getSimpleName();

    private DatabaseReference currentRunningJobReference = null;
    private FireStore mFireStore;
    private BoxHelper mBoxHelper;
    private Authentication mAuthentication;
    private LocationCallback locationCallback;
    private PendingIntent pendingIntent;

    public TrackingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EventBus.getDefault().register(this);
        mFireStore = FireStore.getInstance(getApplicationContext());
        mBoxHelper = BoxHelper.getInstance(getApplicationContext());
        mAuthentication = Authentication.getInstance(getApplicationContext());

        Notification notification = null;
        Intent trackingIntent = new Intent(this, TrackingService.class);
        pendingIntent = PendingIntent.getActivity(this, 0, trackingIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Log.d(TAG, "Building Notification Channel");
            CharSequence name = (TRACKING_NOTIFICATION_CHANNEL_ID);
            String description = ("This channel is used to display Easy Tracker Notifications.");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(TRACKING_NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
            notification = getNotification("Tracking");
        } else {
            notification = getNotification("Tracking");

        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(600000);
        locationRequest.setFastestInterval(300000);
        locationRequest.setMaxWaitTime(900000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.build();
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, "LocationChangedEvent Success");
                    if (location != null && location != null) {
                        Log.d(TAG, "New Location MainActivity: " + location.toString());
                    }
                    if (location != null) {
                        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                        LocationData locationData = new LocationData(System.currentTimeMillis(), loc.latitude, loc.longitude);
                        if (currentRunningJobReference == null) {
                            if (SharedPreferenceHelper.doesValueExist(SharedPreferenceHelper.KEY_RUNNING_JOB, getApplicationContext())) {
                                String reference = SharedPreferenceHelper.getPreference(SharedPreferenceHelper.KEY_RUNNING_JOB, getApplicationContext());
                                currentRunningJobReference = mFireStore.getReference().child("contractors/" + mAuthentication.getUID()).child("jobList/" + reference);
                            }
                        } else {
                            locationData.setJobID(currentRunningJobReference.getKey());
                            mFireStore.sendLocationUpdateToFireStore(currentRunningJobReference, locationData);
                            mBoxHelper.addLocationData(locationData);
                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            Date date = new Date();
                            Notification notification = getNotification("Tracking Lat:" + location.getLatitude() + " Lon:" + location.getLongitude() + " DateTime:" + Utilities.jobDateFormatter(date));
                            notificationManager.notify(1, notification);
                        }
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }

        startForeground(1, notification);
        return START_STICKY;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLocationChangedEvent(LocationChangedEvent event) {

    }

    private Notification getNotification(String message) {
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, TRACKING_NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("EasyTracker")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_wrench)
                    .setContentIntent(pendingIntent)
                    .setOnlyAlertOnce(true)
                    .build();
        } else {

            notification = new Notification.Builder(this)
                    .setContentTitle("EasyTracker")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_wrench)
                    .setContentIntent(pendingIntent)
                    .setOnlyAlertOnce(true)
                    .build();
        }
        return notification;
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }

    }
}
