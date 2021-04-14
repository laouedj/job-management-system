package org.prototype.study;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;
import org.prototype.study.job.parameters.JobInputParametersBuilder;
import org.prototype.study.job.parameters.ParameterType;
import org.prototype.study.job.state.JobState;

import java.time.LocalDateTime;
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


        JobInputParametersBuilder jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("param.key.1", "Hello", ParameterType.STRING);
        jobInputParametersBuilder.addInputParameter("param.key.2", 7, ParameterType.LONG);
        jobInputParametersBuilder.addInputParameter("param.key.3", LocalDateTime.now(), ParameterType.DATE);

        Job job = new DefaultJob(jobInputParametersBuilder.toJobInputParameters());

        jobManager.launchOne(job);


        JobContext jobContext = job.getJobExecutionContext();
        jobContext.getDone().await();
        assertEquals(jobContext.getStatus(), JobState.SUCCESS, "Job should succeed");
        assertNotNull(jobContext.getStartTime(), "Start Time is null");
        assertNotNull(jobContext.getEndTime(), "End Time is null");

    }


    @Test
    public void jobsShouldCompleteSuccessfullyWhenLaunchedOneByOne() throws InterruptedException {

        List<Job> jobsToLaunch = new ArrayList<Job>();

        for (int i = 1; i < 5; i++) {

            JobInputParametersBuilder jobInputParametersBuilder = new JobInputParametersBuilder();
            jobInputParametersBuilder.addInputParameter("param.key.1", "Hello " + i, ParameterType.STRING);
            jobInputParametersBuilder.addInputParameter("param.key.2", i, ParameterType.LONG);
            jobInputParametersBuilder.addInputParameter("param.key.3", LocalDateTime.now(), ParameterType.DATE);


            Job job = new DefaultJob(jobInputParametersBuilder.toJobInputParameters());

            jobsToLaunch.add(job);
        }


        for (Job job : jobsToLaunch) {

            jobManager.launchOne(job);

        }


        for (Job job : jobsToLaunch) {

            JobContext jobContext = job.getJobExecutionContext();
            jobContext.getDone().await();
            assertEquals(jobContext.getStatus(), JobState.SUCCESS, "Job should succeed");
            assertNotNull(jobContext.getStartTime(), "Start Time is null");
            assertNotNull(jobContext.getEndTime(), "End Time is null");

        }
    }

    @Test
    public void jobsShouldCompleteSuccessfullyWhenLaunchedByBulk() throws InterruptedException {

        List<Job> jobsToLaunch = new ArrayList<Job>();

        for (int i = 1; i < 5; i++) {

            JobInputParametersBuilder jobInputParametersBuilder = new JobInputParametersBuilder();
            jobInputParametersBuilder.addInputParameter("param.key.1", "Hello " + i, ParameterType.STRING);
            jobInputParametersBuilder.addInputParameter("param.key.2", i, ParameterType.LONG);
            jobInputParametersBuilder.addInputParameter("param.key.3", LocalDateTime.now(), ParameterType.DATE);


            Job job = new DefaultJob(jobInputParametersBuilder.toJobInputParameters());
            jobsToLaunch.add(job);

        }

        jobManager.launchMany(jobsToLaunch);


        for (Job job : jobsToLaunch) {
            JobContext jobContext = job.getJobExecutionContext();
            jobContext.getDone().await();
            assertEquals(jobContext.getStatus(), JobState.SUCCESS, "Job should succeed");
            assertNotNull(jobContext.getStartTime(), "Start Time is null");
            assertNotNull(jobContext.getEndTime(), "End Time is null");
        }
    }

    @AfterEach
    void tearDown() {
        jobManager.shutdown();
    }


}
