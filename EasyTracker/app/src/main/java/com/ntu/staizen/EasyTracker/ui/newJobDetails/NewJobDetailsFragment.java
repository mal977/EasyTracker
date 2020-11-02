package com.ntu.staizen.EasyTracker.ui.newJobDetails;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.Utilities;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.manager.EasyTrackerManager;
import com.ntu.staizen.EasyTracker.model.LocationData;

import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class NewJobDetailsFragment extends Fragment {
    private static String TAG = NewJobDetailsFragment.class.getSimpleName();

    private JobDetailsViewModel jobDetailsModel;

    private EasyTrackerManager locationManager;

    private TextView tvLatLon;
    private EditText etCompanyName;
    private EditText etDateTime;
    private Button btnStartNewJob;

    private Date date;

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

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("New Job");
        actionBar.show();

        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        jobDetailsModel = new ViewModelProvider(this).get(JobDetailsViewModel.class);

        locationManager = EasyTrackerManager.getInstance(getActivity());

        tvLatLon = view.findViewById(R.id.tv_lat_lon);
        etCompanyName = view.findViewById(R.id.et_company_name);
        etDateTime = view.findViewById(R.id.et_time);
        btnStartNewJob = view.findViewById(R.id.btn_end_job);

        date = new Date();
        etDateTime.setText(Utilities.jobDateFormatter(date));

        jobDetailsModel.getCurrentLocationData().observe(getViewLifecycleOwner(), new Observer<LocationData>() {
            @Override
            public void onChanged(LocationData locationData) {
                tvLatLon.setText("Lat: " + locationData.getLat() + " Lon: " + locationData.getLon());
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
                String uid = jobDetailsModel.startNewJob(etCompanyName.getText().toString(), date);
                if(uid!= null) {
                    navController.navigate(NewJobDetailsFragmentDirections.actionNewJobDetailsToJobDetails(uid));
                }else{
                    Toast.makeText(getContext(),"Error adding Job",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        if (Utilities.isLocationEnabled(getContext())) {
            locationManager.startLocationUpdates(getContext(),5000);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_job_details, container, false);
    }

}