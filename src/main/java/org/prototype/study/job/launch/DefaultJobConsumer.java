package org.prototype.study.job.launch;

import org.prototype.study.job.Job;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DefaultJobConsumer implements JobConsumer {

    private JobQueue jobQueue;
    private JobRunner jobRunner;
    private ExecutorService executorService;
    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
    private static final int DEFAULT_CORE_POOL_SIZE = 1;


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
        Properties appProps = new Properties();
        try {
            appProps.load(DefaultJobConsumer.class.getResourceAsStream("/configuration.properties"));
            if (appProps.getProperty("consumer.core.pool.size") != null) {
                corePoolSize = Integer.valueOf(appProps.getProperty("consumer.core.pool.size"));
            }
            this.executorService = Executors.newFixedThreadPool(corePoolSize);

            System.out.println("Consumer Started with Core Pool Size = " + corePoolSize );

            executorService.execute(this);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void consume(Job job) {
        if (job != null) {
            System.out.println("Job taken ....." + job.getJobExecutionContext().getJobInputParameters());
            jobRunner.execute(job);
        } else {
            System.out.println("No job to execute  ! .....");
        }
    }

    @Override
    public void shutdown() {
        doShutdownConsumer();
        this.jobRunner.shutdown();
    }


    private void doShutdownConsumer() {
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
        }else {
            System.out.println("can't shutdwon Consumer Executor service.....");
        }

        end.countDown();
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
