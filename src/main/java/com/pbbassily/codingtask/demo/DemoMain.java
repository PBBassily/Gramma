package com.pbbassily.codingtask.demo;

import com.google.common.collect.ImmutableList;
import com.pbbassily.codingtask.grammaproject.job.Job;
import com.pbbassily.codingtask.grammaproject.scheduler.GrammaScheduler;
import com.pbbassily.codingtask.grammaproject.time.GrammaTime;
import com.pbbassily.codingtask.grammaproject.trigger.Trigger;
import com.pbbassily.codingtask.grammaproject.trigger.TriggerBaseTime;


public class DemoMain {

    public static void main (String[] args) {

        GrammaTime executionTime = GrammaTime
                .builder()
                .value(2)
                .unit(GrammaTime.GrammaTimeUnit.MINUTE)
                .build();

        GrammaScheduler scheduler =  new GrammaScheduler(GrammaScheduler.DEFAULT_FIXED_POOL_THREAD_NUMBER, executionTime);
        Job job1 = new DemoJob("Job1");

        GrammaTime freq = GrammaTime
                .builder()
                .value(10)
                .unit(GrammaTime.GrammaTimeUnit.SECOND)
                .build();

        TriggerBaseTime triggerBaseTime = TriggerBaseTime.now();

        Trigger trigger = Trigger
                .builder()
                .baseTime(triggerBaseTime)
                .frequency(freq)
                .jobsToBeExecuted(ImmutableList.of(job1))
                .build();


        Job job2 = new DemoJob("Job2");

        GrammaTime freq2 = GrammaTime
                .builder()
                .value(1)
                .unit(GrammaTime.GrammaTimeUnit.SECOND)
                .build();

        TriggerBaseTime triggerBaseTime2 = TriggerBaseTime
                .builder()
                .minute(1)
                .second(32)
                .build();

        Trigger trigger2 = Trigger
                .builder()
                .baseTime(triggerBaseTime2)
                .frequency(freq2)
                .jobsToBeExecuted(ImmutableList.of(job2))
                .build();

        scheduler.registerTrigger(trigger2);
        scheduler.registerTrigger(trigger);
        scheduler.start();
    }
}