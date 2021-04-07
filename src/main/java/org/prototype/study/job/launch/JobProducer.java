package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

import java.util.List;

public interface JobProducer {

    void produceOne(Job job) ;

    void produceMany(List<Job> jobs) ;

}
