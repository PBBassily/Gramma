package com.pbbassily.codingtask.grammaproject.scheduler;

import com.pbbassily.codingtask.grammaproject.job.Job;
import com.pbbassily.codingtask.grammaproject.job.JobContext;
import com.pbbassily.codingtask.grammaproject.trigger.Trigger;
import com.pbbassily.codingtask.grammaproject.trigger.TriggersTracker;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class GrammaScheduler {

    @NonNull private final TriggersTracker triggersTracker;
    @NonNull private final Timer timer;
    @NonNull private final ExecutorService executor;
    private final int MINIMUM_INTERVAL_IN_SEC = 1;

    public GrammaScheduler(ExecutorService executor) {
        this.timer = new Timer();
        this.triggersTracker = new TriggersTracker();
        this.executor = executor;
    }

    public void registerTrigger(Trigger trigger) {
        triggersTracker.addTrigger(trigger);
    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("From sched timerTask => Thread:" + Thread.currentThread().getId());
                long parsedTime = parseTimeNow();
                Optional<List<Job>> jobs = triggersTracker.getFiredTriggers(parsedTime);
                jobs.ifPresent(presentJobs -> presentJobs.forEach(job -> promoteJobToExecution(job)));
            }
        }, 0, TimeUnit.SECONDS.toMillis(MINIMUM_INTERVAL_IN_SEC));
    }

    public void stop() {
        this.timer.cancel();
        this.executor.shutdown();
    }

    private long parseTimeNow() {
        long factor = TimeUnit.SECONDS.toMillis(1) ;
        return (System.currentTimeMillis() / factor) * factor   ;
    }

    private void promoteJobToExecution(Job job) {
        executor.submit(() -> {
            job.execute(new JobContext("Thread:" + Thread.currentThread().getId()));
        });
    }
}
