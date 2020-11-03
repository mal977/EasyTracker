package com.ntu.staizen.EasyTracker.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.ntu.staizen.EasyTracker.SharedPreferenceHelper;
import com.ntu.staizen.EasyTracker.firebase.Authentication;
import com.ntu.staizen.EasyTracker.firebase.FireStore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class EasyTrackerManagerTest {

    private EasyTrackerManager easyTrackerManager;
    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    final FireStore fireStore = Mockito.mock(FireStore.class);
    final Authentication authentication = Mockito.mock(Authentication.class);
//    final SharedPreferenceHelper sharedPreferenceHelper = Mockito.mock(SharedPreferenceHelper.class);

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getInstance() {
//        sharedPreferenceHelper.setPreferences("running_job","-RpkicsXc1uM4dj1PzYXoZMijYyH3",context);
//        Mockito.when(sharedPreferenceHelper.doesValueExist("running_job", context)).thenReturn(true);
//        Mockito.when(SharedPreferenceHelper.getPreference("running_job", context)).thenReturn("-ML5HeJYUXlxrOCjJSBc");
//        Mockito.when(authentication.getUID()).thenReturn("-RpkicsXc1uM4dj1PzYXoZMijYyH3 ");
//        Mockito.when(fireStore.getReference().child("contractors/" + authentication.getUID()).child("jobList/ML5HeJYUXlxrOCjJSBc")).thenReturn();
        easyTrackerManager = EasyTrackerManager.getInstance(context);
        assertEquals(easyTrackerManager.currentRunningJobReference, null);
        assertEquals(easyTrackerManager.currentRunningJobReference.getKey(), "-RpkicsXc1uM4dj1PzYXoZMijYyH3");

    }

    @Test
    public void testGetInstance() {
    }

    @Test
    public void startLocationUpdates() {
    }

    @Test
    public void testStartLocationUpdates() {
    }

    @Test
    public void stopLocationUpdates() {
    }

    @Test
    public void startNewJob() {
    }

    @Test
    public void endCurrentRunningJob() {
    }

    @Test
    public void isCurrentJobTracking() {
    }

    @Test
    public void checkAndResumeTrackingJob() {
    }
}