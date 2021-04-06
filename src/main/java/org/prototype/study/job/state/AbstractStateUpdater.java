package org.prototype.study.job.state;

import org.prototype.study.job.Job;
import org.prototype.study.job.state.StateUpdater;

public abstract class AbstractStateUpdater implements StateUpdater {

    @Override
     public void toNextState(Job job) {
        checkCurrentState(job);
        doUpdateState(job);
    }

    abstract void checkCurrentState(Job job);

    abstract void doUpdateState(Job job);
}
