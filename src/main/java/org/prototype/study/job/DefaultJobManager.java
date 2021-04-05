package org.prototype.study.job;

public class DefaultJobManager implements JobManager{
    @Override
    public Job create(JobInputDataList jobInputDataList) {

        JobContext jobContext = new JobContext(jobInputDataList);
        return new DefaultJob(jobContext);
    }
}
