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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SuccessfulScheduledJobUnitTest {

    private JobManager jobManager;

    // Accepted late time duration (in seconds) to begin a scheduled job
    private static final long ACCEPTED_DELTA_DURATION = 2;

    @BeforeEach
    public void setUp() {
        this.jobManager = new DefaultJobManager();
        this.jobManager.start();
    }

    @Test
    public void oneScheduledJobShouldCompleteSuccessfully() throws InterruptedException {


        LocalDateTime scheduleDate = LocalDateTime.now().plusSeconds(10);
        JobInputParametersBuilder jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("schedule.date", scheduleDate, ParameterType.DATE);
        JobInputParameters jobInputParameters = jobInputParametersBuilder.toJobInputParameters();


        Job job = new DefaultJob(jobInputParameters);

        jobManager.launchOne(job);


        JobContext jobContext = job.getJobExecutionContext();
        jobContext.getDone().await();
        assertEquals(jobContext.getStatus(), JobState.SUCCESS, "Job should succeed");

        // Assert that the job started date is closed to schedule date
        // We accept a delta between start date & scheduled date
        assertNotNull(jobContext.getStartTime(), "Start Time is null");
        long diff = ChronoUnit.SECONDS.between(jobContext.getStartTime(), (LocalDateTime)jobInputParameters.getJobInputParameter("schedule.date"));
        assertTrue(diff< ACCEPTED_DELTA_DURATION, "Job has started to late");

        assertNotNull(jobContext.getEndTime(), "End Time is null");

    }


    @Test
    public void scheduledJobsShouldCompleteSuccessfullyWhenLaunchedOneByOne() throws InterruptedException {

        List<Job> jobsToLaunch = new ArrayList<Job>();
        LocalDateTime scheduleDate = LocalDateTime.now();

        for (int i = 1; i < 5; i++) {

            JobInputParametersBuilder jobInputParametersBuilder = new JobInputParametersBuilder();
            jobInputParametersBuilder.addInputParameter("param.1", "HELLO " + i, ParameterType.STRING);
            jobInputParametersBuilder.addInputParameter("schedule.date", scheduleDate.plusSeconds(i*10), ParameterType.DATE);
            JobInputParameters jobInputParameters = jobInputParametersBuilder.toJobInputParameters();
            Job job = new DefaultJob(jobInputParameters);
            jobsToLaunch.add(job);
        }


        for (Job job : jobsToLaunch) {

            jobManager.launchOne(job);

        }


        for (Job job : jobsToLaunch) {

            JobContext jobContext = job.getJobExecutionContext();
            jobContext.getDone().await();
            assertEquals(jobContext.getStatus(), JobState.SUCCESS, "Job should succeed");

            // Assert that the job has started at schedule date
            assertNotNull(jobContext.getStartTime(), "Start Time is null");
            long diff = ChronoUnit.SECONDS.between(jobContext.getStartTime(), (LocalDateTime)jobContext.getJobInputParameters().getJobInputParameter("schedule.date"));
            assertTrue(diff< ACCEPTED_DELTA_DURATION, "Job has started to late");

            assertNotNull(jobContext.getEndTime(), "End Time is null");

        }

    }

    @Test
    public void shouldCompleteManyJobSuccessfully() throws InterruptedException {


        LocalDateTime scheduleDate = LocalDateTime.now();
        List<Job> jobsToLaunch = new ArrayList<Job>();



        for (int i = 1; i < 10; i++) {

            JobInputParametersBuilder jobInputParametersBuilder = new JobInputParametersBuilder();
            jobInputParametersBuilder.addInputParameter("param.1", "HELLO " + i, ParameterType.STRING);
            jobInputParametersBuilder.addInputParameter("schedule.date", scheduleDate.plusSeconds(i*10), ParameterType.DATE);
            JobInputParameters jobInputParameters = jobInputParametersBuilder.toJobInputParameters();
            Job job = new DefaultJob(jobInputParameters);
            jobsToLaunch.add(job);

        }

            jobManager.launchMany(jobsToLaunch);


            for (Job job : jobsToLaunch) {
                JobContext jobContext = job.getJobExecutionContext();
                jobContext.getDone().await();
                assertEquals(jobContext.getStatus(), JobState.SUCCESS, "Job should success");

                // Assert that the job has started at schedule date
                assertNotNull(jobContext.getStartTime(),"Start Time is null");
                long diff = ChronoUnit.SECONDS.between(jobContext.getStartTime(), (LocalDateTime)jobContext.getJobInputParameters().getJobInputParameter("schedule.date"));
                assertTrue(diff< ACCEPTED_DELTA_DURATION, "Job has started too late");


                assertNotNull(jobContext.getEndTime(),"End Time is null");
            }


    }

    @AfterEach
    void tearDown() {
        jobManager.shutdown();
    }
}