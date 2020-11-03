package com.ntu.staizen.EasyTracker.ui.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ntu.staizen.EasyTracker.R;

/**
 * Created by Malcom on 3 Nov 2020
 */
public class SettingsFragment extends PreferenceFragmentCompat {


    public SettingsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Settings");
        actionBar.show();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences,rootKey);
        EditTextPreference pref = (EditTextPreference) findPreference("interval");
        if (pref != null) {
            pref.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
        }
        EditTextPreference prefmax_interval = (EditTextPreference) findPreference("max_interval");
        if (prefmax_interval != null) {
            prefmax_interval.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
        }
        EditTextPreference prefmin_interval = (EditTextPreference) findPreference("min_interval");
        if (prefmin_interval != null) {
            prefmin_interval.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_settings, container, false);
//    }
}