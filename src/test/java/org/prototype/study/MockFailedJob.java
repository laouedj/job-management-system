package org.prototype.study;

import org.prototype.study.job.AbstractJob;
import org.prototype.study.job.JobContext;
import org.prototype.study.job.parameters.JobInputParameters;

public class MockFailedJob extends AbstractJob {

    public MockFailedJob(JobContext jobContext) {
        super(jobContext);
    }

    public MockFailedJob(JobInputParameters jobInputParameters) {
        super(jobInputParameters);
    }

    @Override
    protected void doExecute() {

        System.out.println("I Am a dummy job " + this.getJobExecutionContext().getJobInputParameters().toString());
        throw new RuntimeException("This is a Fake Failed Job");
    }
}
