package com.ntu.staizen.EasyTracker.ui.jobList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.Utilities;
import com.ntu.staizen.EasyTracker.model.JobData;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Malcom Teh on 01/11/2020
 * Standard Adapter for recycler view
 */
public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder> {
    public static final String TAG = RecyclerView.class.getSimpleName();

    private ArrayList<JobData> mJobList;
    protected NavController navController;

    private Context context;

    public JobListAdapter(Context context, ArrayList<JobData> jobDataArrayList){
        this.context = context;
        this.mJobList = jobDataArrayList;
    }

    public void setNewJobListData(ArrayList<JobData> jobListData){
        this.mJobList = jobListData;
        notifyDataSetChanged();
    }

    public void addNewJobData(JobData jobData){
        this.mJobList.add(jobData);
        notifyItemInserted(mJobList.size());
    }

    @NonNull
    @Override
    public JobListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.model_job,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.updateData(mJobList.get(position),position);

    }

    @Override
    public int getItemCount() {
        return mJobList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ConstraintLayout constraintLayout;
        TextView jobID;
        TextView companyName;
        TextView timeStart;
        TextView timeEnd;


        ViewHolder(View itemView){
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.cl_job);
            jobID = itemView.findViewById(R.id.tv_job_id);
            companyName = itemView.findViewById(R.id.tv_company_name);
            timeStart = itemView.findViewById(R.id.tv_time_start);
            timeEnd = itemView.findViewById(R.id.tv_time_end);
            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        public void updateData(JobData jobData, int position){
            jobID.setText(String.valueOf(jobData.getUID()));
            companyName.setText(jobData.getCompany());
            Date date = new Date();
            date.setTime(jobData.getDateTimeStart());
            timeStart.setText(Utilities.jobDateFormatter(date));
            if(jobData.getDateTimeEnd()==0){
                timeEnd.setText("In Progress");
            }else {
                date.setTime(jobData.getDateTimeEnd());
                timeEnd.setText(Utilities.jobDateFormatter(date));
            }
        }
    }
}
