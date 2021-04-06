package org.prototype.study.job;

import org.prototype.study.launch.DefaultJobRunner;
import org.prototype.study.launch.JobRunner;
import org.prototype.study.launch.JobSubmitter;
import org.prototype.study.launch.QueueJobSubmitter;

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


    private Job create(JobInputDataList jobInputDataList) {

        JobContext jobContext = new JobContext(jobInputDataList);
        Job job = new DefaultJob(jobContext);
        return job;
    }

    @Override
    public JobContext launch(JobInputDataList jobInputDataList) {
        Job job = create(jobInputDataList);
        this.jobSubmitter.submit(job);
        return  job.getJobExecutionContext();
    }
}
