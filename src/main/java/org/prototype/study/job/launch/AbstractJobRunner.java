package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.JobContext;
import org.prototype.study.job.state.StateUpdater;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class AbstractJobRunner implements JobRunner {

    protected StateUpdater stateManager;
    protected boolean started = false;

    public AbstractJobRunner(StateUpdater stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void execute(Job job) {

        canExecute();
        doExecute(job, getExecutor(job));
    }


    protected void canExecute() {
        if (!started) {
            System.out.println("Job Runner not started .....");
            throw new RuntimeException("Job Runner not started .....");
        }
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


    protected abstract Executor getExecutor(Job job);

}
