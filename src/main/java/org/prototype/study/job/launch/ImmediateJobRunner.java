package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.JobContext;
import org.prototype.study.job.state.StateManager;
import org.prototype.study.job.state.StateUpdater;

import java.util.Date;
import java.util.concurrent.*;

public class ImmediateJobRunner extends AbstractJobRunner {

    private static final int DEFAULT_CORE_POOL_SIZE = 3;

    private ExecutorService executorService;


    public ImmediateJobRunner( StateUpdater stateManager, ExecutorService executorService) {
        super(stateManager);
        this.executorService = executorService;

    }


    public ImmediateJobRunner() {
        this(new StateManager(),Executors.newFixedThreadPool(DEFAULT_CORE_POOL_SIZE));
    }



    @Override
    public void shutdown() {
        System.out.println("Shutdown ImmediateRunner ....");
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
    protected Executor getExecutor(Job job) {
        if (this.executorService.isShutdown()) {
            System.out.println("The executor is shutdown ....");
            throw new RuntimeException("The executor is shutdown ....");
        }
        return this.executorService;
    }

}
