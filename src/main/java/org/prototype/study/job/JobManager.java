package org.prototype.study.job;

import java.util.List;

public interface JobManager {

    void launchOne(Job job) ;

    void launchMany(List<Job> jobs) ;

    void shutdown();

    void start();

}
