package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.JobContext;
import org.prototype.study.job.state.StateManager;
import org.prototype.study.job.state.StateUpdater;

import java.util.Date;
import java.util.concurrent.*;

public class DefaultJobRunner implements JobRunner {

    private static final int DEFAULT_CORE_POOL_SIZE = 3;

    private ExecutorService executorService;
    private StateUpdater stateManager;
    private boolean started = false;

    public DefaultJobRunner(StateUpdater stateManager,ExecutorService executorService) {
        this.executorService = executorService;
        this.stateManager = stateManager;
    }

    public DefaultJobRunner() {
        this(new StateManager(),Executors.newFixedThreadPool(DEFAULT_CORE_POOL_SIZE));
    }


    @Override
    public void start() {
        started = true;
    }

    @Override
    public void execute(Job job) {

        if (!started) {
            System.out.println("Job Runner not started .....");
            throw new RuntimeException("Job Runner not started .....");
        }
        System.out.println("Start Running job  .....");
        job.getJobExecutionContext().setStartTime(new Date());
        this.stateManager.toNextState(job);
        CompletableFuture<JobContext> task = CompletableFuture.supplyAsync(job, this.executorService)
                .whenCompleteAsync((result, exception) -> {
                    if (exception != null) {
                        System.out.println("exception occurs" + exception);
                        result.setError(exception);
                    } else {
                        System.out.println("No exception occurs");
                    }
                })
                .exceptionally(throwable ->
                {
                    job.getJobExecutionContext().setError(throwable);
                    System.out.println("exception occurs" + throwable);
                    return job.getJobExecutionContext();
                })
                .thenApply(result ->
                {
                    result.setEndTime(new Date());
                    this.stateManager.toNextState(job);
                    result.getDone().countDown();
                    System.out.println("Finish Running job  .....");
                    return result;
                });
      }

    @Override
    public void shutdown() {
        this.executorService.shutdown();
        this.started = false;
    }

}
