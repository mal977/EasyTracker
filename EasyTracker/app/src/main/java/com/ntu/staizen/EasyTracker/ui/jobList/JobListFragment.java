package com.ntu.staizen.EasyTracker.ui.jobList;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.firebase.Authentication;
import com.ntu.staizen.EasyTracker.firebase.FireStore;
import com.ntu.staizen.EasyTracker.manager.LocationManager;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.LocationData;
import com.ntu.staizen.EasyTracker.ui.jobDetails.JobDetailsFragment;
import com.ntu.staizen.EasyTracker.ui.newJobDetails.JobDetailState;
import com.ntu.staizen.EasyTracker.ui.newJobDetails.JobDetailsViewModel;

import java.util.ArrayList;

/**
 *
 */
public class JobListFragment extends Fragment {
    private static String TAG = JobListFragment.class.getSimpleName();

    static int counter = 0;

    private JobListAdapter jobListAdapter;
    private RecyclerView jobListRecyclerView;
    private ArrayList<JobData> jobDataArrayList = new ArrayList<>();

    private JobListViewModel jobListViewModel;
    private JobDetailsViewModel jobDetailsViewModel;

    private boolean locationPermissionGranted = false;
    private boolean isJobRunning = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    public JobListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        jobListViewModel.updatePastJobHistory();
        getLocationPermission();

        LocationManager locationManager = LocationManager.getInstance(getActivity());
        if(locationManager.isCurrentJobTracking()){
            JobData jobData = locationManager.getCurrentTrackingJob();
            jobDetailsViewModel.setJobDetails(jobData);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.hide();

        jobListViewModel = new ViewModelProvider(this).get(JobListViewModel.class);
        jobDetailsViewModel = new ViewModelProvider(getActivity()).get(JobDetailsViewModel.class);
        NavController navController = Navigation.findNavController(view);
        Authentication authentication = Authentication.getInstance(getContext());
        if (authentication.getmAuth().getCurrentUser() == null) {
            navController.navigate(R.id.loginFragment);
        } else {
            TextView tvUID = view.findViewById(R.id.tv_uid);
            tvUID.setText("UID: " + authentication.getmAuth().getCurrentUser().getUid());
        }

        jobListRecyclerView = view.findViewById(R.id.rv_job_list);
        jobListAdapter = new JobListAdapter(getContext(), jobDataArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        jobListRecyclerView.setLayoutManager(linearLayoutManager);
        jobListRecyclerView.setAdapter(jobListAdapter);

        Button start_new_job = (Button) view.findViewById(R.id.btn_start_new_job);
        Button btn_temp = (Button) view.findViewById(R.id.btn_temp);
        TextView tv_no_jobs = view.findViewById(R.id.tv_no_jobs);

        jobListViewModel.getJobDataState().observe(getViewLifecycleOwner(), new Observer<ArrayList<JobData>>() {
            @Override
            public void onChanged(ArrayList<JobData> vmJobDataArrayList) {
                if(vmJobDataArrayList==null){
                    tv_no_jobs.setVisibility(View.VISIBLE);
                }else {
                    tv_no_jobs.setVisibility(View.GONE);
                    jobDataArrayList = new ArrayList<>(vmJobDataArrayList);
                    jobListAdapter.setNewJobListData(jobDataArrayList);
                }
            }
        });

        jobDetailsViewModel.getJobDetailStateMutableLiveData().observe(getViewLifecycleOwner(), new Observer<JobDetailState>() {
            @Override
            public void onChanged(JobDetailState jobDetailState) {
                if (jobDetailState != null && jobDetailState.getUID() != null) {
                    start_new_job.setText(getString(R.string.resume_job));
                    isJobRunning = true;
                } else {
                    start_new_job.setText(getString(R.string.start_new_job));
                    isJobRunning = false;
                }
            }
        });


        start_new_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isJobRunning) {
                    navController.navigate(JobListFragmentDirections.actionJobListFragmentToJobDetails(jobDetailsViewModel.getUID()));
                } else {
                    navController.navigate(JobListFragmentDirections.actionJobListFragmentToNewJobDetails());
                }
            }
        });

        btn_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    jobDataArrayList.add(new JobData("MalcomCompany" + counter, System.currentTimeMillis(), System.currentTimeMillis() + 10000));
                    jobListAdapter.notifyItemChanged(counter);
                    jobListRecyclerView.scrollToPosition(jobDataArrayList.size() - 1);    //This is such a stupid hack
                    counter++;
                    return;
                }
                FireStore fireStore = FireStore.getInstance(getContext());
//                ContractorInfo contractorInfo = new ContractorInfo("MalcomNew", "69696969", null);
//                fireStore.sendNewContractorToFireStore(authentication.getmAuth().getUid(),contractorInfo,false);
                LocationManager locationManager = LocationManager.getInstance(getContext());
                JobData jobData = new JobData("MalcomCompany", System.currentTimeMillis(), System.currentTimeMillis() + 10000);
                Location location = new Location("Test");
                location.setLatitude(1.3);
                location.setLongitude(1.2);
                LocationData locationData = new LocationData(System.currentTimeMillis(), 1.69, 1.69);
                ArrayList<LocationData> locationArrayList = new ArrayList<>();
                locationArrayList.add(locationData);
                locationArrayList.add(locationData);

                jobData.setLocation(locationArrayList);
                locationManager.startNewJob(jobData);
//                EventBus.getDefault().post(new LocationChangedEvent(location));
//                EventBus.getDefault().post(new LocationChangedEvent(location));
//                EventBus.getDefault().post(new LocationChangedEvent(location));


//                locationManager.startNewJob(new JobData("MalcomCompany3", System.currentTimeMillis(),System.currentTimeMillis()+1000));


//        FireStore.getInstance(this).sendNewJobToFireStore(mAuthentication.getUID(),jobData);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_list, container, false);
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;

        } else {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
    }
}