package org.prototype.study.job.launch;

import org.prototype.study.ConfigurationManager;
import org.prototype.study.job.Job;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DefaultJobConsumer implements JobConsumer {

    private static final int DEFAULT_CORE_POOL_SIZE = 1;
    private JobQueue jobQueue;
    private JobRunner jobRunner;
    private ExecutorService executorService;
    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
    private CountDownLatch end;

    public DefaultJobConsumer(JobQueue jobQueue) {
        this.jobQueue = jobQueue;
        jobRunner = new GlobalJobRunner();
    }

    @Override
    public void start() {

        doStartConsumer();

        this.jobRunner.start();

    }

    private void doStartConsumer() {

        this.end = new CountDownLatch(1);
        loadConfiguration();
        startExecutor();

    }

    private void startExecutor() {
        this.executorService = Executors.newFixedThreadPool(corePoolSize);
        System.out.println("Consumer Started with Core Pool Size = " + corePoolSize);
        executorService.execute(this);
    }

    private void loadConfiguration() {
        if (ConfigurationManager.getPropertyValue("consumer.core.pool.size") != null) {
            corePoolSize = Integer.valueOf(ConfigurationManager.getPropertyValue("consumer.core.pool.size"));
        }
    }

    @Override
    public void consume(Job job) {
        if (job != null) {
            System.out.println("Job taken ....." + job.getJobExecutionContext().getJobInputParameters());
            System.out.println("Launching Job with priority " + job.getJobExecutionContext().getPriority());
            jobRunner.execute(job);
        } else {
            System.out.println("No job to execute  ! .....");
        }
    }

    @Override
    public void shutdown() {
        end.countDown();
        shutdownExecutor();
        this.jobRunner.shutdown();
    }


    private void shutdownExecutor() {
        System.out.println("Shutdown Consumer Executor service .....");
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(900, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        if (executorService.isShutdown()) {
            System.out.println("Consumer Executor service is down.....");
        } else {
            System.out.println("can't shutdwon Consumer Executor service.....");
        }
    }

    @Override
    public void run() {
        while (canRun()) {
            try {
                System.out.println("Waiting for jobs .....");
                Job job = jobQueue.take();
                consume(job);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    private boolean canRun() {
        return (this.end != null) && (this.end.getCount() != 0);
    }


}
