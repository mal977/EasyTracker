package com.ntu.staizen.EasyTracker.firebase;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.ntu.staizen.EasyTracker.MapsActivity;
import com.ntu.staizen.EasyTracker.SharedPreferenceHelper;
import com.ntu.staizen.EasyTracker.events.FirebaseAuthenticatedEvent;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;
import com.ntu.staizen.EasyTracker.model.ContractorInfo;


import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;


public class Authentication {

    private static final String TAG = Authentication.class.getSimpleName();

    private Context mContext;
    private static Authentication instance;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser = null;

    private boolean isAuthenticated = false;

    public static synchronized Authentication getInstance(Context context) {
        if (instance == null) {
            instance = new Authentication(context.getApplicationContext());
        }
        return instance;
    }

    public Authentication(Context context) {
        this.mContext = context;
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            isAuthenticated = true;
        }
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public String getUID() {
        return mAuth.getUid();
    }
    
    /**
     * Signs in Anonymously
     *
     * @param activity
     */
    public void signInAnonymously(Activity activity, String username, String phonenumber) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success, UID: " + mAuth.getCurrentUser().getUid());
                            isAuthenticated = true;
                            mUser = mAuth.getCurrentUser();
                            SharedPreferenceHelper.setPreferences(SharedPreferenceHelper.KEY_USERNAME, username, activity.getApplicationContext());
                            SharedPreferenceHelper.setPreferences(SharedPreferenceHelper.KEY_PHONE_NUMBER, phonenumber, activity.getApplicationContext());
                            FireStore.getInstance().sendNewContractorToFireStore(mUser.getUid(), new ContractorInfo(username, phonenumber, null), false);
                            EventBus.getDefault().postSticky(new FirebaseAuthenticatedEvent(1));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            EventBus.getDefault().postSticky(new FirebaseAuthenticatedEvent(0, task.getException().toString()));

                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void signInWithGoogle(String idToken, Activity activity) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void linkGoogleWithAnonymous(Activity activity, String id) {
        if (isAuthenticated) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null && user.isAnonymous()) {
                AuthCredential credential = GoogleAuthProvider.getCredential(id, null);

                mAuth.getCurrentUser().linkWithCredential(credential)
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "linkWithCredential:success");
                                    FirebaseUser user = task.getResult().getUser();
                                    Log.d(TAG, "uid: " + user.getUid());

                                } else {
                                    Log.w(TAG, "linkWithCredential:failure", task.getException());

                                }

                            }
                        });
            }
        }
    }

}
