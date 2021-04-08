package org.prototype.study;


import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;
import org.prototype.study.job.state.JobState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test for simple App.
 */
public class FailedJobUnitTest {


    @Test
    public void jobShouldFailedWhenException() throws InterruptedException {

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


}
