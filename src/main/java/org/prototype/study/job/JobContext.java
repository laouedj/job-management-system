package org.prototype.study.job;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class JobContext {

    private final JobInputDataList jobInputDataList;

    private JobState status;
    private JobPriority priority;
    private Date startTime;
    private Date endTime;
    private final CountDownLatch doneSignal = new CountDownLatch(1);


    public JobContext(JobInputDataList jobInputDataList) {
        this.jobInputDataList = jobInputDataList;
    }

    public JobState getStatus() {
        return status;
    }

    public void setStatus(JobState status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public JobPriority getPriority() {
        return priority;
    }

    public void setPriority(JobPriority priority) {
        this.priority = priority;
    }

    public CountDownLatch getDoneSignal() {
        return doneSignal;
    }
}
