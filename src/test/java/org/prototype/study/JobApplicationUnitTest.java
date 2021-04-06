package org.prototype.study;


import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;
import org.prototype.study.job.state.JobState;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class JobApplicationUnitTest
{

    @Test
    public void shouldCompleteJobSuccessfully() throws InterruptedException{

        JobManager jobManager = new DefaultJobManager();
        JobInputDataList jobInputDataList = new JobInputDataList();
        InputData param = new InputData(DataType.STRING, "HELLO");
        jobInputDataList.addInputData("Greating",param);
        Job job = new DefaultJob(jobInputDataList);
        JobContext jobContext = jobManager.launch(job);
        jobContext.getDoneSignal().await();
        assertEquals(jobContext.getStatus(), JobState.SUCCESS);
        assertNotNull(jobContext.getStartTime());
        assertNotNull(jobContext.getEndTime());
    }

    @Test
    public void shouldFailedWhenException() throws InterruptedException{

        JobManager jobManager = new DefaultJobManager();
        JobInputDataList jobInputDataList = new JobInputDataList();
        InputData param = new InputData(DataType.STRING, "HELLO");
        jobInputDataList.addInputData("Greating",param);
        Job job = new MockFailedJob(jobInputDataList);
        JobContext jobContext = jobManager.launch(job);
        jobContext.getDoneSignal().await();
        assertEquals(jobContext.getStatus(), JobState.FAILED);
        assertNotNull(jobContext.getStartTime());
        assertNotNull(jobContext.getEndTime());
        assertNotNull(jobContext.getError());

    }

    @Test
    public void shouldFailedWithoutParameters() throws InterruptedException{

        JobManager jobManager = new DefaultJobManager();
        JobInputDataList jobInputDataList = new JobInputDataList();
        Job job = new DefaultJob(jobInputDataList);
        JobContext jobContext = jobManager.launch(job);
        jobContext.getDoneSignal().await();
        assertEquals(jobContext.getStatus(), JobState.FAILED);
        assertNotNull(jobContext.getStartTime());
        assertNotNull(jobContext.getEndTime());
        assertNotNull(jobContext.getError());

    }
}
