package com.ntu.staizen.EasyTracker;

import android.content.Context;
import android.content.SharedPreferences;

import javax.annotation.Nullable;

public class SharedPreferenceHelper {

    public static String KEY_USERNAME = "username";
    public static String KEY_PHONE_NUMBER = "phone_number";
    public static String PREFERENCE_FILE_KEY = "com.ntu.staizen.EasyTracker";

    public static void setPreferences(String key, String value, Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Nullable
    public static String getPreference(String key, Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
