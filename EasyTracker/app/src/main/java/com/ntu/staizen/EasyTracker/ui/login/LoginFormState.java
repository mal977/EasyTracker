package com.ntu.staizen.EasyTracker.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer phoneNumberError;
    private boolean isDataValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer phoneNumberError) {
        this.usernameError = usernameError;
        this.phoneNumberError = phoneNumberError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.phoneNumberError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

    @Nullable
    Integer getPhoneNumberError() {
        return phoneNumberError;
    }
}