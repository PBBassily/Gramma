package com.pbbassily.codingtask.grammaproject.scheduler;

import com.pbbassily.codingtask.grammaproject.time.GrammaTime;
import com.pbbassily.codingtask.grammaproject.time.IntervalGuardian;
import com.pbbassily.codingtask.grammaproject.trigger.TriggersTracker;
import lombok.Getter;
import lombok.NonNull;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class GrammaSchedulerContext {

    public static final int DEFAULT_FIXED_POOL_THREAD_NUMBER = 10;

    private static final GrammaTime EPOC_TIME = GrammaTime
            .builder()
            .value(1)
            .unit(GrammaTime.GrammaTimeUnit.SECOND)
            .build();

    @NonNull private final TriggersTracker triggersTracker;
    @NonNull private final Timer timer;
    @NonNull private final IntervalGuardian intervalGuardian;
    @NonNull private final GrammaTime epocTime;
    @NonNull private final ExecutorService executorService;

    protected GrammaSchedulerContext(TriggersTracker triggersTracker,
                                     Timer timer,
                                     IntervalGuardian intervalGuardian,
                                     GrammaTime epocTime,
                                     int nThreads) {
        this.triggersTracker = triggersTracker;
        this.timer = timer;
        this.intervalGuardian =  intervalGuardian;
        this.executorService = Executors.newFixedThreadPool(nThreads);
        this.epocTime = epocTime;
    }

    public static GrammaSchedulerContext getDefaultInstance(GrammaTime executionTime) {
         new IntervalGuardian(executionTime);
        return getDefaultInstance(executionTime, DEFAULT_FIXED_POOL_THREAD_NUMBER);
    }

    public static GrammaSchedulerContext getDefaultInstance(GrammaTime executionTime, int nThreads) {
        new IntervalGuardian(executionTime);
        return new GrammaSchedulerContext(new TriggersTracker(), new Timer(),
                new IntervalGuardian(executionTime), EPOC_TIME, nThreads);
    }
}
