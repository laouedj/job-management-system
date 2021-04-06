package org.prototype.study.job;

import org.prototype.study.launch.DefaultJobRunner;
import org.prototype.study.launch.JobRunner;

public class DefaultJobManager implements JobManager{

    private JobRunner jobRunner;

    public DefaultJobManager(JobRunner jobRunner) {
        this.jobRunner= jobRunner;
    }

    public DefaultJobManager() {
        this.jobRunner= new DefaultJobRunner();
    }


    private Job create(JobInputDataList jobInputDataList) {

        JobContext jobContext = new JobContext(jobInputDataList);
        return new DefaultJob(jobContext);
    }

    @Override
    public JobContext run(JobInputDataList jobInputDataList) {
        Job job = create(jobInputDataList);
        this.jobRunner.submitJob(job);
        return  job.getJobExecutionContext();
    }
}
