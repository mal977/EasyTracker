package com.ntu.staizen.EasyTracker.firebase;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.LocationData;


public class FireStore {

    private static final String TAG = FireStore.class.getSimpleName();

    private Context mContext;
    private static FireStore instance;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;


    public static synchronized FireStore getInstance(Context context) {
        if (instance == null) {
            instance = new FireStore(context.getApplicationContext());
        }
        return instance;
    }

    public FireStore(Context context) {
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);
        mReference = mDatabase.getReference();
    }

    public void sendNewJobToFireStore(String UID, JobData jobData){
        mReference.child("jobs/"+UID).setValue(jobData);

    }

    public void sendLocationUpdateToFireStore(String UID, LocationData locationData){
        mReference.child("jobs/"+UID).child("locationDataList").push().setValue(locationData);
    }

}
