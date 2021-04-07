package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.state.JobState;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DefaultJobProducer implements JobProducer {

    private final JobQueue jobQueue;

    public DefaultJobProducer(JobQueue jobQueue) {
        this.jobQueue = jobQueue;
    }

    @Override
    public void produceOne(Job job) {
        try {
            System.out.println(" Submitting Job .....");
            job.getJobExecutionContext().setStatus(JobState.QUEUED);
            jobQueue.put(job);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void produceMany(List<Job> jobs) {

        if (jobs == null || jobs.isEmpty()) {
            return;
        }
        for (Job job : jobs) {
            produceOne(job);
        }

    }

}
