package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.JobContext;
import org.prototype.study.job.state.StateManager;
import org.prototype.study.job.state.StateUpdater;

import java.util.Date;
import java.util.concurrent.*;

public class ScheduledJobRunner extends  AbstractJobRunner{


  public ScheduledJobRunner(StateUpdater stateManager) {
        super(stateManager);
    }

    public ScheduledJobRunner() {
        super(new StateManager());
    }

    @Override
    public void shutdown() {
      System.out.println("Shutdown ScheduledJobRunner ....");
      this.started = false;
    }

    @Override
    protected Executor getExecutor(Job job) {

        Executor scheduledExecutor = CompletableFuture.delayedExecutor(10L, TimeUnit.SECONDS);
        return scheduledExecutor;
    }
}
