package com.ntu.staizen.EasyTracker.ui.jobDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.ui.newJobDetails.JobDetailState;
import com.ntu.staizen.EasyTracker.ui.newJobDetails.JobDetailsViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class JobDetailsFragment extends Fragment {
    private static String TAG = JobDetailsViewModel.class.getSimpleName();

    private String jobUID;
    private JobData jobData;

    private JobDetailsViewModel jobDetailsModel;

    public JobDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ActionBar
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

        //Getting JobUID from arguements
        jobUID = JobDetailsFragmentArgs.fromBundle(getArguments()).getJobUid();

        jobDetailsModel = new ViewModelProvider(requireActivity()).get(JobDetailsViewModel.class);
        jobDetailsModel.getJobDetails(jobUID);

        final TextView tvCompanyName = view.findViewById(R.id.tv_company_name);
        final TextView tvTime = view.findViewById(R.id.tv_time);
        final TextView tvLocation = view.findViewById(R.id.tv_lat_lon);
        final TextView tvStatus = view.findViewById(R.id.tv_status);
        final TextView tvJobId = view.findViewById(R.id.tv_title_job_id);

        jobDetailsModel.getJobDetailStateMutableLiveData().observe(getViewLifecycleOwner(), new Observer<JobDetailState>() {
            @Override
            public void onChanged(JobDetailState jobDetailState) {
                tvCompanyName.setText(jobDetailState.getCompanyName());
                Date startDate = new Date(jobDetailState.getStart());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm EEEE dd/mm/yyyy");
                tvTime.setText(simpleDateFormat.format(startDate));
                tvStatus.setText("Tracking");
                tvJobId.setText(jobDetailState.getUID());
            }
        });
    }
}