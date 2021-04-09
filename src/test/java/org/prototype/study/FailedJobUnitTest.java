package org.prototype.study;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;
import org.prototype.study.job.parameters.JobInputParametersBuilder;
import org.prototype.study.job.parameters.ParameterType;
import org.prototype.study.job.parameters.JobInputParameters;
import org.prototype.study.job.parameters.JobInputParameter;
import org.prototype.study.job.state.JobState;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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

        JobInputParametersBuilder jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("param.key.1", "Hello ", ParameterType.STRING);
        jobInputParametersBuilder.addInputParameter("param.key.2", 4 , ParameterType.LONG);
        jobInputParametersBuilder.addInputParameter("param.key.3",  LocalDateTime.now(), ParameterType.DATE);
        Job job = new MockFailedJob(jobInputParametersBuilder.toJobInputParameters());


        jobManager.launchOne(job);


        JobContext jobContext = job.getJobExecutionContext();
        jobContext.getDone().await();
        assertEquals(jobContext.getStatus(), JobState.FAILED, "Job should failed");
        assertNotNull(jobContext.getStartTime(),"Start Time should not be null");
        assertNotNull(jobContext.getEndTime(), "End Time should not be null");
        assertNotNull(jobContext.getError(),"Error should not be null");



    }


    @AfterEach
    void tearDown() {
        jobManager.shutdown();
    }


}
