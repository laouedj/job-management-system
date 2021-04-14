package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.state.JobState;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultJobProducer implements JobProducer {

    private final JobQueue jobQueue;
    private boolean started = false;


    public DefaultJobProducer(JobQueue jobQueue) {
        this.jobQueue = jobQueue;
    }

    @Override
    public void produceOne(Job job) {
        if (!started) {
            System.out.println("Job Producer not started .....");
            throw new RuntimeException("Job Producer not started .....");
        }

        try {
            System.out.println(" Submitting Job ....." + job.getJobExecutionContext().getJobInputParameters());
            jobQueue.put(job);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void produceMany(List<Job> jobs) {

        if (!started) {
            System.out.println("Job Producer not started .....");
            throw new RuntimeException("Job Producer not started .....");
        }

        if (jobs == null || jobs.isEmpty()) {
            return;
        }

        List<Job> sortedJobsByPriority = jobs.stream().sorted().collect(Collectors.toList());

        for (Job job : sortedJobsByPriority) {
            produceOne(job);
        }

    }

    @Override
    public void start() {
        this.started = true;
    }

    @Override
    public void shutdown() {
        this.started = false;
    }

}
