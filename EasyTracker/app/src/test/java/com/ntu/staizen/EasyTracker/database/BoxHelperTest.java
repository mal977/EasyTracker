package com.ntu.staizen.EasyTracker.database;

import com.ntu.staizen.EasyTracker.database.BoxHelper;
import com.ntu.staizen.EasyTracker.model.JobData;
import com.ntu.staizen.EasyTracker.model.LocationData;
import com.ntu.staizen.EasyTracker.model.MyObjectBox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.ArrayList;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.DebugFlags;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Malcom Teh
 * This test suite ensures correct functionality of BoxHelper class
 * @see com.ntu.staizen.EasyTracker.database.BoxHelper
 */
@RunWith(JUnit4.class)
public class BoxHelperTest {
    private BoxHelper boxHelper;

    //Testing Data
    private static final File TEST_DIRECTORY = new File("objectbox-example/test-db");
    private LocationData locationData1 = new LocationData("1", System.currentTimeMillis(), 1.3, 1.2);
    private LocationData locationData2 = new LocationData("1", System.currentTimeMillis() + 1000, 1.4, 1.5);
    private ArrayList<LocationData> locationDataArrayList = new ArrayList<LocationData>() {
        {
            add(locationData1);
            add(locationData2);
        }
    };
    private JobData jobData1 = new JobData("TestJob1", System.currentTimeMillis(), System.currentTimeMillis() + 1000){{
        setUID("Test_UID1");
    }};
    private JobData jobData2 = new JobData("TestJob1", System.currentTimeMillis(), System.currentTimeMillis() + 1000){{
        setUID("Test_UID2");
    }};
    private ArrayList<JobData> jobDataArrayList = new ArrayList<JobData>() {
        {
            add(jobData1);
            add(jobData2);
        }
    };
    //End of Testing Data

    @Before
    public void setUp() throws Exception {
        boxHelper = new BoxHelper();
        BoxHelper.instance = boxHelper;
        BoxStore.deleteAllFiles(TEST_DIRECTORY);
        BoxHelper.mBoxStore = MyObjectBox.builder()
                .directory(TEST_DIRECTORY)
                .debugFlags(DebugFlags.LOG_QUERIES | DebugFlags.LOG_QUERY_PARAMETERS)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        if (BoxHelper.mBoxStore != null) {
            BoxHelper.mBoxStore.close();
            BoxHelper.mBoxStore = null;
        }
        BoxStore.deleteAllFiles(TEST_DIRECTORY);
    }

    @Test
    public void getInstance() {
        BoxHelper output = BoxHelper.getInstance();
        assertEquals(boxHelper, output);
    }


    @Test
    public void addLocationDataList() {
        //Temporarily not Used
    }

    /**
     * @see BoxHelper#addLocationData(LocationData)
     * This tests the addLocationData method
     * It adds a location data, and verifies if the stored LocationData is correct
     */
    @Test
    public void addLocationData() {
        boxHelper.addLocationData(locationData1);
        Box<LocationData> locationDataBox = boxHelper.mBoxStore.boxFor(LocationData.class);
        LocationData output = locationDataBox.query().build().findFirst();
        assertEquals(locationData1.getDateTime(), output.getDateTime());
        assertEquals(locationData1.getJobID(), output.getJobID());
        assertEquals(locationData1.getLat(), output.getLat(), 0);
        assertEquals(locationData1.getLon(), output.getLon(), 0);

    }

    /**
     * @see BoxHelper#getLocationDataMatchingJob(String)
     * This tests the getLocationDataMatchingJob method
     * It adds a arraylist of location data, and verifies if the stored LocationData is correct
     */
    @Test
    public void getLocationDataMatchingJob() {
        boxHelper.addLocationData(locationData1);
        boxHelper.addLocationData(locationData2);
        ArrayList<LocationData> output = boxHelper.getLocationDataMatchingJob("1");
        for (int i = 0; i < output.size(); i++
        ) {
            assertEquals(locationDataArrayList.get(i).getDateTime(), output.get(i).getDateTime());
            assertEquals(locationDataArrayList.get(i).getJobID(), output.get(i).getJobID());
            assertEquals(locationDataArrayList.get(i).getLat(), output.get(i).getLat(), 0);
            assertEquals(locationDataArrayList.get(i).getLon(), output.get(i).getLon(), 0);
        }
    }

    @Test
    public void getLocationData() {
        //Temporarily not Used
    }

