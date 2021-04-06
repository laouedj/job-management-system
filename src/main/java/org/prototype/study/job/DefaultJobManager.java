package org.prototype.study.job;

import org.prototype.study.job.launch.JobSubmitter;
import org.prototype.study.job.launch.QueueJobSubmitter;

import java.util.List;
import java.util.concurrent.*;

public class DefaultJobManager implements JobManager{

    private ExecutorService executorService;
    private static final int DEFAULT_CORE_POOL_SIZE = 3;
    private JobSubmitter jobSubmitter;

    public DefaultJobManager(JobSubmitter jobSubmitter, ExecutorService executorService) {
      this.jobSubmitter= jobSubmitter;
      this.executorService = executorService;
    }

    public DefaultJobManager() {
        this(new QueueJobSubmitter(),Executors.newFixedThreadPool(DEFAULT_CORE_POOL_SIZE));
        executorService.execute(this.jobSubmitter);
    }

    @Override
    public void launchOne(Job job) {
        this.jobSubmitter.submitOne(job);
    }

    @Override
    public void launchMany(List<Job> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return;
        }
        this.jobSubmitter.submitMany(jobs);
    }

    @Override
    public void waitToFinish() throws InterruptedException {
        jobSubmitter.waitToFinish();
    }
}
