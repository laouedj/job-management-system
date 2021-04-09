package org.prototype.study.job.launch;

import org.prototype.study.ConfigurationManager;
import org.prototype.study.job.Job;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class JobBlockingQueue implements JobQueue {

    private final BlockingQueue<Job> jobQueue;
    private static final int DEFAULT_QUEUE_POLLING_TIMEOUT = 1;  // In Seconds
    private int queuePollingTimeout = DEFAULT_QUEUE_POLLING_TIMEOUT;

    public JobBlockingQueue(BlockingQueue<Job> jobQueue) {

        if (ConfigurationManager.getPropertyValue("job.queue.polling.timeout") != null) {
            queuePollingTimeout = Integer.valueOf(ConfigurationManager.getPropertyValue("job.queue.polling.timeout"));
        }

        this.jobQueue = jobQueue;

        System.out.println(" Job Queue Started with Polling timeout " + queuePollingTimeout);
    }

    public JobBlockingQueue() {
        this(new LinkedBlockingQueue<Job>());
    }

    @Override
    public void put(Job job) throws InterruptedException  {
            jobQueue.put(job);
            System.out.println(" Job Queued .....");
    }


    @Override
    public Job take() throws InterruptedException {

        Job result = jobQueue.poll(queuePollingTimeout, TimeUnit.SECONDS);
        return result;
    }


}
