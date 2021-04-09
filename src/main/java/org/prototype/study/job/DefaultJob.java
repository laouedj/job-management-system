package org.prototype.study.job;

import org.prototype.study.job.parameters.JobInputParameters;

public class DefaultJob extends AbstractJob {

    public DefaultJob(JobContext jobContext) {
        super(jobContext);
    }

    public DefaultJob(JobInputParameters jobInputParameters) {
        super(jobInputParameters);
    }

    @Override
    protected void doExecute() {
        System.out.println("I Am a dummy job " + this.getJobExecutionContext().getJobInputParameters().toString());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
