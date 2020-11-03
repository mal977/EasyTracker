package com.ntu.staizen.EasyTracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.ntu.staizen.EasyTracker.services.LocationChangedReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
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
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
    }

    public static void showGPSPrompt(Context context){
        new AlertDialog.Builder(context)
                .setMessage("Easy Tracker requires location updates to perform core functionality! Please enable your location services!")
                .setPositiveButton("Enable Location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
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
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return output;
    }
}
