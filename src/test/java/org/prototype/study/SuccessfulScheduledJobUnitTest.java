package org.prototype.study;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;
import org.prototype.study.job.parameters.DataType;
import org.prototype.study.job.parameters.JobInputDataList;
import org.prototype.study.job.state.InputData;
import org.prototype.study.job.state.JobState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SuccessfulScheduledJobUnitTest {

    private JobManager jobManager;

    @BeforeEach
    public void setUp() {
        this.jobManager = new DefaultJobManager();
        this.jobManager.start();
    }

    @Test
    public void oneScheduledJobShouldCompleteSuccessfully() throws InterruptedException {

        JobInputDataList jobInputDataList = new JobInputDataList();
        LocalDateTime scheduleDate = LocalDateTime.now().plusSeconds(10);

        InputData param = new InputData(DataType.DATE, scheduleDate.toString());
        jobInputDataList.addInputData("schedule.date", param);
        Job job = new DefaultJob(jobInputDataList);

        jobManager.launchOne(job);


        JobContext jobContext = job.getJobExecutionContext();
        jobContext.getDone().await();
        assertEquals(jobContext.getStatus(), JobState.SUCCESS);
        assertNotNull(jobContext.getStartTime());
        assertNotNull(jobContext.getEndTime());

    }


    @Test
    public void scheduledJobsShouldCompleteSuccessfullyWhenLaunchedOneByOne() throws InterruptedException {

        List<Job> jobsToLaunch = new ArrayList<Job>();
        LocalDateTime scheduleDate = LocalDateTime.now();

        for (int i = 1; i < 10; i++) {

            JobInputDataList jobInputDataList = new JobInputDataList();
            InputData param = new InputData(DataType.STRING, "HELLO " + i);
            jobInputDataList.addInputData("Greating", param);
            if (i == 3) {
                param = new InputData(DataType.DATE, scheduleDate.plusSeconds(10).toString());
                jobInputDataList.addInputData("schedule.date", param);
            }

            if (i == 6) {
                param = new InputData(DataType.DATE, scheduleDate.plusSeconds(20).toString());
                jobInputDataList.addInputData("schedule.date", param);
            }

            if (i == 9) {
                param = new InputData(DataType.DATE, scheduleDate.plusSeconds(30).toString());
                jobInputDataList.addInputData("schedule.date", param);
            }
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
    public void shouldCompleteManyJobSuccessfully() throws InterruptedException {


        LocalDateTime scheduleDate = LocalDateTime.now();
        List<Job> jobsToLaunch = new ArrayList<Job>();



        for (int i = 1; i < 10; i++) {

            JobInputDataList jobInputDataList = new JobInputDataList();
            InputData param = new InputData(DataType.STRING, "HELLO " + i);
            jobInputDataList.addInputData("Greating", param);
            if (i == 3) {
                param = new InputData(DataType.DATE, scheduleDate.plusSeconds(10).toString());
                jobInputDataList.addInputData("schedule.date", param);
            }

            if (i == 6) {
                param = new InputData(DataType.DATE, scheduleDate.plusSeconds(20).toString());
                jobInputDataList.addInputData("schedule.date", param);
            }

            if (i == 9) {
                param = new InputData(DataType.DATE, scheduleDate.plusSeconds(30).toString());
                jobInputDataList.addInputData("schedule.date", param);
            }

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