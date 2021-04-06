package org.prototype.study.job.state;

import org.prototype.study.job.Job;

public class SuccessStateUpdater extends AbstractStateUpdater {
    @Override
    void checkCurrentState(Job job) {
        if (job.getJobExecutionContext().getStatus() != JobState.SUCCESS) {
            throw new RuntimeException("This job has an unconsistence state" + job.getJobExecutionContext().getStatus());
        }
    }

    @Override
    void doUpdateState(Job job) {
        throw new RuntimeException("This job is in the its last state " + job.getJobExecutionContext().getStatus());
    }
}