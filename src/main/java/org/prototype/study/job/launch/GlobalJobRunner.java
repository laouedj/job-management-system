package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.launch.scheduling.ExecutionMode;
import org.prototype.study.job.launch.scheduling.ScheduledJobRunner;
import org.prototype.study.job.parameters.JobInputParameters;

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

        JobInputParameters inputDataList = job.getJobExecutionContext().getJobInputParameters();
        if (inputDataList.getJobInputParameter("schedule.date") != null) {
            System.out.println("Job is scheduled on " + inputDataList.getJobInputParameter("schedule.date"));
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
