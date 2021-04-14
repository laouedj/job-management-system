package org.prototype.study.job;

import org.prototype.study.job.launch.*;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class DefaultJobManager implements JobManager {

    private JobProducer jobProducer;
    private JobConsumer jobConsumer;
    private JobQueue jobQueue;


    public DefaultJobManager() {

        this.jobQueue = new JobBlockingQueue(new PriorityBlockingQueue<Job>());
        this.jobProducer = new DefaultJobProducer(this.jobQueue);
        this.jobConsumer = new DefaultJobConsumer(this.jobQueue);
    }


    @Override
    public void launchOne(Job job) {

        this.jobProducer.produceOne(job);
    }

    @Override
    public void launchMany(List<Job> jobs) {

        if (jobs == null || jobs.isEmpty()) {
            return;
        }

        this.jobProducer.produceMany(jobs);
    }

    @Override
    public void shutdown() {
        this.jobConsumer.shutdown();
        this.jobProducer.start();
    }


    @Override
    public void start() {
        this.jobConsumer.start();
        this.jobProducer.start();
    }

}
