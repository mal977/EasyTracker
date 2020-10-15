package com.ntu.staizen.EasyTracker.ui.jobList;

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
import android.widget.Toast;

import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.firebase.Authentication;
import com.ntu.staizen.EasyTracker.model.JobData;

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
        if(authentication.getmAuth().getCurrentUser()==null){
            navController.navigate(R.id.loginFragment);
        }else{
            Toast.makeText(getContext(), authentication.getmAuth().getCurrentUser().getUid(), Toast.LENGTH_LONG).show();
        }
        jobDataArrayList = new ArrayList<>();
        jobDataArrayList.add(new JobData("MalCo1",System.currentTimeMillis(),System.currentTimeMillis()+1000));
        JobData j = new JobData("MalCo2",System.currentTimeMillis(),System.currentTimeMillis()+1000);
        j.setUID("123124sad12sad");
        jobDataArrayList.add(j);
        jobDataArrayList.add(j);
        jobDataArrayList.add(j);
        jobDataArrayList.add(j);
        jobDataArrayList.add(j);
        jobDataArrayList.add(new JobData("MalCo3",System.currentTimeMillis(),System.currentTimeMillis()+1000));

        jobListRecyclerView = view.findViewById(R.id.rv_job_list);
        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        jobListAdapter = new JobListAdapter(getContext(),jobDataArrayList);
        jobListRecyclerView.setAdapter(jobListAdapter);
        jobListRecyclerView.addItemDecoration(new DividerItemDecoration(jobListRecyclerView.getContext(),DividerItemDecoration.VERTICAL));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_list, container, false);
    }
}