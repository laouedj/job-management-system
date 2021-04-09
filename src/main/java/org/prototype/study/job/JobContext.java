package org.prototype.study.job;

import org.prototype.study.job.parameters.JobInputParameters;
import org.prototype.study.job.state.JobState;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

public class JobContext {

    private final JobInputParameters jobInputParameters;

    private JobState status;
    private JobPriority priority = JobPriority.LOW;  // Default Priority
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private CountDownLatch done;
    private Throwable error;


    public JobContext(JobInputParameters jobInputParameters) {

        this.jobInputParameters = jobInputParameters;
        if (jobInputParameters.getJobInputParameter("priority") != null) {
            String priority = (String) jobInputParameters.getJobInputParameter("priority");
            this.priority = JobPriority.valueOf(priority.toUpperCase());
        }
        done = new CountDownLatch(1);
    }

    public JobState getStatus() {
        return status;
    }

    public void setStatus(JobState status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public JobPriority getPriority() {
        return priority;
    }

    public void setPriority(JobPriority priority) {
        this.priority = priority;
    }

    public CountDownLatch getDone() {
        return done;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public JobInputParameters getJobInputParameters() {
        return jobInputParameters;
    }

    public void setDone(CountDownLatch done) {
        this.done = done;
    }
}
