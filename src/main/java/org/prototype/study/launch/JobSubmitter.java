package org.prototype.study.launch;

import org.prototype.study.job.Job;

public interface JobSubmitter extends Runnable {

    void submit(Job job) ;
}
