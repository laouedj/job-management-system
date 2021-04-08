package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.JobContext;
import org.prototype.study.job.state.StateManager;
import org.prototype.study.job.state.StateUpdater;

import java.util.Date;
import java.util.concurrent.*;

public class ScheduledJobRunner extends  AbstractJobRunner{


    @Override
    protected Executor getExecutor(Job job, ExecutorService executorService) {

        Executor scheduledExecutor = CompletableFuture.delayedExecutor(10L, TimeUnit.SECONDS, executorService);
        return scheduledExecutor;
    }
}
