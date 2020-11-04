package com.ntu.staizen.EasyTracker;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Date;

import static org.junit.Assert.*;

/**
 * Created by Malcom
 * Test suite for Utilties date formatter method
 * @see Utilities#jobDateFormatter(java.util.Date)
 */
@RunWith(JUnit4.class)
public class UtilitiesDateFormatterTest {

    Date testDate = new Date(1604522061937L); //04:34 Thursday 05/11/2020

    /**
     * assert 04:34 Thursday 05/11/2020 is returned for test date
     */
    @Test
    public void jobDateFormatter() {
        assertEquals("04:34 Thursday 05/11/2020",Utilities.jobDateFormatter(testDate));
    }

    /**
     * null date test
     * empty string "" is expected
     */
    @Test(expected = IllegalStateException.class)
    public void nullDateFormatterTest(){
        assertEquals("",Utilities.jobDateFormatter(null));
    }
}