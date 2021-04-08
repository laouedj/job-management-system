package org.prototype.study;

import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;
import org.prototype.study.job.state.JobState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FailedJobWithoutSideEffectUnitTest {

    @Test
    public void shouldCompleteOthersJobsWhenOneFailed() throws InterruptedException {

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