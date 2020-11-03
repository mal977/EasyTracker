package com.ntu.staizen.EasyTracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.myapp.MyEventBusIndex;
import com.ntu.staizen.EasyTracker.database.BoxHelper;
import com.ntu.staizen.EasyTracker.firebase.Authentication;
import com.ntu.staizen.EasyTracker.firebase.FireStore;
import com.ntu.staizen.EasyTracker.manager.EasyTrackerManager;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus eventBus = EventBus.builder().addIndex(new MyEventBusIndex()).build();


        EasyTrackerManager locationManager = EasyTrackerManager.getInstance(this);
        Authentication authentication = Authentication.getInstance(this);
        FireStore fireStore = FireStore.getInstance();
        BoxHelper boxHelper = BoxHelper.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        NavHostFragment navHostFragment = (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        InputMethodManager inputMethodManager = (InputMethodManager )getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        switch (item.getItemId()){
        case android.R.id.home: {
               onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}