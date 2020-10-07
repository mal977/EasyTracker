package com.ntu.staizen.EasyTracker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Utilities {

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
}
