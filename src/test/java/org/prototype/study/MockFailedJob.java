package org.prototype.study;

import org.prototype.study.job.AbstractJob;
import org.prototype.study.job.JobContext;
import org.prototype.study.job.JobInputDataList;

public class MockFailedJob extends AbstractJob {

    public MockFailedJob(JobContext jobContext) {
        super(jobContext);
    }

    public MockFailedJob(JobInputDataList jobInputDataList) {
        super(jobInputDataList);
    }

    @Override
    protected void doExecute() {
        throw new RuntimeException("This is a Fake Failed Job");
    }
}
