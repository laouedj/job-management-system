package org.prototype.study;


import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;
import org.prototype.study.job.state.JobState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class JobApplicationUnitTest {

    @Test
    public void shouldCompleteJobSuccessfully() throws InterruptedException {

        JobManager jobManager = new DefaultJobManager();
        jobManager.start();

        JobInputDataList jobInputDataList = new JobInputDataList();
        InputData param = new InputData(DataType.STRING, "HELLO");
        jobInputDataList.addInputData("Greating", param);
        Job job = new DefaultJob(jobInputDataList);

        jobManager.launchOne(job);


        JobContext jobContext = job.getJobExecutionContext();
        jobContext.getDone().await();
        assertEquals(jobContext.getStatus(), JobState.SUCCESS);
        assertNotNull(jobContext.getStartTime());
        assertNotNull(jobContext.getEndTime());

        jobManager.shutdown();
    }

    @Test
    public void shouldCompleteJobSuccessfullyWhenLaunchedManyTime() throws InterruptedException {


        List<Job> jobsToLaunch = new ArrayList<Job>();
        JobManager jobManager = new DefaultJobManager();
        jobManager.start();

        for (int i = 1; i < 50; i++) {

            JobInputDataList jobInputDataList = new JobInputDataList();
            InputData param = new InputData(DataType.STRING, "HELLO " + i);
            jobInputDataList.addInputData("Greating", param);
            Job job = new DefaultJob(jobInputDataList);
            jobsToLaunch.add(job);
        }


        for (Job job : jobsToLaunch) {

            jobManager.launchOne(job);

        }


        for (Job job : jobsToLaunch) {

            JobContext jobContext = job.getJobExecutionContext();
            jobContext.getDone().await();
            assertEquals(jobContext.getStatus(), JobState.SUCCESS);
            assertNotNull(jobContext.getStartTime());
            assertNotNull(jobContext.getEndTime());

        }

        jobManager.shutdown();


    }

    @Test
    public void shouldCompleteManyJobSuccessfully() throws InterruptedException {

        JobManager jobManager = new DefaultJobManager();
        jobManager.start();

        List<Job> jobsToLaunch = new ArrayList<Job>();


        for (int i = 1; i < 50; i++) {

            JobInputDataList jobInputDataList = new JobInputDataList();
            InputData param = new InputData(DataType.STRING, "HELLO " + i);
            jobInputDataList.addInputData("Greating", param);
            Job job = new DefaultJob(jobInputDataList);
            jobsToLaunch.add(job);

        }



        jobManager.launchMany(jobsToLaunch);


        for (Job job : jobsToLaunch) {
            JobContext jobContext = job.getJobExecutionContext();
            jobContext.getDone().await();
            assertEquals(jobContext.getStatus(), JobState.SUCCESS);
            assertNotNull(jobContext.getStartTime());
            assertNotNull(jobContext.getEndTime());
        }

        jobManager.shutdown();

    }

    @Test
    public void shouldFailedWhenException() throws InterruptedException {

        JobManager jobManager = new DefaultJobManager();
        jobManager.start();

        JobInputDataList jobInputDataList = new JobInputDataList();
        InputData param = new InputData(DataType.STRING, "HELLO");
        jobInputDataList.addInputData("Greating", param);
        Job job = new MockFailedJob(jobInputDataList);

        jobManager.launchOne(job);


        JobContext jobContext = job.getJobExecutionContext();
        jobContext.getDone().await();
        assertEquals(jobContext.getStatus(), JobState.FAILED);
        assertNotNull(jobContext.getStartTime());
        assertNotNull(jobContext.getEndTime());
        assertNotNull(jobContext.getError());

        jobManager.shutdown();

    }

    @Test
    public void shouldFailedWithoutParameters() throws InterruptedException {

        JobManager jobManager = new DefaultJobManager();
        jobManager.start();

        JobInputDataList jobInputDataList = new JobInputDataList();
        Job job = new DefaultJob(jobInputDataList);

        jobManager.launchOne(job);



        JobContext jobContext = job.getJobExecutionContext();
        jobContext.getDone().await();
        assertEquals(jobContext.getStatus(), JobState.FAILED);
        assertNotNull(jobContext.getStartTime());
        assertNotNull(jobContext.getEndTime());
        assertNotNull(jobContext.getError());

        jobManager.shutdown();


    }


    @Test
    public void shouldCompleteManyJobWithoutSideEffect() throws InterruptedException {

        JobManager jobManager = new DefaultJobManager();
        jobManager.start();

        JobInputDataList jobInputDataListOne = new JobInputDataList();
        InputData paramOne = new InputData(DataType.STRING, "HELLO");
        jobInputDataListOne.addInputData("Greating", paramOne);
        Job jobOne = new DefaultJob(jobInputDataListOne);

        JobInputDataList jobInputDataListTwo = new JobInputDataList();
        InputData paramTwo = new InputData(DataType.STRING, "FAILED JOB");
        jobInputDataListTwo.addInputData("Greating", paramTwo);
        Job jobTwo = new MockFailedJob(jobInputDataListTwo);


        JobInputDataList jobInputDataListThree = new JobInputDataList();
        InputData paramThree = new InputData(DataType.STRING, "Yes !!!");
        jobInputDataListThree.addInputData("Greating", paramThree);
        Job jobThree = new DefaultJob(jobInputDataListThree);

        List<Job> jobsToLaunch = new ArrayList<Job>();
        jobsToLaunch.add(jobOne);
        jobsToLaunch.add(jobTwo);
        jobsToLaunch.add(jobThree);


        jobManager.launchMany(jobsToLaunch);


        JobContext jobContextOne = jobOne.getJobExecutionContext();
        jobContextOne.getDone().await();
        assertEquals(jobContextOne.getStatus(), JobState.SUCCESS);
        assertNotNull(jobContextOne.getStartTime());
        assertNotNull(jobContextOne.getEndTime());

        JobContext jobContextTwo = jobTwo.getJobExecutionContext();
        jobContextTwo.getDone().await();
        assertEquals(jobContextTwo.getStatus(), JobState.FAILED);
        assertNotNull(jobContextTwo.getStartTime());
        assertNotNull(jobContextTwo.getEndTime());
        assertNotNull(jobContextTwo.getError());


        JobContext jobContextThree = jobThree.getJobExecutionContext();
        jobContextThree.getDone().await();
        assertEquals(jobContextThree.getStatus(), JobState.SUCCESS);
        assertNotNull(jobContextThree.getStartTime());
        assertNotNull(jobContextThree.getEndTime());

        jobManager.shutdown();
    }

}
