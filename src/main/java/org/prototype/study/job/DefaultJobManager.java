package org.prototype.study.job;

import org.prototype.study.job.launch.*;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DefaultJobManager implements JobManager{

    private ExecutorService executorService;
    private static final int DEFAULT_CORE_POOL_SIZE = 1;
    private JobProducer jobProducer;
    private JobConsumer jobConsumer;
    private  JobQueue jobQueue;
    private boolean started = false;


    public DefaultJobManager() {
        this.executorService = Executors.newFixedThreadPool(DEFAULT_CORE_POOL_SIZE);
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
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(900, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    @Override
    public void start() {
        this.jobConsumer.start();
        executorService.execute(this.jobConsumer);
        this.started = true;
    }

}
