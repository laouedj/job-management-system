package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.state.JobState;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class QueueJobSubmitter implements JobSubmitter {

    private final BlockingQueue<Job> jobQueue;
    private final JobRunner jobRunner;

    public QueueJobSubmitter(BlockingQueue<Job> jobQueue, JobRunner jobRunner) {
        this.jobQueue = jobQueue;
        this.jobRunner = jobRunner;
    }

    public QueueJobSubmitter() {
        this(new LinkedBlockingDeque(),new DefaultJobRunner());
    }




    private void doRun() {
        try {

            Job jobToExecute = jobQueue.take();
            System.out.println("Job taken .....");
            jobRunner.execute(jobToExecute);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

    }


    @Override
    public void run() {
       doRun();
    }

    @Override
    public void submit(Job job) {
        try {
            System.out.println(" Submitting Job .....");
            job.getJobExecutionContext().setStatus(JobState.QUEUED);
            jobQueue.put(job);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
