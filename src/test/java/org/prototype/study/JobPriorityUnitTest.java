package org.prototype.study;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;
import org.prototype.study.job.parameters.JobInputParametersBuilder;
import org.prototype.study.job.parameters.ParameterType;
import org.prototype.study.job.parameters.JobInputParameters;
import org.prototype.study.job.parameters.JobInputParameter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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


        JobInputParametersBuilder jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("param.key.1", "Job One with low priority", ParameterType.STRING);
        jobInputParametersBuilder.addInputParameter("param.key.2", 4 , ParameterType.LONG);
        jobInputParametersBuilder.addInputParameter("param.key.3",  LocalDateTime.now(), ParameterType.DATE);
        Job lowJob = new DefaultJob(jobInputParametersBuilder.toJobInputParameters());
        lowJob.getJobExecutionContext().setPriority(JobPriority.LOW);
        jobsToLaunch.add(lowJob);


        jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("param.key.1", "Job Two with medium priority", ParameterType.STRING);
        jobInputParametersBuilder.addInputParameter("param.key.2", 4 , ParameterType.LONG);
        jobInputParametersBuilder.addInputParameter("param.key.3",  LocalDateTime.now(), ParameterType.DATE);
        Job mediumJob = new DefaultJob(jobInputParametersBuilder.toJobInputParameters());
        mediumJob.getJobExecutionContext().setPriority(JobPriority.MEDIUM);
        jobsToLaunch.add(mediumJob);


        jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("param.key.1", "Job Three with high priority", ParameterType.STRING);
        jobInputParametersBuilder.addInputParameter("param.key.2", 4 , ParameterType.LONG);
        jobInputParametersBuilder.addInputParameter("param.key.3",  LocalDateTime.now(), ParameterType.DATE);
        Job highJob = new DefaultJob(jobInputParametersBuilder.toJobInputParameters());
        highJob.getJobExecutionContext().setPriority(JobPriority.HIGH);
        jobsToLaunch.add(highJob);


        jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("param.key.1", "Job Four with low priority", ParameterType.STRING);
        jobInputParametersBuilder.addInputParameter("param.key.2", 4 , ParameterType.LONG);
        jobInputParametersBuilder.addInputParameter("param.key.3",  LocalDateTime.now(), ParameterType.DATE);
        Job lowJob2 = new DefaultJob(jobInputParametersBuilder.toJobInputParameters());
        lowJob2.getJobExecutionContext().setPriority(JobPriority.LOW);
        jobsToLaunch.add(lowJob2);

        jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("param.key.1", "Job Five with medium priority", ParameterType.STRING);
        jobInputParametersBuilder.addInputParameter("param.key.2", 4 , ParameterType.LONG);
        jobInputParametersBuilder.addInputParameter("param.key.3",  LocalDateTime.now(), ParameterType.DATE);
        Job mediumJob2 = new DefaultJob(jobInputParametersBuilder.toJobInputParameters());
        mediumJob2.getJobExecutionContext().setPriority(JobPriority.MEDIUM);
        jobsToLaunch.add(mediumJob2);

        jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("param.key.1", "Job Six  with high priority", ParameterType.STRING);
        jobInputParametersBuilder.addInputParameter("param.key.2", 4 , ParameterType.LONG);
        jobInputParametersBuilder.addInputParameter("param.key.3",  LocalDateTime.now(), ParameterType.DATE);
        Job highJob2 = new DefaultJob(jobInputParametersBuilder.toJobInputParameters());
        highJob2.getJobExecutionContext().setPriority(JobPriority.HIGH);
        jobsToLaunch.add(highJob2);

        jobManager.launchMany(jobsToLaunch);


        //Waiting all job to finish
        for (Job job : jobsToLaunch) {

            JobContext jobContext = job.getJobExecutionContext();
            jobContext.getDone().await();

        }

        // Compare start date between jobs to assert that they have executed in priority
        assertTrue(highJob.getJobExecutionContext().getStartTime().isBefore(mediumJob.getJobExecutionContext().getStartTime()), "A Job with high priority should be executed before a job  with medium priority");
        assertTrue(mediumJob.getJobExecutionContext().getStartTime().isBefore(lowJob.getJobExecutionContext().getStartTime()), "A Job with medium priority should be executed before a job  with low priority");
        assertTrue(highJob2.getJobExecutionContext().getStartTime().isBefore(mediumJob2.getJobExecutionContext().getStartTime()), "A Job with high priority should be executed before a job  with medium priority");
        assertTrue(mediumJob2.getJobExecutionContext().getStartTime().isBefore(lowJob2.getJobExecutionContext().getStartTime()),"A Job with medium priority should be executed before a job  with low priority");


    }

    @AfterEach
    void tearDown() {
        jobManager.shutdown();
    }
}
