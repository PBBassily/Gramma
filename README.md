# Gramma
### Brief Description
Gramma originally comes from *Grammatéas (γραμματέας)* which means *secretary* in Greek.

Gramma is be your next in-process scheduler, you can define as many as scheduled jobs to run periodically with predefined frequency and base-time.
these jobs will be executed each on a separate thread.

### Reasoning of the technical decisions
- bla bla
### Trade-offs
- bla bla

### Usage
- Define the execution interval to be e.g. 2 minutes
````
        GrammaTime executionTime = GrammaTime
                .builder()
                .value(2)
                .unit(GrammaTime.GrammaTimeUnit.MINUTE)
                .build();
````

- Initialise the scheduler with the predefined execution time.
````
        GrammaScheduler scheduler =  new GrammaScheduler(GrammaSchedulerContext.getDefaultInstance(executionTime));
````
- Create an instance of your concrete job class which implements `Job` interface
````
        Job job1 = new DemoJob("Job1");
````
- Define the frequency
````
        GrammaTime freq = GrammaTime
                .builder()
                .value(10)
                .unit(GrammaTime.GrammaTimeUnit.SECOND)
                .build();
````
- Define base time i.e constant time to defined ahead of the first execution of your job. Example: after 1 min 32 sec (base time), call my job every sec (freq)
````
        TriggerBaseTime triggerBaseTime = TriggerBaseTime
                        .builder()
                        .minute(1)
                        .second(32)
                        .build();
````
- Define the trigger which will relate freq, basetime and job(s) to be executed.
PS: trigger can have many jobs, but a job has one trigger
```` 
        Trigger trigger = Trigger
                .builder()
                .baseTime(triggerBaseTime)
                .frequency(freq)
                .name("Trigger1")
                .jobsToBeExecuted(ImmutableList.of(job1))
                .build();
````  
 - Register triggers
```` 
        scheduler.registerTrigger(trigger);
        scheduler.registerTrigger(trigge2);
````         
- kick off scheduler!
```` 
 scheduler.start();
```` 

### Future Work
- Remove triggers without stopping the schedulers.
- Enriching `JobContext.class` to give the client more info while executing the job.
- Unit test all the classes.
- Provide client control over whether the job is executed after the base-time or after the base-time + 1 frequency period.
- Support metrics to monitor the scheduler sanity at client-side. 