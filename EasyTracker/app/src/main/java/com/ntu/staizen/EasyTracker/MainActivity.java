package com.ntu.staizen.EasyTracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.MenuItem;

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
        FireStore fireStore = FireStore.getInstance(this);
        BoxHelper boxHelper = BoxHelper.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Help Me");
        actionBar.hide();
//        Authentication authentication = Authentication.getInstance(this);
//        FirebaseAuth auth = authentication.getmAuth();
//
//
//        ContractorInfo contractorInfo = new ContractorInfo();
//        contractorInfo.setName("Malcom");
//        contractorInfo.setPhoneNo("222222222");
//        FireStore.getInstance(this).sendNewContractorToFireStore(auth.getCurrentUser().getUid(), contractorInfo, false);

        NavHostFragment navHostFragment = (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
               onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}