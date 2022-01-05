package com.pbbassily.codingtask.demo;

import com.google.common.collect.ImmutableList;
import com.pbbassily.codingtask.grammaproject.job.Job;
import com.pbbassily.codingtask.grammaproject.scheduler.GrammaScheduler;
import com.pbbassily.codingtask.grammaproject.scheduler.GrammaSchedulerContext;
import com.pbbassily.codingtask.grammaproject.time.GrammaTime;
import com.pbbassily.codingtask.grammaproject.trigger.Trigger;
import com.pbbassily.codingtask.grammaproject.trigger.TriggerBaseTime;

/**
 * Here is a sample usage of Gramma
 */
public class DemoMain {

    public static void main (String[] args) {


        // define the execution interval to be e.g. 2 minutes
        GrammaTime executionTime = GrammaTime
                .builder()
                .value(2)
                .unit(GrammaTime.GrammaTimeUnit.MINUTE)
                .build();

        // initialise the scheduler with the predefined execution time.
        GrammaScheduler scheduler =  new GrammaScheduler(GrammaSchedulerContext.getDefaultInstance(executionTime));

        // create an instance of your concrete job class
        Job job1 = new DemoJob("Job1");

        // define the frequency
        GrammaTime freq = GrammaTime
                .builder()
                .value(10)
                .unit(GrammaTime.GrammaTimeUnit.SECOND)
                .build();

        // define base time i.e constant time to defined ahead of the first execution of your job
        // example: after 1 hr (base time), call my job every minute (freq)
        TriggerBaseTime triggerBaseTime = TriggerBaseTime.now();

        // define the trigger which will relate freq, basetime and job(s) to be executed.
        // PS: trigger can have many jobs, but a job has one trigger

        Trigger trigger = Trigger
                .builder()
                .baseTime(triggerBaseTime)
                .frequency(freq)
                .name("Trigger1")
                .jobsToBeExecuted(ImmutableList.of(job1))
                .build();


        // you can create another job with different trigger and both jobs/triggers will
        // be processed concurrently.

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
                .name("Trigger2")
                .jobsToBeExecuted(ImmutableList.of(job2))
                .build();

        // register triggers
        scheduler.registerTrigger(trigger);
        scheduler.registerTrigger(trigger2);

        // kick off scheduler!
        scheduler.start();
    }
}