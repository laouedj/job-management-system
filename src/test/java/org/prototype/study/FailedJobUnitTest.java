package org.prototype.study;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;
import org.prototype.study.job.parameters.DataType;
import org.prototype.study.job.parameters.JobInputDataList;
import org.prototype.study.job.state.InputData;
import org.prototype.study.job.state.JobState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test for simple App.
 */
public class FailedJobUnitTest {

    private JobManager jobManager;

    @BeforeEach
    public void setUp() {
        this.jobManager = new DefaultJobManager();
        this.jobManager.start();
   }


    @Test
    public void jobShouldFailedWhenException() throws InterruptedException {


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



    }


    @AfterEach
    void tearDown() {
        jobManager.shutdown();
    }


}
