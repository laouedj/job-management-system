package org.prototype.study.job;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public interface Job extends Supplier<JobContext> {

    JobContext getJobExecutionContext();
}
