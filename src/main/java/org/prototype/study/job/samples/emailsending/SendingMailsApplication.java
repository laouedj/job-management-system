package org.prototype.study.job.samples.emailsending;

import org.prototype.study.job.DefaultJobManager;
import org.prototype.study.job.Job;
import org.prototype.study.job.JobManager;
import org.prototype.study.job.parameters.JobInputParametersBuilder;
import org.prototype.study.job.parameters.ParameterType;
import org.prototype.study.job.state.JobState;

public class SendingMailsApplication {

    public static void main( String[] args )  {

        JobState resultState = null;

        JobManager jobManager = new DefaultJobManager();
        JobInputParametersBuilder jobInputParametersBuilder = new JobInputParametersBuilder();
        jobInputParametersBuilder.addInputParameter("file.path", "emails.txt", ParameterType.STRING);
        Job sendingMailsJob = new SendingMailsJob(jobInputParametersBuilder.toJobInputParameters());


        jobManager.start();

        try{
            jobManager.launchOne(sendingMailsJob);
            sendingMailsJob.getJobExecutionContext().getDone().await();

        }catch (Exception e) {
            System.out.println("Error occured when executing sending mails job");
            e.printStackTrace();
        }finally {
            resultState = sendingMailsJob.getJobExecutionContext().getStatus();
            jobManager.shutdown();
        }

        System.out.println("Job finished with status " + resultState);


    }
}
