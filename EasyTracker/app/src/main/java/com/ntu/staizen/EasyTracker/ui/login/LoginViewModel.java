package com.ntu.staizen.EasyTracker.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;

import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.firebase.Authentication;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    MutableLiveData<AuthenticatedState> authenticated = new MutableLiveData<>();

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<AuthenticatedState> getAuthenticatedState(){
        return authenticated;
    }


    public void login(String username, String password, Activity activity) {
        Authentication mAuthenciation = Authentication.getInstance(activity);
        mAuthenciation.signInAnonymously(activity);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void setAuthenticated(int authenticated){
        this.authenticated.setValue(new AuthenticatedState(authenticated));
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}