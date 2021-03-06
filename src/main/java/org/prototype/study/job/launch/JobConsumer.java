package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

public interface JobConsumer extends Runnable {

    void start();

    void consume(Job job);

    void shutdown();
}
