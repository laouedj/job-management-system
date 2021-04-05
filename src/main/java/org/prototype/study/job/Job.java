package org.prototype.study.job;

public interface Job {

    void execute();

    JobContext getJobExecutionContext();
}
