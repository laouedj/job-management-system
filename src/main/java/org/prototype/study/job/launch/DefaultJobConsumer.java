package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

import java.util.concurrent.CountDownLatch;

public class DefaultJobConsumer implements JobConsumer {

    private JobQueue jobQueue;
    private JobRunner jobRunner;

    private CountDownLatch end = new CountDownLatch(1);

    public DefaultJobConsumer(JobQueue jobQueue) {
        this.jobQueue = jobQueue;
        jobRunner = new DefaultJobRunner();
    }

    @Override
    public void consume(Job job) {
        if (job != null) {
            System.out.println("Job taken .....");
            jobRunner.execute(job);
        } else {
            System.out.println("Not Job available yet ! .....");
        }
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
        return (this.end.getCount() != 0);
    }

}
