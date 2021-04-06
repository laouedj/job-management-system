package org.prototype.study.job;

import org.prototype.study.job.state.JobState;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class JobContext {

    private final JobInputDataList jobInputDataList;

    private JobState status;
    private JobPriority priority;
    private Date startTime;
    private Date endTime;
    private CountDownLatch doneSignal;
    private Throwable error;


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

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public JobInputDataList getJobInputDataList() {
        return jobInputDataList;
    }

    public void setDoneSignal(CountDownLatch doneSignal) {
        this.doneSignal = doneSignal;
    }
}
