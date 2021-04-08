package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.JobContext;
import org.prototype.study.job.state.StateManager;
import org.prototype.study.job.state.StateUpdater;

import java.util.Date;
import java.util.concurrent.*;

public class ImmediateJobRunner extends AbstractJobRunner {

    @Override
    protected Executor getExecutor(Job job, ExecutorService executorService) {
        if (executorService.isShutdown()) {
            System.out.println("The executor is shutdown ....");
            throw new RuntimeException("The executor is shutdown ....");
        }
        return this.executorService;
    }

}
