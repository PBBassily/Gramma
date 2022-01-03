package com.pbbassily.codingtask.demo;

import com.google.common.collect.ImmutableList;
import com.pbbassily.codingtask.grammaproject.scheduler.GrammaScheduler;
import com.pbbassily.codingtask.grammaproject.trigger.Trigger;
import com.pbbassily.codingtask.grammaproject.trigger.TriggerBaseTime;
import com.pbbassily.codingtask.grammaproject.trigger.TriggerFrequency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoMain {

    public static void main (String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        GrammaScheduler scheduler =  new GrammaScheduler(executorService);
        DemoJob job = new DemoJob();

        TriggerFrequency triggerFrequency = TriggerFrequency
                .builder()
                .value(1)
                .unit(TriggerFrequency.TriggerFrequencyUnit.SECOND)
                .build();

        TriggerBaseTime triggerBaseTime = TriggerBaseTime.now();

        Trigger trigger = Trigger
                .builder()
                .baseTime(triggerBaseTime)
                .frequency(triggerFrequency)
                .jobsToBeExecuted(ImmutableList.of(job))
                .build();

        scheduler.registerTrigger(trigger);
        scheduler.start();
        Thread.sleep(22000);
        scheduler.stop();
    }
}