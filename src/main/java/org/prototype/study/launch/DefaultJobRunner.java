package org.prototype.study.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.JobContext;
import org.prototype.study.job.JobState;

import java.util.Date;
import java.util.concurrent.*;

public class DefaultJobRunner implements JobRunner {

    private ExecutorService executorService;

    private static final int DEFAULT_CORE_POOL_SIZE = 3;

    public DefaultJobRunner(int corePoolSize) {
        this.executorService = Executors.newFixedThreadPool(corePoolSize);
    }

    public DefaultJobRunner() {
        this(DEFAULT_CORE_POOL_SIZE);
    }


    @Override
    public void execute(Job job) {
        System.out.println("Start Running job  .....");
        job.getJobExecutionContext().setStartTime(new Date());

        CompletableFuture<JobContext> task = CompletableFuture.supplyAsync(job, this.executorService)
                .whenCompleteAsync((result, exception) -> {
                    if (exception != null) {
                        System.out.println("exception occurs");
                        System.err.println(exception);
                    } else {
                        result.setStatus(JobState.SUCCESS);
                    }
                })
                .exceptionally(throwable ->
                {
                    job.getJobExecutionContext().setStatus(JobState.FAILED);
                    return job.getJobExecutionContext();
                })
                .thenApply(result ->
                {
                    result.setEndTime(new Date());
                    System.out.println("Finish Running job  .....");
                    result.getDoneSignal().countDown();
                    return result;
                });
      }

}
