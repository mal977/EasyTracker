package com.ntu.staizen.EasyTracker.ui.login;

import javax.annotation.Nullable;

public class AuthenticatedState {

    private Integer authenticatedStatus;    //0=FAILED, 1 = Sucesss

    @Nullable
    private String errorMessage;

    public AuthenticatedState(Integer integer){
        this.authenticatedStatus = integer;
    }

    public AuthenticatedState(Integer integer, String errorMessage){
        this.authenticatedStatus = integer;
        this.errorMessage = errorMessage;

    }

    public Integer getAuthenticatedStatus() {
        if(authenticatedStatus == null){
            return -1;
        }
        return authenticatedStatus;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }
}
