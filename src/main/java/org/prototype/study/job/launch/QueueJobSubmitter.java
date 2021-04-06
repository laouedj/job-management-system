package org.prototype.study.job.launch;

import org.prototype.study.job.Job;
import org.prototype.study.job.state.JobState;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

public class QueueJobSubmitter implements JobSubmitter {

    private final BlockingQueue<Job> jobQueue;
    private final JobRunner jobRunner;
    private CountDownLatch startSignal = new CountDownLatch(1);
    private CountDownLatch endSignal;

    public QueueJobSubmitter(BlockingQueue<Job> jobQueue, JobRunner jobRunner) {
        this.jobQueue = jobQueue;
        this.jobRunner = jobRunner;
    }

    public QueueJobSubmitter() {
        this(new LinkedBlockingDeque(),new DefaultJobRunner());
    }




    private void doRun() {
        while (!terminate()) {
            try {
                Job jobToExecute = jobQueue.take();
                System.out.println("Job taken .....");
                jobRunner.execute(jobToExecute);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

    }


    @Override
    public void run() {
        try {
            if (canStart()) {
                doRun();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean canStart() throws InterruptedException {
        this.startSignal.await();
        return true;
    }

    @Override
    public void submitOne(Job job) {
            endSignal = new CountDownLatch(1);
            doSubmit(job);
            start();
    }

    private void doSubmit(Job job)  {
        try {
            System.out.println(" Submitting Job .....");
            job.getJobExecutionContext().setStatus(JobState.QUEUED);
            jobQueue.put(job);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void submitMany(List<Job> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return;
        }
        endSignal = new CountDownLatch(jobs.size());
        for (Job job: jobs)
        {
            doSubmit(job);
        }

        start();
    }

    @Override
    public void waitToFinish() throws InterruptedException {
        this.endSignal.await();
    }

    private void start() {
        this.jobRunner.init(this.endSignal);
        this.startSignal.countDown();
    }

    public CountDownLatch getEndSignal() {
        return endSignal;
    }

    private boolean terminate() {
        return (this.endSignal.getCount() == 0);
    }

    public void setEndSignal(CountDownLatch endSignal) {
        this.endSignal = endSignal;
    }
}
