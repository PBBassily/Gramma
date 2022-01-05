# Gramma
### Brief Description
Gramma originally comes from *Grammatéas (γραμματέας)* which means *secretary* in Greek.

Gramma is be your next in-process scheduler, you can define as many as scheduled jobs to run periodically with predefined frequency and base-time.
these jobs will be executed each on a separate thread.

### Reasoning of the technical decisions
##### Smart Long Polling
- The solution of the scheduler based on a simple long polling technique which relies on check the clock each `cycleTime` and see whether a trigger should be fired or not.
Efficiently enough, the scheduler is using on the `TriggersTracker` so each non-fruitful cycle check (i.e not triggers are fired up) it should cost constant time O(1) the time of checking the Map of triggers.

##### TriggerTrackers
It supports the idea of Smart Long Polling, storing the triggers internally using Map. The key of such map is the next awaited timestamp for each trigger, thus the long poll process from the scheduler shouldn't take more than constant time.
In addition, the value of the map is a set of triggers, thus the searching for a specific trigger should be O(1) theoretically.
For better results, instead of directly searching for an entry by the exact machine time each cycle, the TriggersTracker uses less precise time by using machine time in seconds instead of milliseconds.    

##### Parallelism 
Since Jobs are atomic, having one thread may block the scheduler from executing other jobs if the current executed job is blocking the main thread. Thus Gramma is using fixed pool of threads to maintain concurrency and efficiency. The number of default threads is 10, but client can change it from the `GrammaSchedulerContext`. 
  
### Trade-offs
##### Maven vs Gradle
- Gradle takes less time in building.
- Gradle has stronger fine-grained controlled dependencies resolution.
- Gradle has good UX from Intellij, which had been used in the dev of this project.
 

### Usage

- First of all create your `AwesomeJob` implementing `Job` interface.
````
public class DemoJob extends Job {
      ...
}
````

- Implement `execute` method to do your define the functionality of your job.
````
 @Override
    public void execute(JobContext context) { 
     
       // TODO awesome stuff
    }
````

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
- Define base time i.e constant time to be defined ahead of the first execution of your job. Example: after 1 min 32 sec (base time), call my job every sec (freq)
````
        TriggerBaseTime triggerBaseTime = TriggerBaseTime
                        .builder()
                        .minute(1)
                        .second(32)
                        .build();
````
- Define the trigger which will relate freq, basetime and job(s) to be executed.
PS: trigger and job relations ship is many to many.
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


- you can deliberately stop the schedule.
```` 
 scheduler.stop();
```` 

### Future Work
- Remove triggers without stopping the schedulers.
- Enriching `JobContext.class` to give the client more info while executing the job.
- Unit test all the classes.
- Provide client control over whether the job is executed after the base-time or after the base-time + 1 frequency period.
- Support metrics to monitor the scheduler sanity at client-side. 
- Usage of Guice for better dependency injection & testability.
