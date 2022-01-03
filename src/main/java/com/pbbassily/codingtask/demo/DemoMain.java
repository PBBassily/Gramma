package com.pbbassily.codingtask.demo;

import com.google.common.collect.ImmutableList;
import com.pbbassily.codingtask.grammaproject.scheduler.GrammaScheduler;
import com.pbbassily.codingtask.grammaproject.trigger.Trigger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoMain {

    public static void main (String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        GrammaScheduler scheduler =  new GrammaScheduler(executorService);
        DemoJob job = new DemoJob();
        Trigger trigger = Trigger
                .builder()
                .baseTimestamp(System.currentTimeMillis())
                .frequency(10)
                .jobsToBeExecuted(ImmutableList.of(job))
                .build();
        scheduler.registerTrigger(trigger);
        scheduler.start();
        System.out.println("Thread:" + Thread.currentThread().getName());
        Thread.sleep(22000);
        scheduler.stop();
    }
}