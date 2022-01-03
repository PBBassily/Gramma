package com.pbbassily.codingtask.grammaproject.scheduler;

import com.pbbassily.codingtask.grammaproject.job.Job;
import com.pbbassily.codingtask.grammaproject.job.JobContext;
import com.pbbassily.codingtask.grammaproject.time.GrammaTime;
import com.pbbassily.codingtask.grammaproject.trigger.Trigger;
import com.pbbassily.codingtask.grammaproject.trigger.TriggersTracker;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GrammaScheduler {

    @NonNull private final TriggersTracker triggersTracker;
    @NonNull private final Timer timer;
    @NonNull private final GrammaTime time;
    @NonNull private final ExecutorService executor;

    private long initiatedTime;
    private final GrammaTime EPOC_TIME = GrammaTime
            .builder()
            .value(1)
            .unit(GrammaTime.GrammaTimeUnit.SECOND)
            .build();

    public static final int DEFAULT_FIXED_POOL_THREAD_NUMBER = 10;

    public GrammaScheduler(int numberOfThreads, GrammaTime time) {
        this.timer = new Timer();
        this.triggersTracker = new TriggersTracker();
        this.executor = Executors.newFixedThreadPool(numberOfThreads);
        this.time = time;
    }

    public void registerTrigger(Trigger trigger) {
        triggersTracker.addTrigger(trigger);
    }

    public void start() {
        this.initiatedTime = System.currentTimeMillis();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long parsedTime = parseTimeNow();
                Optional<List<Job>> jobs = triggersTracker.getFiredTriggers(parsedTime);
                jobs.ifPresent(presentJobs -> presentJobs.forEach(job -> promoteJobToExecution(job)));
                checkClosure();
            }
        }, 0, EPOC_TIME.getValueInMillis());
    }

    public void stop() {
        this.timer.cancel();
        this.executor.shutdown();
    }

    private long parseTimeNow() {
        long factor = TimeUnit.SECONDS.toMillis(1) ;
        return (System.currentTimeMillis() / factor) * factor;
    }

    private void promoteJobToExecution(Job job) {
        executor.submit(() -> job.execute(new JobContext("Thread:" + Thread.currentThread().getId())));
    }

    private void checkClosure() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - initiatedTime >= time.getValueInMillis()) {
            stop();
        }
    }
}
