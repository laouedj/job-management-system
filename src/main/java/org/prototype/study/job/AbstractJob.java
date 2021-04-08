package org.prototype.study.job;

import org.prototype.study.job.parameters.JobInputDataList;

public abstract class AbstractJob implements Job{


    protected JobContext jobContext;

    public AbstractJob(JobContext jobContext) {
        this.jobContext = jobContext;
    }

    public AbstractJob(JobInputDataList jobInputDataList) {
        this(new JobContext(jobInputDataList));
    }


    @Override
    public JobContext getJobExecutionContext() {
        return this.jobContext;
    }


    abstract protected void doExecute();

    @Override
    public void execute(){
        validateParameters(this.jobContext.getJobInputDataList());
        doExecute();
    }

    @Override
    public void run() {
        execute();
    }

    private void validateParameters(JobInputDataList jobInputDataList) {
        //TODO  some stuff to validate parameters
    }

    @Override
    public int compareTo(Job job) {
        return this.getJobExecutionContext().getPriority().compareTo(job.getJobExecutionContext().getPriority());
    }
}