    /**
     * @see BoxHelper#getLatestLocationDataMatchingJob(String) (String)
     * This tests the getLatestLocationDataMatchingJob method
     * ensures only the most recent added location data matching a jobUID is returned
     */
    @Test
    public void getLatestLocationDataMatchingJob() {
        // This test to make sure only the LATEST location data per matching job is returned
        boxHelper.addLocationData(locationData1);
        boxHelper.addLocationData(locationData2);
        LocationData output = boxHelper.getLatestLocationDataMatchingJob("1");
        assertEquals(locationData2.getDateTime(), output.getDateTime());
        assertEquals(locationData2.getJobID(), output.getJobID());
        assertEquals(locationData2.getLat(), output.getLat(), 0);
        assertEquals(locationData2.getLon(), output.getLon(), 0);

        output = boxHelper.getLatestLocationDataMatchingJob("expectedNull");
        assertNull(output);

    }

    /**
     * @see BoxHelper#getLatestLocationDataMatchingJob(String) (String)
     * This tests the getLatestLocationDataMatchingJob method
     * ensures only the most recent added location data matching a jobUID is returned
     * this test checks for null value of non existing jobId
     */
    @Test
    public void getLatestLocationDataMatchingJobExpectedNull() {

        LocationData output = boxHelper.getLatestLocationDataMatchingJob("expectedNull");
        assertNull(output);

    }

    /**
     * @see BoxHelper#addJobData(JobData)
     * This tests the addJobData method
     * add a job data and checks if it is correclty stored
     */
    @Test
    public void addJobData() {

        boxHelper.addJobData(jobData1);
        Box<JobData> jobDataBox = boxHelper.mBoxStore.boxFor(JobData.class);
        JobData output = jobDataBox.query().build().findFirst();
        assertEquals(jobData1.getCompany(), output.getCompany());
        assertEquals(jobData1.getDateTimeEnd(), output.getDateTimeEnd(), 0);
        assertEquals(jobData1.getDateTimeStart(), output.getDateTimeStart(), 0);
        assertEquals(jobData1.getUID(), output.getUID());
    }

    /**
     * @see BoxHelper#getJobData(String)
     * This tests the getJobData method
     * gets a job data matching uid
     * also ensures expected behavious, null is returned when no job is found
     */
    @Test
    public void getJobData() {
        boxHelper.addJobData(jobData1);
        JobData output = boxHelper.getJobData("Test_UID1");
        assertEquals(jobData1.getCompany(), output.getCompany());
        assertEquals(jobData1.getDateTimeEnd(), output.getDateTimeEnd(), 0);
        assertEquals(jobData1.getDateTimeStart(), output.getDateTimeStart(), 0);
        assertEquals(jobData1.getUID(), output.getUID());

        output = boxHelper.getJobData("EXPECTED_NULL");
        assertNull(output);
    }

    /**
     * @see BoxHelper#getAllJobData()
     * This tests the getAllJobData method
     * gets all job data
     */
    @Test
    public void getAllJobData() {
        ArrayList<JobData> output = boxHelper.getAllJobData();

        boxHelper.addJobData(jobData1);
        boxHelper.addJobData(jobData2);
        output = boxHelper.getAllJobData();
        for (int i = 0; i < output.size(); i++
        ) {
            assertEquals(jobDataArrayList.get(i).getCompany(), output.get(i).getCompany());
            assertEquals(jobDataArrayList.get(i).getDateTimeEnd(), output.get(i).getDateTimeEnd(), 0);
            assertEquals(jobDataArrayList.get(i).getDateTimeStart(), output.get(i).getDateTimeStart(), 0);
            assertEquals(jobDataArrayList.get(i).getUID(), output.get(i).getUID());
        }
    }

    /**
     * @see BoxHelper#updateJobData(JobData)
     * This tests the updateJobData method
     * updates a job and ensures the data is correctly updated
     */
    @Test
    public void updateJobData() {
        JobData testData = jobData1;
        boxHelper.addJobData(testData);
        testData.setCompany("updateJobData");
        boxHelper.updateJobData(testData);
        JobData output = boxHelper.getJobData(testData.getUID());
        assertEquals("updateJobData", output.getCompany());
        assertEquals(testData.getUID(), output.getUID());
        assertEquals(testData.getDateTimeEnd(), output.getDateTimeEnd(), 0);
        assertEquals(testData.getDateTimeStart(), output.getDateTimeStart(), 0);    }
}