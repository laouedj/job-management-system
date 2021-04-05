package org.prototype.study.job;

import java.util.Date;

public abstract class AbstractJob implements Job{


    protected JobContext jobContext;

    public AbstractJob(JobContext jobContext) {
        this.jobContext = jobContext;
    }


    @Override
    public final void execute() {

        try {
            this.jobContext.setStartTime(new Date());
            doExecute();
            this.jobContext.setStatus(JobState.SUCCESS);

        }catch(Throwable t) {
            this.jobContext.setStatus(JobState.FAILED);
        }
        finally {
            this.jobContext.setEndTime(new Date());
        }


    }

    @Override
    public JobContext getJobExecutionContext() {
        return this.jobContext;
    }

    abstract protected void doExecute();
}
