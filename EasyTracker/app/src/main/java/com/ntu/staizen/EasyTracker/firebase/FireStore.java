package com.ntu.staizen.EasyTracker.firebase;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ntu.staizen.EasyTracker.model.ContractorInfo;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.LocationData;

import androidx.annotation.NonNull;


public class FireStore {

    private static final String TAG = FireStore.class.getSimpleName();

    private Context mContext;
    private static FireStore instance;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private boolean doesContractorExist = false;


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

    /**
     * Recursive method which first checks if existing contractor info exists. If the info dosent exists, it calls this method again, but sets checkedContractorInfo to true;
     *
     * @param UID
     * @param contractorInfo
     * @param checkedContractorInfo
     */
    public void sendNewContractorToFireStore(String UID, ContractorInfo contractorInfo, boolean checkedContractorInfo) {

        if (!checkedContractorInfo) {
            ValueEventListener contractorListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() == null) {
                        Log.d(TAG, "ContractorInfo does not exist, adding new contractorInfo: " + contractorInfo.toString() + " UID:" + UID);
                        doesContractorExist = false;
                        mReference.removeEventListener(this);
                        sendNewContractorToFireStore(UID, contractorInfo, true);
                    } else {
                        Log.d(TAG, "Existing contractorInfo already exists");
                        doesContractorExist = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "loadContractorInfo:onCancelled", error.toException());
                }
            };
            mReference.child("contractors/" + UID).addListenerForSingleValueEvent(contractorListener);
        } else {
            mReference.child("contractors/" + UID).setValue(contractorInfo);

        }


    }

    public DatabaseReference sendNewJobToFireStore(String UID, JobData jobData) {
        Log.d(TAG, "Adding a new Job : " + jobData.toString() + " to UID : " + UID);

        DatabaseReference databaseReference = mReference.child("contractors/" + UID).child("jobList").push();
        databaseReference.setValue(jobData);
        return databaseReference;
    }

    public DatabaseReference sendLocationUpdateToFireStore(String UID, String jobUID ,LocationData locationData) {
        Log.d(TAG, "Adding a new location: " + locationData.toString() + " to UID : " + UID + " jobUID : " + jobUID);

        DatabaseReference reference = mReference.child("contractors/" + UID).child("jobList/"+jobUID).child("location").push();
        reference.setValue(locationData);
        return reference;
    }

    public void sendLocationUpdateToFireStore(DatabaseReference databaseReference, LocationData locationData) {
        Log.d(TAG, "Adding a new location: " + locationData.toString() + " to path : " + databaseReference.getKey());
        databaseReference.child("location").push().setValue(locationData);
    }

    private void checkIfContractorExist(String UID) {
        ValueEventListener contractorListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ContractorInfo fetchedInfo = snapshot.getValue(ContractorInfo.class);
                if (fetchedInfo == null) {
                    mReference.removeEventListener(this);
                    doesContractorExist = false;
                } else {
                    Log.d(TAG, "Existing contractorInfo already exists: " + fetchedInfo.toString());
                    doesContractorExist = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "loadContractorInfo:onCancelled", error.toException());
            }
        };
    }

}
