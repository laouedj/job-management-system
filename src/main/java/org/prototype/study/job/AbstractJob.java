package org.prototype.study.job;

import org.prototype.study.job.parameters.JobInputParameters;

public abstract class AbstractJob implements Job{


    protected JobContext jobContext;

    public AbstractJob(JobContext jobContext) {
        this.jobContext = jobContext;
    }

    public AbstractJob(JobInputParameters jobInputParameters) {
        this(new JobContext(jobInputParameters));
    }

    @Override
    public JobContext getJobExecutionContext() {
        return this.jobContext;
    }


    abstract protected void doExecute();

    @Override
    public void execute(){
        validateParameters(this.jobContext.getJobInputParameters());
        doExecute();
    }

    @Override
    public void run() {
        execute();
    }

    private void validateParameters(JobInputParameters jobInputParameters) {
        //TODO  some stuff to validate parameters
    }

    @Override
    public int compareTo(Job job) {
        return this.getJobExecutionContext().getPriority().compareTo(job.getJobExecutionContext().getPriority());
    }

    public static class JobBuilder {

        protected JobContext jobContext;

        JobBuilder buildContext(JobContext jobContext) {
            this.jobContext = jobContext;
            return this;
        }

        //JobBuilder buildParameters()



    }
}
