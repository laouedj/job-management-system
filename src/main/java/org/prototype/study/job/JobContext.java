package org.prototype.study.job;

import java.util.Date;

public class JobContext {

    private final JobInputDataList jobInputDataList;

    private JobState status;
    private Date startTime;
    private Date endTime;


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
}
