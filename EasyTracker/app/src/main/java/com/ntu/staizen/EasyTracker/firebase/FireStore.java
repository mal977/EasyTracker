package com.ntu.staizen.EasyTracker.firebase;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
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

    private static FireStore instance;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    public DatabaseReference getReference() {
        return mReference;
    }

    public static synchronized FireStore getInstance() {
        if (instance == null) {
            instance = new FireStore();
        }
        return instance;
    }

    public FireStore() {
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
        Log.d(TAG, "sendNewContractorToFireStore" + contractorInfo.toString() + " UID:" + UID);

        if (!checkedContractorInfo) {
            ValueEventListener contractorListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() == null) {
                        Log.d(TAG, "ContractorInfo does not exist, adding new contractorInfo: " + contractorInfo.toString() + " UID:" + UID);
                        mReference.removeEventListener(this);
                        sendNewContractorToFireStore(UID, contractorInfo, true);
                    } else {
                        Log.d(TAG, "Existing contractorInfo already exists");
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

    /**
     * Sends a new JobData to FireStore
     *
     * @param UID
     * @param jobData
     * @return
     */
    public DatabaseReference sendNewJobToFireStore(String UID, JobData jobData) {
        Log.d(TAG, "Adding a new Job : " + jobData.toString() + " to UID : " + UID);

        DatabaseReference databaseReference = mReference.child("contractors/" + UID).child("jobList").push();
        databaseReference.setValue(jobData).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "firebase error: " + e.toString());
                e.printStackTrace();
            }
        });
        return databaseReference;
    }

    /**
     * Updates a Job with matching UID its endTime value, effectively ending the Job
     *
     * @param UID
     * @param jobUID
     * @param endTime
     */
    public void sendEndJobToFireStore(String UID, String jobUID, long endTime) {
        Log.d(TAG, "sendEndJobToFireStore()" + " to UID : " + UID + " endTime : " + endTime);
        mReference.child("contractors/" + UID).child("jobList/" + jobUID).child("dateTimeEnd").setValue(endTime);

    }

    /**
     * Updates a Job with matching UID its endTime value, effectively ending the Job. Uses a database reference to idenfity the job.
     *
     * @param databaseReference
     * @param endTime
     */
    public void sendEndJobToFireStore(DatabaseReference databaseReference, long endTime) {
        Log.d(TAG, "sendEndJobToFireStore()" + " to UID : " + databaseReference.getKey() + " endTime : " + endTime);
        databaseReference.child("dateTimeEnd").setValue(endTime);

    }

    /**
     * Sends location updates to a stored database reference
     *
     * @param databaseReference
     * @param locationData
     */
    public void sendLocationUpdateToFireStore(DatabaseReference databaseReference, LocationData locationData) {
        Log.d(TAG, "Adding a new location: " + locationData.toString() + " to path : " + databaseReference.getKey());
        databaseReference.child("location").push().setValue(locationData);
    }
}
// Archives
//    public DatabaseReference sendLocationUpdateToFireStore(String UID, String jobUID, LocationData locationData) {
//        Log.d(TAG, "Adding a new location: " + locationData.toString() + " to UID : " + UID + " jobUID : " + jobUID);
//
//        DatabaseReference reference = mReference.child("contractors/" + UID).child("jobList/" + jobUID).child("location").push();
//        reference.setValue(locationData);
//        return reference;
//    }
//    private void checkIfContractorExist(String UID) {
//        ValueEventListener contractorListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ContractorInfo fetchedInfo = snapshot.getValue(ContractorInfo.class);
//                if (fetchedInfo == null) {
//                    mReference.removeEventListener(this);
//                } else {
//                    Log.d(TAG, "Existing contractorInfo already exists: " + fetchedInfo.toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "loadContractorInfo:onCancelled", error.toException());
//            }
//        };
//    }