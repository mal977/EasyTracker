package com.ntu.staizen.EasyTracker.ui.jobList;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.database.BoxHelper;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.MyObjectBox;
import com.ntu.staizen.EasyTracker.ui.login.LoginFragment;
import com.ntu.staizen.EasyTracker.ui.newJobDetails.JobDetailState;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import io.objectbox.BoxStore;
import io.objectbox.DebugFlags;

import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static org.junit.Assert.*;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class JobListFragmentTest {

    private JobListFragment jobListFragment;
    private TestNavHostController navHostController;

    @Before
    public void setUp() throws Exception {
        BoxHelper boxHelper = BoxHelper.getInstance(InstrumentationRegistry.getInstrumentation().getTargetContext());
        navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.nav_graph);

        FragmentScenario<JobListFragment> fragmentFragmentScenario = FragmentScenario.launchInContainer(JobListFragment.class, null, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader,
                                        @NonNull String className) {
                jobListFragment = new JobListFragment(true);
                jobListFragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner viewLifecycleOwner) {

                        // The fragment’s view has just been created
                        if (viewLifecycleOwner != null) {
                            Navigation.setViewNavController(jobListFragment.requireView(), navHostController);
                        }

                    }

                });
                return jobListFragment;
            }
        });
        fragmentFragmentScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navHostController));

    }

    @Test
    public void testJobListRecyclerView() {

        onView(withId(R.id.rv_job_list)).check(matches(hasChildCount(0)));      //Recycler View has 0 elements
        onView(withId(R.id.rv_job_list)).check(matches(hasChildCount(0)));      //Recycler View has 0 elements


        ArrayList<JobData> jobDataArrayList = new ArrayList<>();
        jobDataArrayList.add(new JobData("Test1", System.currentTimeMillis(), System.currentTimeMillis()));
        jobDataArrayList.add(new JobData("Test2", System.currentTimeMillis(), System.currentTimeMillis()));
        jobListFragment.jobListViewModel.pastJobDataHistory.postValue(jobDataArrayList);

        onView(withId(R.id.rv_job_list)).check(matches(hasChildCount(2)));      //Recycler View has 2 items now

    }

//    @Test
//    public void testNewJobDetails() {
//        jobListFragment.jobDetailsViewModel.jobDetailStateMutableLiveData.postValue(new JobDetailState("uid", "companyName", System.currentTimeMillis(), System.currentTimeMillis()));
//        onView(withId(R.id.btn_start_new_job)).perform(click());
//        assertEquals(navHostController.getCurrentDestination().getId(), R.id.newJobDetails);
//
//    }
//
//    @Test
//    public void testResumeJob() {
//        jobListFragment.jobDetailsViewModel.jobDetailStateMutableLiveData.postValue(new JobDetailState("uid", "companyName", System.currentTimeMillis(), System.currentTimeMillis()));
//        jobListFragment.isJobRunning = true;
//        onView(withId(R.id.btn_start_new_job)).perform(click());
//        assertEquals(navHostController.getCurrentDestination().getId(), R.id.jobDetails);
//    }
}