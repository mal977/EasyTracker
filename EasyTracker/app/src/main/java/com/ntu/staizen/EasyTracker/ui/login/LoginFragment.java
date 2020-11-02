package com.ntu.staizen.EasyTracker.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.events.FirebaseAuthenticatedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginFragment extends Fragment {
    private static String TAG = LoginFragment.class.getSimpleName();

    private LoginViewModel loginViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final EditText usernameEditText = view.findViewById(R.id.et_name);
        final EditText phoneNumberEditText = view.findViewById(R.id.et_number);
        final Button loginButton = (Button) view.findViewById(R.id.btn_login);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.pb_login);

        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPhoneNumberError() != null) {
                    phoneNumberEditText.setError(getString(loginFormState.getPhoneNumberError()));
                }
            }
        });

        loginViewModel.getAuthenticatedState().observe(getViewLifecycleOwner(), new Observer<AuthenticatedState>() {
            @Override
            public void onChanged(AuthenticatedState authenticatedState) {

                switch (authenticatedState.getAuthenticatedStatus()) {
                    case 0:
                        loadingProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), authenticatedState.getErrorMessage(), Toast.LENGTH_LONG).show();
                        loginButton.setEnabled(true);
                        break;
                    case 1:
                        loadingProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Authenticated!", Toast.LENGTH_LONG).show();
                        NavController navController = Navigation.findNavController(getView());
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToJobListFragment());
                        break;

                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(), phoneNumberEditText.getText().toString()
                      );
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        phoneNumberEditText.addTextChangedListener(afterTextChangedListener);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                loginViewModel.login(usernameEditText.getText().toString(), phoneNumberEditText.getText().toString(),
                         getActivity());
            }
        });
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void handleFirebaseAuthenticatedEvent(FirebaseAuthenticatedEvent event) {
        Log.d(TAG, "FirebaseAuthenticatedEvent Success");
        loginViewModel.setAuthenticated(1, null);
    }
}