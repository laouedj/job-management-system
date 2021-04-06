package org.prototype.study.job.state;

import org.prototype.study.job.Job;

public interface StateUpdater {

    void toNextState(Job job);
}
