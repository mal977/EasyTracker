package com.ntu.staizen.EasyTracker.ui.login;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import com.ntu.staizen.EasyTracker.MainActivity;
import com.ntu.staizen.EasyTracker.R;
import com.ntu.staizen.EasyTracker.ui.jobDetails.JobDetailsFragment;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class LoginFragmentTest {

    private TestNavHostController navHostController;
    private FragmentScenario<LoginFragment> fragmentFragmentScenario;
    private Activity activity;

    @Before
    public void setUp() throws Exception {
        navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.nav_graph);

        fragmentFragmentScenario = FragmentScenario.launchInContainer(LoginFragment.class);
        fragmentFragmentScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(),navHostController));
        fragmentFragmentScenario.onFragment(fragment -> activity = fragment.getActivity());

    }

    @Test
    public void testPhoneNumberValid(){
        onView(withId(R.id.et_number)).perform(typeText("88characters"), closeSoftKeyboard());
        onView(withId(R.id.et_number)).check(matches(withText("88")));  //Ensure non numeric characters are not accepted
        onView(withId(R.id.et_name)).check(matches(hasErrorText("Not a valid username")));
    }

    /**
     * This test checks if the login fragment correctly blocks the user from going to job details screen until he
     * correctly fills the login details and authenticates
     */
    @Test
    public void testEnterDetailsToast(){
        onView(withId(R.id.btn_login)).perform(click());
        onView(withText(R.string.please_enter_details)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    /**
     * Test user login detail verification
     * Also tests that nav graph properly sets navigation to job details screen
     */
    @Test
    public void loginUserDetailVerificationTest() {

        onView(withId(R.id.btn_login)).perform(click());
        onView(withId(R.id.pb_login)).check(matches(not(isDisplayed())));

        onView(withId(R.id.et_number)).perform(typeText("88characters"), closeSoftKeyboard());
        onView(withId(R.id.et_number)).check(matches(withText("88")));  //Ensure non numeric characters are not accepted
        onView(withId(R.id.et_name)).check(matches(hasErrorText("Not a valid username")));

        onView(withId(R.id.et_name)).perform(typeText("EspressoTest"));
        onView(withId(R.id.et_number)).check(matches(hasErrorText("Invalid Phone Number!")));

        onView(withId(R.id.et_number)).perform(typeText("88888888"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
//        onView(withId(R.id.pb_login)).check(matches(not(isDisplayed())));

        onView(withId(R.id.btn_login)).perform(click());

        //Verify that login screen transitions to job list screen
        assertEquals(navHostController.getCurrentDestination().getId(),R.id.jobListFragment);

    }
}