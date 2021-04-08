package org.prototype.study;

import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;
import org.prototype.study.job.state.JobState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SuccessfulImmediateJobUnitTest {

    @Test
    public void oneJobShouldCompleteSuccessfully() throws InterruptedException {

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
    public void jobsShouldCompleteSuccessfullyWhenLaunchedOneByOne() throws InterruptedException {


        List<Job> jobsToLaunch = new ArrayList<Job>();
        JobManager jobManager = new DefaultJobManager();
        jobManager.start();

        for (int i = 1; i < 10; i++) {

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
    public void jobsShouldCompleteSuccessfullyWhenLaunchedByBulk() throws InterruptedException {

        JobManager jobManager = new DefaultJobManager();
        jobManager.start();

        List<Job> jobsToLaunch = new ArrayList<Job>();


        for (int i = 1; i < 10; i++) {

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

}
