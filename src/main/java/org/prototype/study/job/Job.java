package org.prototype.study.job;

public interface Job extends Runnable, Comparable<Job> {

    void execute();

    JobContext getJobExecutionContext();

}
