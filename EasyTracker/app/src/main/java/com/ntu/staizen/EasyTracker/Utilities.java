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

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.app.Notification.FLAG_ONLY_ALERT_ONCE;

public class Utilities {
    private static final String TAG = Utilities.class.getSimpleName();
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * Takes in date
     * returns formatted string representing date in hh:mm EEEE dd/MM/yyyy
     * @param date
     * @return
     */
    public static String jobDateFormatter(Date date) {
        String output = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm EEEE dd/MM/yyyy");
        try {
            output = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
