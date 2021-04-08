package org.prototype.study;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;
import org.prototype.study.job.parameters.DataType;
import org.prototype.study.job.parameters.JobInputDataList;
import org.prototype.study.job.state.InputData;
import org.prototype.study.job.state.JobState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SuccessfulImmediateJobUnitTest {


    private JobManager jobManager;

    @BeforeEach
    public void setUp() {
        this.jobManager = new DefaultJobManager();
        this.jobManager.start();
    }

    @Test
    public void oneJobShouldCompleteSuccessfully() throws InterruptedException {

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

    }


    @Test
    public void jobsShouldCompleteSuccessfullyWhenLaunchedOneByOne() throws InterruptedException {

        List<Job> jobsToLaunch = new ArrayList<Job>();

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
     }

    @Test
    public void jobsShouldCompleteSuccessfullyWhenLaunchedByBulk() throws InterruptedException {

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
     }

    @AfterEach
    void tearDown() {
        jobManager.shutdown();
    }


}
