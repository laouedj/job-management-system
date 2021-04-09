package org.prototype.study.job.launch;

import org.prototype.study.ConfigurationManager;
import org.prototype.study.job.Job;
import org.prototype.study.job.state.StateManager;
import org.prototype.study.job.state.StateUpdater;

import java.time.LocalDateTime;
import java.util.concurrent.*;

public abstract class AbstractJobRunner implements JobRunner {

    private static final int DEFAULT_CORE_POOL_SIZE = 1;


    protected StateUpdater stateManager;
    protected ExecutorService executorService;
    protected boolean started = false;
    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;

    @Override
    public void start() {

            this.stateManager = new StateManager();
        if (ConfigurationManager.getPropertyValue("runner.core.pool.size") != null) {
            corePoolSize = Integer.valueOf(ConfigurationManager.getPropertyValue("runner.core.pool.size"));
            }

            this.executorService = Executors.newFixedThreadPool(this.corePoolSize);
            if (this.executorService.isShutdown()) {
                System.out.println("The executor is not started ....");
                throw new RuntimeException("The executor is shutdown ....");
            }
            System.out.println("Runner " + this.getClass().getName() + " Started with Core Pool Size = " + this.corePoolSize);

            started = true;

    }

    @Override
    public void shutdown() {
        System.out.println("Shutdown Runner ExecutorService ....");
        this.executorService.shutdown();
        try {
            if (!this.executorService.awaitTermination(900, TimeUnit.MILLISECONDS)) {
                this.executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.executorService.shutdownNow();
        }
        this.started = false;

        if (this.executorService.isShutdown()) {
            System.out.println(" Runner ExecutorService is down....");
        } else {
            System.out.println(" Can't Shutdown Runner ExecutorService....");
        }

    }

    @Override
    public void execute(Job job) {

        if (!isStarted()) {
            System.out.println("Job Runner not started .....");
            throw new RuntimeException("Job Runner not started .....");

        }
        doExecute(job, getExecutor(job, this.executorService));
    }


    protected boolean isStarted() {
        return this.started;
    }


    protected void doExecute(Job job, Executor executorService) {

        CompletableFuture<Void> task = CompletableFuture
                .runAsync(() -> {
                    System.out.println("Start Running job  .....");
                    job.getJobExecutionContext().setStartTime(LocalDateTime.now());
                    this.stateManager.toNextState(job);
                }, executorService)
                .thenRun(job)
                .exceptionally(throwable ->
                {
                    job.getJobExecutionContext().setError(throwable);
                    System.out.println("exception occurs " + throwable);
                    throwable.printStackTrace();
                    return null;
                })
                .thenRun(() -> {
                    job.getJobExecutionContext().setEndTime(LocalDateTime.now());
                    this.stateManager.toNextState(job);
                    job.getJobExecutionContext().getDone().countDown();
                    System.out.println("Finish Running job  .....");
                });
    }


    protected abstract Executor getExecutor(Job job, ExecutorService executor);

}
