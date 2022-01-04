package com.pbbassily.codingtask.grammaproject.scheduler;

import com.pbbassily.codingtask.grammaproject.job.Job;
import com.pbbassily.codingtask.grammaproject.job.JobContext;
import com.pbbassily.codingtask.grammaproject.job.JobExecutor;
import com.pbbassily.codingtask.grammaproject.time.GrammaTime;
import com.pbbassily.codingtask.grammaproject.time.IntervalGuardian;
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
    @NonNull private final IntervalGuardian intervalGuardian;
    @NonNull private final ExecutorService executor;
    @NonNull private final GrammaTime epocTime;

    public GrammaScheduler(GrammaSchedulerContext grammaSchedulerContext) {
        this.timer = grammaSchedulerContext.getTimer();
        this.triggersTracker = grammaSchedulerContext.getTriggersTracker();
        this.executor = grammaSchedulerContext.getExecutorService();
        this.intervalGuardian = grammaSchedulerContext.getIntervalGuardian();
        this.epocTime = grammaSchedulerContext.getEpocTime();
    }

    public void registerTrigger(Trigger trigger) {
        triggersTracker.addTrigger(trigger);
    }

    public void start() {
        intervalGuardian.setInitialTimestamp(System.currentTimeMillis());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long parsedTime = parseTimeNow();
                Optional<List<Job>> jobs = triggersTracker.getFiredTriggers(parsedTime);
                jobs.ifPresent(presentJobs -> presentJobs.forEach(job -> promoteJobToExecution(job)));
                checkClosure();
            }
        }, 0, epocTime.getValueInMillis());
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
        executor.submit(() -> {
            JobExecutor.execute(job, new JobContext("Thread:" + Thread.currentThread().getId()));
        });
    }

    private void checkClosure() {
        long currentTime = System.currentTimeMillis();
        if (intervalGuardian.isIntervalElapsed(currentTime)) {
            stop();
        }
    }
}
