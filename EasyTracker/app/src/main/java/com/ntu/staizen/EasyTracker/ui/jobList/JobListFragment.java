package com.ntu.staizen.EasyTracker.ui.jobList;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.firebase.Authentication;
import com.ntu.staizen.EasyTracker.firebase.FireStore;
import com.ntu.staizen.EasyTracker.manager.LocationManager;
import com.ntu.staizen.EasyTracker.model.ContractorInfo;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.LocationData;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class JobListFragment extends Fragment {


    private JobListAdapter jobListAdapter;
    private RecyclerView jobListRecyclerView;
    private ArrayList<JobData> jobDataArrayList;


    public JobListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        Authentication authentication = Authentication.getInstance(getContext());
        if (authentication.getmAuth().getCurrentUser() == null) {
            navController.navigate(R.id.loginFragment);
        } else {
            TextView tvUID = view.findViewById(R.id.tv_uid);
            tvUID.setText("UID: " + authentication.getmAuth().getCurrentUser().getUid());
        }
        jobDataArrayList = new ArrayList<>();
        jobDataArrayList.add(new JobData("MalCo1", System.currentTimeMillis(), System.currentTimeMillis() + 1000));
        JobData j = new JobData("MalCo2", System.currentTimeMillis(), System.currentTimeMillis() + 1000);
        j.setUID("123124sad12sad");
        jobDataArrayList.add(j);
        jobDataArrayList.add(j);
        jobDataArrayList.add(j);
        jobDataArrayList.add(j);
        jobDataArrayList.add(j);
        jobDataArrayList.add(new JobData("MalCo3", System.currentTimeMillis(), System.currentTimeMillis() + 1000));

        jobListRecyclerView = view.findViewById(R.id.rv_job_list);
        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        jobListAdapter = new JobListAdapter(getContext(), jobDataArrayList);
        jobListRecyclerView.setAdapter(jobListAdapter);
        jobListRecyclerView.addItemDecoration(new DividerItemDecoration(jobListRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        Button start_new_job = (Button) view.findViewById(R.id.btn_start_new_job);
        Button btn_temp = (Button) view.findViewById(R.id.btn_temp);

        start_new_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "LOLOLOLOLO", Toast.LENGTH_LONG).show();

            }
        });

        btn_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireStore fireStore = FireStore.getInstance(getContext());
//                ContractorInfo contractorInfo = new ContractorInfo("MalcomNew", "69696969", null);
//                fireStore.sendNewContractorToFireStore(authentication.getmAuth().getUid(),contractorInfo,false);
                LocationManager locationManager = LocationManager.getInstance(getContext());
                JobData jobData = new JobData("MalcomCompany", System.currentTimeMillis(), System.currentTimeMillis() + 10000);
                Location location = new Location("Test");
                location.setLatitude(1.3);
                location.setLongitude(1.2);
                LocationData locationData = new LocationData(System.currentTimeMillis(), 1.69,1.69);
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
}