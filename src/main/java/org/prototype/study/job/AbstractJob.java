package org.prototype.study.job;

import java.util.Date;

public abstract class AbstractJob implements Job{


    protected JobContext jobContext;

    public AbstractJob(JobContext jobContext) {
        this.jobContext = jobContext;
    }

    @Override
    public JobContext getJobExecutionContext() {
        return this.jobContext;
    }


    abstract protected void doExecute();

    @Override
    public JobContext get(){

        doExecute();
        return this.jobContext;

    }
}
