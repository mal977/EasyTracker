package com.ntu.staizen.EasyTracker.firebase;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ntu.staizen.EasyTracker.events.FirebaseAuthenticatedEvent;
import com.ntu.staizen.EasyTracker.events.LocationChangedEvent;


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

    }

    public void signInAnonymously(Activity activity) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success, UID: " + mAuth.getCurrentUser().getUid());
                            isAuthenticated = true;
                            mUser = mAuth.getCurrentUser();
                            EventBus.getDefault().post(new FirebaseAuthenticatedEvent());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public String getUID(){
        if(!isAuthenticated){
            return null;
        }
        return mUser.getUid();
    }

}
