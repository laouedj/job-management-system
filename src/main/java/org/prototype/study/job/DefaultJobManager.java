package org.prototype.study.job;

import org.prototype.study.job.launch.*;

import java.util.List;
import java.util.concurrent.*;

public class DefaultJobManager implements JobManager{

    private ExecutorService executorService;
    private static final int DEFAULT_CORE_POOL_SIZE = 3;
    private JobProducer jobProducer;
    private JobConsumer jobConsumer;
    private  JobQueue jobQueue;


    public DefaultJobManager() {
        this.executorService = Executors.newFixedThreadPool(DEFAULT_CORE_POOL_SIZE);
        this.jobQueue = new JobSimpleQueue();
        this.jobProducer = new DefaultJobProducer(this.jobQueue);
        this.jobConsumer = new DefaultJobConsumer(this.jobQueue);
        executorService.execute(this.jobConsumer);
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


}
