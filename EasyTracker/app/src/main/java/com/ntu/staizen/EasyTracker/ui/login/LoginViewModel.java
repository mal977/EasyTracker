package com.ntu.staizen.EasyTracker.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;

import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.firebase.Authentication;

/**
 * Created by Malcom Teh
 * View model for LoginFragment
 * <p>
 * If you are unsure how this view model works
 *
 * @see <a href https://developer.android.com/topic/libraries/architecture/viewmodel></a>
 */
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<AuthenticatedState> authenticated = new MutableLiveData<>();

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<AuthenticatedState> getAuthenticatedState() {
        return authenticated;
    }

    /**
     * This method checks if loginData is valid first, before Authenticating user
     * @param username
     * @param phoneNumber
     * @param activity
     */
    public void login(String username, String phoneNumber, Activity activity) {
        if (getLoginFormState().getValue() == null) {
            setAuthenticated(0, activity.getString(R.string.please_enter_details));
            return;
        }
        if (getLoginFormState().getValue().isDataValid()) {
            Authentication mAuthenciation = Authentication.getInstance(activity);
            mAuthenciation.signInAnonymously(activity, username, phoneNumber);
        } else {
            setAuthenticated(0, "Please fix the following errors.");
        }
    }

    /**
     * Updates loginData when user enters details
     * @param username
     * @param phoneNumber
     */
    public void loginDataChanged(String username, String phoneNumber) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPhoneNumberValid(phoneNumber)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_phone_number));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    /**
     * Sets Authenticated status
     * @see com.ntu.staizen.EasyTracker.ui.login.AuthenticatedState
     *
     * @param authenticated
     * @param errorMessage
     */
    public void setAuthenticated(int authenticated, String errorMessage) {
        this.authenticated.setValue(new AuthenticatedState(authenticated, errorMessage));
    }

    /**
     * Username validation method
     * @param username
     * @return
     */
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

    /**
     * Phone number validation method
     * @param phoneNumber
     * @return
     */
    private boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        phoneNumber = phoneNumber.trim();

        if (phoneNumber.length() >= 8) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        } else {
            return false;
        }
    }
}