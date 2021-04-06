package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

import java.util.List;

public interface JobSubmitter extends Runnable {

    void submitOne(Job job) ;

    void submitMany(List<Job> jobs) ;

    public void waitToFinish() throws InterruptedException ;

}
