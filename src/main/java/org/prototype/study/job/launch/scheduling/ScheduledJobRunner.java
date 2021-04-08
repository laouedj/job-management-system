package org.prototype.study.job.launch.scheduling;

import org.prototype.study.job.Job;
import org.prototype.study.job.launch.AbstractJobRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class ScheduledJobRunner extends AbstractJobRunner {


    @Override
    protected Executor getExecutor(Job job, ExecutorService executorService) {

        long delay = getExecutionDelay(job);
        Executor scheduledExecutor = CompletableFuture.delayedExecutor(delay, TimeUnit.SECONDS, executorService);
        return scheduledExecutor;
    }

    private long getExecutionDelay(Job job) {
        //Should handle Time Zone
        LocalDateTime scheduleTime = (LocalDateTime) job.getJobExecutionContext().getJobInputDataList().getInputData("schedule.date");
        LocalDateTime currentTime = LocalDateTime.now();
        long delay = Duration.between(currentTime,scheduleTime).getSeconds();
        return delay;
    }
}
