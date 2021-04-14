package org.prototype.study;

import org.prototype.study.job.DefaultJobManager;
import org.prototype.study.job.Job;
import org.prototype.study.job.JobManager;
import org.prototype.study.job.parameters.JobInputParametersBuilder;
import org.prototype.study.job.samples.emailsending.SendingMailsJob;
import org.prototype.study.job.state.JobState;

import java.util.Properties;

/**
 * This is an implementation to launch the Job Management Service by line commande
 * Date parameter key should have .date suffix to considered as date
 * Format date in ISO-8601 calendar system such as 2007-12-03T10:15:30
 * Examples
 * schedule.date=2021-04-09T23:32:00
 * file.path=emails.txt
 * To specify a job priority ==> add a priority parameter , example
 * priority=LOW
 */
public class SendingMailJobCommandLine {

    private JobManager jobManager;

    SendingMailJobCommandLine(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    public static void main(String[] args) {

        SendingMailJobCommandLine sendingMailJobCommandLine = new SendingMailJobCommandLine(new DefaultJobManager());
        sendingMailJobCommandLine.start("Job One", args);

    }

    JobState start(String jobName, String[] parameters) {

        JobState result = null;
        JobInputParametersBuilder jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.fromProperties(fromArrayToProperties(parameters));

        Job job = new SendingMailsJob(jobInputParametersBuilder.toJobInputParameters());

        try {
            this.jobManager.start();
            this.jobManager.launchOne(job);
            job.getJobExecutionContext().getDone().await();

        } catch (Exception e) {
            System.out.println("Error occured when executing job " + jobName);
            e.printStackTrace();
        } finally {
            jobManager.shutdown();
            result = job.getJobExecutionContext().getStatus();
        }

        return result;
    }

    private Properties fromArrayToProperties(String[] parameters) {

        Properties result = new Properties();
        for (String param : parameters) {
            String[] paramToken = param.split("=");
            result.put(paramToken[0], paramToken[1]);
        }

        return result;
    }
}
