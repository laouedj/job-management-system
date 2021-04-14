package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

public interface JobQueue {

    void put(Job job) throws InterruptedException;

    Job take() throws InterruptedException;


}
