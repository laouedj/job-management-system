package org.prototype.study.job.state;

import org.prototype.study.job.Job;

public class QueuedStateUpdater extends AbstractStateUpdater {

    @Override
    void checkCurrentState(Job job) {
        if (job.getJobExecutionContext().getStatus() != JobState.QUEUED) {
            throw new RuntimeException("This job has an unconsistence state" + job.getJobExecutionContext().getStatus());
        }
    }

    @Override
    void doUpdateState(Job job) {
        job.getJobExecutionContext().setStatus(JobState.RUNNING);
    }
}
