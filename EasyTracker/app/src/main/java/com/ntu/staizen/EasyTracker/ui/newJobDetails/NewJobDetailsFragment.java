package com.ntu.staizen.EasyTracker.ui.newJobDetails;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.Utilities;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.manager.LocationManager;

import java.util.Calendar;

/**
 *
 */
public class NewJobDetailsFragment extends Fragment {
    private static String TAG = NewJobDetailsFragment.class.getSimpleName();

    private JobDetailsViewModel jobDetailsModel;

    private LocationManager locationManager;

    private TextView tvLatLon;
    private EditText etCompanyName;
    private EditText etDateTime;
    private Button btnStartNewJob;

    private Calendar date;

    public NewJobDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        jobDetailsModel = new ViewModelProvider(this).get(JobDetailsViewModel.class);

        locationManager = LocationManager.getInstance(getActivity());

        tvLatLon = view.findViewById(R.id.tv_lat_lon);
        etCompanyName = view.findViewById(R.id.et_company_name);
        etDateTime = view.findViewById(R.id.et_time);
        btnStartNewJob = view.findViewById(R.id.btn_end_job);


        jobDetailsModel.getCurrentLocationEvent().observe(getViewLifecycleOwner(), new Observer<LocationChangedEvent>() {
            @Override
            public void onChanged(LocationChangedEvent locationChangedEvent) {
                Location location = locationChangedEvent.getNewLocation();
                tvLatLon.setText("Lat: " + location.getLatitude() + " Lon: " + location.getLongitude());
            }
        });

        etDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currCalendar = Calendar.getInstance();
                date = Calendar.getInstance();


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.set(year, month, dayOfMonth);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                etDateTime.setText(date.getTime().toString());
                            }
                        }, currCalendar.get(Calendar.HOUR_OF_DAY), currCalendar.get(Calendar.MINUTE), false);
                        timePickerDialog.show();
                    }
                }, currCalendar.get(Calendar.YEAR), currCalendar.get(Calendar.MONTH), currCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnStartNewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validDetails = true;
                if (etCompanyName.getText().toString().isEmpty()) {
                    etCompanyName.setError("Please enter a Company Name!");
                    validDetails = false;
                }
                if (etDateTime.getText().toString().isEmpty()) {
                    etDateTime.setError("Please enter a Date Time!");
                    validDetails = false;
                }
                if (!validDetails)
                    return;
                String uid = jobDetailsModel.startNewJob(etCompanyName.getText().toString(), date.getTime());
                navController.navigate( NewJobDetailsFragmentDirections.actionNewJobDetailsToJobDetails(uid));

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        if (Utilities.isLocationEnabled(getContext())) {
            locationManager.startLocationUpdates(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_job_details, container, false);
    }

}