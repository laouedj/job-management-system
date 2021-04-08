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

public class JobPriorityUnitTest {

    private JobManager jobManager;

    @BeforeEach
    public void setUp() {
        this.jobManager = new DefaultJobManager();
        this.jobManager.start();
    }

    @Test
    public void shoudlRunJobsByPriority () throws InterruptedException {

        List<Job> jobsToLaunch = new ArrayList<Job>();

        JobInputDataList jobInputDataList = new JobInputDataList();
        InputData param = new InputData(DataType.STRING, "HELLO LOW JOB 1");
        jobInputDataList.addInputData("Greating", param);
        Job lowJob = new DefaultJob(jobInputDataList);
        lowJob.getJobExecutionContext().setPriority(JobPriority.LOW);
        jobsToLaunch.add(lowJob);

        jobInputDataList = new JobInputDataList();
        param = new InputData(DataType.STRING, "HELLO MEDIUM JOB");
        jobInputDataList.addInputData("Greating", param);
        Job mediumJob = new DefaultJob(jobInputDataList);
        mediumJob.getJobExecutionContext().setPriority(JobPriority.MEDIUM);
        jobsToLaunch.add(mediumJob);

        jobInputDataList = new JobInputDataList();
        param = new InputData(DataType.STRING, "HELLO HIGH JOB");
        jobInputDataList.addInputData("Greating", param);
        Job highJob = new DefaultJob(jobInputDataList);
        highJob.getJobExecutionContext().setPriority(JobPriority.HIGH);
        jobsToLaunch.add(highJob);

        jobInputDataList = new JobInputDataList();
        param = new InputData(DataType.STRING, "HELLO LOW JOB 2");
        jobInputDataList.addInputData("Greating", param);
        Job lowJob2 = new DefaultJob(jobInputDataList);
        lowJob2.getJobExecutionContext().setPriority(JobPriority.LOW);
        jobsToLaunch.add(lowJob2);

        jobInputDataList = new JobInputDataList();
        param = new InputData(DataType.STRING, "HELLO MEDIUM JOB 2");
        jobInputDataList.addInputData("Greating", param);
        Job mediumJob2 = new DefaultJob(jobInputDataList);
        mediumJob2.getJobExecutionContext().setPriority(JobPriority.MEDIUM);
        jobsToLaunch.add(mediumJob2);

        jobInputDataList = new JobInputDataList();
        param = new InputData(DataType.STRING, "HELLO HIGH JOB 2");
        jobInputDataList.addInputData("Greating", param);
        Job highJob2 = new DefaultJob(jobInputDataList);
        highJob2.getJobExecutionContext().setPriority(JobPriority.HIGH);
        jobsToLaunch.add(highJob2);

        jobManager.launchMany(jobsToLaunch);

        Thread.sleep(10000L);

/*
        JobContext jobContext = job.getJobExecutionContext();
        jobContext.getDone().await();
        assertEquals(jobContext.getStatus(), JobState.SUCCESS);
        assertNotNull(jobContext.getStartTime());
        assertNotNull(jobContext.getEndTime());
*/

    }

    @AfterEach
    void tearDown() {
        jobManager.shutdown();
    }
}
