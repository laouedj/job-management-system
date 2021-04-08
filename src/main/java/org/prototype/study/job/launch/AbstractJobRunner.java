package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.JobContext;
import org.prototype.study.job.state.StateManager;
import org.prototype.study.job.state.StateUpdater;

import java.util.Date;
import java.util.concurrent.*;

public abstract class AbstractJobRunner implements JobRunner {

    private static final int DEFAULT_CORE_POOL_SIZE = 3;


    protected StateUpdater stateManager;
    protected ExecutorService executorService;
    protected boolean started = false;

    @Override
    public void start() {
        this.stateManager = new StateManager();
        this.executorService = Executors.newFixedThreadPool(DEFAULT_CORE_POOL_SIZE);
        if (this.executorService.isShutdown()) {
            System.out.println("The executor is not started ....");
            throw new RuntimeException("The executor is shutdown ....");
        }
        started = true;
    }

    @Override
    public void shutdown() {
        System.out.println("Shutdown ExecutorService ....");
        this.executorService.shutdown();
        try {
            if (!this.executorService.awaitTermination(900, TimeUnit.MILLISECONDS)) {
                this.executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.executorService.shutdownNow();
        }
        this.started = false;

    }

    @Override
    public void execute(Job job) {

        if (!isStarted()) {
            System.out.println("Job Runner not started .....");
            throw new RuntimeException("Job Runner not started .....");

        }
        doExecute(job, getExecutor(job,this.executorService));
    }


    protected boolean isStarted() {
        return this.started;
    }


    protected void doExecute(Job job, Executor executorService) {
        System.out.println("Start Running job  .....");
        job.getJobExecutionContext().setStartTime(new Date());
        this.stateManager.toNextState(job);
        CompletableFuture<JobContext> task = CompletableFuture.supplyAsync(job, executorService)
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
                .thenApplyAsync(result ->
                {
                    result.setEndTime(new Date());
                    this.stateManager.toNextState(job);
                    result.getDone().countDown();
                    System.out.println("Finish Running job  .....");
                    return result;
                });
    }


    protected abstract Executor getExecutor(Job job, ExecutorService executor);

}
