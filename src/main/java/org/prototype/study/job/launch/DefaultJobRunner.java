package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.JobContext;
import org.prototype.study.job.state.StateManager;
import org.prototype.study.job.state.StateUpdater;

import java.util.Date;
import java.util.concurrent.*;

public class DefaultJobRunner implements JobRunner {

    private ExecutorService executorService;

    private static final int DEFAULT_CORE_POOL_SIZE = 3;

    private StateUpdater stateManager;
    private CountDownLatch endSignal;

    public DefaultJobRunner(int corePoolSize) {
        this.executorService = Executors.newFixedThreadPool(corePoolSize);
        this.stateManager = new StateManager();
    }

    public DefaultJobRunner() {
        this(DEFAULT_CORE_POOL_SIZE);
    }


    @Override
    public void execute(Job job) {
        System.out.println("Start Running job  .....");
        job.getJobExecutionContext().setStartTime(new Date());
        this.stateManager.toNextState(job);
        CompletableFuture<JobContext> task = CompletableFuture.supplyAsync(job, this.executorService)
                .whenCompleteAsync((result, exception) -> {
                    if (exception != null) {
                        System.out.println("exception occurs" + exception);
                        result.setError(exception);
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
                    this.endSignal.countDown();
                    System.out.println("Finish Running job  .....");
                    System.out.println("endSignal count  ....." +  this.endSignal.getCount());
                    return result;
                });
      }

    @Override
    public void init(CountDownLatch endSignal) {
        System.out.println("endSignal count at init ....." +  endSignal.getCount());
        this.endSignal = endSignal;
    }

}
