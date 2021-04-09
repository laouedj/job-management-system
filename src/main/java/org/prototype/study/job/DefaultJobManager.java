package org.prototype.study.job;

import org.prototype.study.job.launch.*;
import org.prototype.study.job.samples.emailsending.SendingMailsJob;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DefaultJobManager implements JobManager{

    private ExecutorService executorService;
    private static final int DEFAULT_CORE_POOL_SIZE = 1;
    private JobProducer jobProducer;
    private JobConsumer jobConsumer;
    private  JobQueue jobQueue;
    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
    private boolean started = false;


    public DefaultJobManager() {

        this.jobQueue = new JobBlockingQueue(new PriorityBlockingQueue<Job>());
        this.jobProducer = new DefaultJobProducer(this.jobQueue);
        this.jobConsumer = new DefaultJobConsumer(this.jobQueue);
    }


    @Override
    public void launchOne(Job job) {
        if (!started) {
            System.out.println("Job Manager not started .....");
            throw new RuntimeException("Job Manager not started .....");
        }
        this.jobProducer.produceOne(job);
    }

    @Override
    public void launchMany(List<Job> jobs) {

        if (!started) {
            System.out.println("Job Manager not started .....");
            throw new RuntimeException("Job Manager not started .....");
        }

        if (jobs == null || jobs.isEmpty()) {
            return;
        }

        List<Job> sortedJobsByPriority = jobs.stream().sorted().collect(Collectors.toList());
        this.jobProducer.produceMany(sortedJobsByPriority);
    }

    @Override
    public void shutdown() {
        shutdownExecutorService(this.executorService);
        this.jobConsumer.shutdown();
        this.started = false;
    }

    private void shutdownExecutorService(ExecutorService executorService) {
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
    }

    @Override
    public void start() {
        Properties appProps = new Properties();
        try {

            appProps.load(DefaultJobManager.class.getResourceAsStream("/configuration.properties"));
            if (appProps.getProperty("consumer.core.pool.size") != null) {
                corePoolSize = Integer.valueOf(appProps.getProperty("consumer.core.pool.size"));
            }
            this.executorService = Executors.newFixedThreadPool(corePoolSize);

            System.out.println("Consumer Started with Core Pool Size = " + corePoolSize );

            this.jobConsumer.start();
            executorService.execute(this.jobConsumer);
            this.started = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
