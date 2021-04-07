package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.JobInputDataList;
import org.prototype.study.job.state.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GlobalJobRunner implements JobRunner {

    private static final Map<ExecutionMode, JobRunner> compositeJobRunner;
    static {
        Map<ExecutionMode, JobRunner> aMap = new HashMap<>();
        aMap.put(ExecutionMode.IMMEDIATE, new ImmediateJobRunner());
        aMap.put(ExecutionMode.SCHEDULED, new ScheduledJobRunner());
        compositeJobRunner = Collections.unmodifiableMap(aMap);
    }

    @Override
    public void start() {
        compositeJobRunner.get(ExecutionMode.SCHEDULED).start();
        compositeJobRunner.get(ExecutionMode.IMMEDIATE).start();
    }

    @Override
    public void execute(Job job) {

        JobInputDataList inputDataList = job.getJobExecutionContext().getJobInputDataList();
        if (inputDataList.getInputData("schedule.date") != null) {
            compositeJobRunner.get(ExecutionMode.SCHEDULED).execute(job);
        }else {
            compositeJobRunner.get(ExecutionMode.IMMEDIATE).execute(job);
        }

    }

    @Override
    public void shutdown() {

        compositeJobRunner.get(ExecutionMode.SCHEDULED).shutdown();
        compositeJobRunner.get(ExecutionMode.IMMEDIATE).shutdown();

    }
}
