package org.prototype.study.job.launch.scheduling;

import org.prototype.study.job.Job;
import org.prototype.study.job.launch.AbstractJobRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledJobRunner extends AbstractJobRunner {


    @Override
    protected Executor getExecutor(Job job, ExecutorService executorService) {

        System.out.println(" Start Scheduler Executor ....");
        long delay = getExecutionDelay(job);
        Executor scheduledExecutor = CompletableFuture.delayedExecutor(delay, TimeUnit.SECONDS, executorService);
        return scheduledExecutor;
    }

    private long getExecutionDelay(Job job) {
        //Should handle Time Zone
        LocalDateTime scheduleTime = (LocalDateTime) job.getJobExecutionContext().getJobInputParameters().getJobInputParameter("schedule.date");
        LocalDateTime currentTime = LocalDateTime.now();
        long delay = Duration.between(currentTime,scheduleTime).getSeconds();
        return delay;
    }
}
