package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

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
