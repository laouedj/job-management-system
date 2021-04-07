package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class JobSimpleQueue implements JobQueue {

    private final BlockingQueue<Job> jobQueue;

    public JobSimpleQueue(BlockingQueue<Job> jobQueue) {
        this.jobQueue = jobQueue;
    }

    public JobSimpleQueue() {
        this.jobQueue = new LinkedBlockingQueue();
    }


    @Override
    public void put(Job job) throws InterruptedException  {
            jobQueue.put(job);
            System.out.println(" Job Queued .....");
    }


    @Override
    public Job take() throws InterruptedException {

        Job result = jobQueue.take();
        return result;
    }


}
