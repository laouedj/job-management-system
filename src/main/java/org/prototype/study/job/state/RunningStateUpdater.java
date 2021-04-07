package org.prototype.study.job.state;

import org.prototype.study.job.Job;

public class RunningStateUpdater extends AbstractStateUpdater {

    @Override
    void checkCurrentState(Job job) {
        if (job.getJobExecutionContext().getStatus() != JobState.RUNNING) {
            throw new RuntimeException("This job has an unconsistence state" + job.getJobExecutionContext().getStatus());
        }
    }

    @Override
    void doUpdateState(Job job) {

        if (job.getJobExecutionContext().getError() != null) {
            System.out.println("Job passe from RUNNING to FAILED.....");
            job.getJobExecutionContext().setStatus(JobState.FAILED);
        } else {
            System.out.println("Job passe from RUNNING to SUCCESS.....");
            job.getJobExecutionContext().setStatus(JobState.SUCCESS);
        }
    }
}
