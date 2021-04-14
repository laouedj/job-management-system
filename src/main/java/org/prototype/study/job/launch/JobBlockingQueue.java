package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class JobBlockingQueue implements JobQueue {

    private final BlockingQueue<Job> jobQueue;

    public JobBlockingQueue(BlockingQueue<Job> jobQueue) {
        this.jobQueue = jobQueue;
    }

    public JobBlockingQueue() {
        this.jobQueue = new LinkedBlockingQueue<Job>();
    }

    @Override
    public void put(Job job) throws InterruptedException  {
            jobQueue.put(job);
            System.out.println(" Job Queued .....");
    }


    @Override
    public Job take() throws InterruptedException {

        Job result = jobQueue.poll(5, TimeUnit.SECONDS);
        return result;
    }


}
