package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

import java.util.concurrent.CountDownLatch;

public interface JobRunner  {

    void execute(Job job);

}
