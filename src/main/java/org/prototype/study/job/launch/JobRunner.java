package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

public interface JobRunner  {

    void start();

    void execute(Job job);

    void shutdown();

}
