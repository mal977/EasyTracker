package com.ntu.staizen.EasyTracker;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.preference.PreferenceManager;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.*;

/**
 * Created by Malcom on 3 Nov 2020
 * This test suite ensures correct functionality of SharedPreferenceHelper class
 * @see com.ntu.staizen.EasyTracker.SharedPreferenceHelper
 */
@RunWith(AndroidJUnit4.class)
public class SharedPreferenceHelperTest {
    Context appContext;

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    }

    @After
    public void tearDown() throws Exception {
        PreferenceManager.getDefaultSharedPreferences(appContext).edit().clear().apply();
    }

    /**
     * @see com.ntu.staizen.EasyTracker.SharedPreferenceHelper#setPreferences(String, String, Context)
     * This tests the setPreference method.
     * Sets a value in shared preference, fetches the value and checks if its the same
     */
    @Test
    public void setPreferenceTest() {
        SharedPreferenceHelper.setPreferences("test", "testValue", appContext);
        assertEquals("testValue", PreferenceManager.getDefaultSharedPreferences(appContext).getString("test", null));
        assertNotEquals("testValue", PreferenceManager.getDefaultSharedPreferences(appContext).getString("test2", null));
        assertNull(PreferenceManager.getDefaultSharedPreferences(appContext).getString("testNull", null));
    }

    /**
     * @see com.ntu.staizen.EasyTracker.SharedPreferenceHelper#getPreference(String, Context)
     * This tests the getPreference method
     * Adds a value, fetches it using the method and checks if the value is correct
     */
    @Test
    public void getPreferenceTest() {
        SharedPreferenceHelper.setPreferences("getTest", "getTestValue", appContext);
        assertEquals("getTestValue", SharedPreferenceHelper.getPreference("getTest", appContext));
        assertNotEquals("testValue", SharedPreferenceHelper.getPreference("getTest", appContext));
        assertNull(SharedPreferenceHelper.getPreference("getTestNull", appContext));
    }

    /**
     * @see com.ntu.staizen.EasyTracker.SharedPreferenceHelper#doesValueExist(String, Context) 
     * This tests the doesValueExist method
     * Checks the doesValueExist method
     * checks if a known missing value is not there
     * then adds a value and checks if it exists
     */
    @Test
    public void doesValueExistTest() {
        assertFalse(SharedPreferenceHelper.doesValueExist("test", appContext));
        SharedPreferenceHelper.setPreferences("test", "getTestValue", appContext);
        assertTrue(SharedPreferenceHelper.doesValueExist("test", appContext));
    }

    /**
     * @see com.ntu.staizen.EasyTracker.SharedPreferenceHelper#removeValue(String, Context) 
     * This test checks if the removeValueTest method
     * adds a value, and then calls the removeValueTest to clear the value
     * checks if the value still exists in shared preference
     */
    @Test
    public void removeValueTest() {
        assertFalse(SharedPreferenceHelper.doesValueExist("test", appContext));
        SharedPreferenceHelper.setPreferences("test","testValue",appContext);
        assertTrue(SharedPreferenceHelper.doesValueExist("test", appContext));
        assertEquals("testValue",SharedPreferenceHelper.getPreference("test", appContext));
        SharedPreferenceHelper.removeValue("test",appContext);
        assertFalse(SharedPreferenceHelper.doesValueExist("test", appContext));

    }
}