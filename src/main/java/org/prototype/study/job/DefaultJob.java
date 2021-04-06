package org.prototype.study.job;

import java.util.Date;

public class DefaultJob extends AbstractJob {

    public DefaultJob(JobContext jobContext) {
        super(jobContext);
    }

    @Override
    protected void doExecute() {
        System.out.println("I Am a dummy job");
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
