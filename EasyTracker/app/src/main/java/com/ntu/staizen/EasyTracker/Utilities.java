package com.ntu.staizen.EasyTracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.ntu.staizen.EasyTracker.services.LocationChangedReceiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.app.Notification.FLAG_ONLY_ALERT_ONCE;

public class Utilities {
    private static final String TAG = Utilities.class.getSimpleName();
    public static String NOTIFICATION_CHANNEL_ID = "easy_tracker_notification_channel";
    public static String TRACKING_NOTIFICATION_CHANNEL_ID = "tracking_notification";

    public static boolean checkPermission(AppCompatActivity activity, String appPermission) {
        int permissionEnabled = ActivityCompat.checkSelfPermission(activity, appPermission);
        if (permissionEnabled == PackageManager.PERMISSION_DENIED) {
            String[] temp = {appPermission};
            ActivityCompat.requestPermissions(activity, temp, 0);
            return false;
        }
        return true;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * This method builds a notification to notify the user location updates
     */
    public static void displayTrackingNotification(Context context, String message) {

        /**
         * ToDo Add intent to action list
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            Log.d(TAG, "Building Notification Channel");
            CharSequence name = (NOTIFICATION_CHANNEL_ID);
            String description = ("This channel is used to display Easy Tracker Notifications.");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(TRACKING_NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }

        //ToDo Update below code to bring user to job details page
//        Intent intent = new Intent(context,MapsActivity.class);
//        intent.putExtra("isNotification","true");
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Tracking Location");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, TRACKING_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_wrench)
                .setContentTitle("Tracking Location")
                .setContentText(message)
                .setStyle(bigTextStyle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // ToDo ID has to be unique code generated
        Notification n = builder.build();
        n.flags = FLAG_ONLY_ALERT_ONCE;
        notificationManager.notify(1,n );

    }

}
