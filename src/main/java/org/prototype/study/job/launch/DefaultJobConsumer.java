package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

import java.util.concurrent.CountDownLatch;

public class DefaultJobConsumer implements JobConsumer {

    private JobQueue jobQueue;
    private JobRunner jobRunner;

    private CountDownLatch end;

    public DefaultJobConsumer(JobQueue jobQueue) {
        this.jobQueue = jobQueue;
        jobRunner = new GlobalJobRunner();
    }

    @Override
    public void start() {
        this.jobRunner.start();
        end = new CountDownLatch(1);
    }

    @Override
    public void consume(Job job) {
        if (job != null) {
            System.out.println("Job taken ....." + job.getJobExecutionContext().getJobInputParameters());
            System.out.println("Launching Job with priority " + job.getJobExecutionContext().getPriority());
            jobRunner.execute(job);
        } else {
            System.out.println("No job to execute  ! .....");
        }
    }

    @Override
    public void shutdown() {
        this.jobRunner.shutdown();
        end.countDown();
    }

    @Override
    public void run() {
        while (canRun()) {
            try {
                System.out.println("Waiting for jobs .....");
                Job job = jobQueue.take();
                consume(job);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    private boolean canRun() {
        return (this.end != null) && (this.end.getCount() != 0);
    }



}
