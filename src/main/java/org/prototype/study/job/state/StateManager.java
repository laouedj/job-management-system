package org.prototype.study.job.state;

import org.prototype.study.job.Job;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StateManager implements StateUpdater {


    private static final Map<JobState, StateUpdater> compositeStateUpdater;
    static {
        Map<JobState, StateUpdater> aMap = new HashMap<>();
        aMap.put(JobState.QUEUED, new QueuedStateUpdater());
        aMap.put(JobState.RUNNING, new RunningStateUpdater());
        aMap.put(JobState.SUCCESS, new SuccessStateUpdater());
        aMap.put(JobState.FAILED, new FailedStateUpdater());
        compositeStateUpdater = Collections.unmodifiableMap(aMap);
    }

    @Override
    public void toNextState(Job job) {
        JobState actualState = job.getJobExecutionContext().getStatus();
        if (actualState == null) {
            throw new RuntimeException("This job has an unconsistence state" + job.getJobExecutionContext().getStatus());
        }else {
            compositeStateUpdater.get(job.getJobExecutionContext().getStatus()).toNextState(job);
        }
    }


}
