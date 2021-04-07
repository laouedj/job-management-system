package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.state.StateManager;
import org.prototype.study.job.state.StateUpdater;

import java.util.concurrent.*;

public class ScheduledJobRunner implements  JobRunner{

    private static final int DEFAULT_CORE_POOL_SIZE = 3;

    private ExecutorService executorService;
    private StateUpdater stateManager;
    private boolean started = false;

    public ScheduledJobRunner(StateUpdater stateManager, ExecutorService executorService) {
        this.executorService = executorService;
        this.stateManager = stateManager;
    }

    public ScheduledJobRunner() {
        this(new StateManager(), Executors.newScheduledThreadPool(DEFAULT_CORE_POOL_SIZE));
    }

    @Override
    public void start() {

    }

    @Override
    public void execute(Job job) {

        //Executor afterTenSecs = CompletableFuture.delayedExecutor(10L, TimeUnit.SECONDS);
        //CompletableFuture<String> future
        //        = CompletableFuture.supplyAsync(() -> "someValue", afterTenSecs);


    }

    @Override
    public void shutdown() {

    }
}
