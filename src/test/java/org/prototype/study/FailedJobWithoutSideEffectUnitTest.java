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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FailedJobWithoutSideEffectUnitTest {

    private JobManager jobManager;

    @BeforeEach
    public void setUp() {
        this.jobManager = new DefaultJobManager();
        this.jobManager.start();
    }

    @Test
    public void shouldCompleteOthersJobsWhenOneFailed() throws InterruptedException {


        JobInputParametersBuilder jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("param.key.1", "Job One ", ParameterType.STRING);
        jobInputParametersBuilder.addInputParameter("param.key.2", 4 , ParameterType.LONG);
        jobInputParametersBuilder.addInputParameter("param.key.3",  LocalDateTime.now(), ParameterType.DATE);
        Job jobOne = new DefaultJob(jobInputParametersBuilder.toJobInputParameters());


        jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("param.key.1", "Job Two", ParameterType.STRING);
        jobInputParametersBuilder.addInputParameter("param.key.2", 4 , ParameterType.LONG);
        jobInputParametersBuilder.addInputParameter("param.key.3",  LocalDateTime.now(), ParameterType.DATE);
        Job jobTwo = new MockFailedJob(jobInputParametersBuilder.toJobInputParameters());




        jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("param.key.1", "Job Three", ParameterType.STRING);
        jobInputParametersBuilder.addInputParameter("param.key.2", 4 , ParameterType.LONG);
        jobInputParametersBuilder.addInputParameter("param.key.3",  LocalDateTime.now(), ParameterType.DATE);
        Job jobThree = new DefaultJob(jobInputParametersBuilder.toJobInputParameters());


        List<Job> jobsToLaunch = new ArrayList<Job>();
        jobsToLaunch.add(jobOne);
        jobsToLaunch.add(jobTwo);
        jobsToLaunch.add(jobThree);


        jobManager.launchMany(jobsToLaunch);


        JobContext jobContextOne = jobOne.getJobExecutionContext();
        jobContextOne.getDone().await();
        assertEquals(jobContextOne.getStatus(), JobState.SUCCESS, "Job should succeed");
        assertNotNull(jobContextOne.getStartTime(), "Start Time should not be null");
        assertNotNull(jobContextOne.getEndTime(), "End Time should not be null");

        JobContext jobContextTwo = jobTwo.getJobExecutionContext();
        jobContextTwo.getDone().await();
        assertEquals(jobContextTwo.getStatus(), JobState.FAILED, "Job should failed");
        assertNotNull(jobContextTwo.getStartTime(),"Start Time should not be null");
        assertNotNull(jobContextTwo.getEndTime(),"End Time should not be null");
        assertNotNull(jobContextTwo.getError(),"Error should not be null");


        JobContext jobContextThree = jobThree.getJobExecutionContext();
        jobContextThree.getDone().await();
        assertEquals(jobContextThree.getStatus(), JobState.SUCCESS, "Job should succeed");
        assertNotNull(jobContextThree.getStartTime(), "Start Time should not be null");
        assertNotNull(jobContextThree.getEndTime(), "End Time should not be null");

    }

    @AfterEach
    void tearDown() {
        jobManager.shutdown();
    }

}
