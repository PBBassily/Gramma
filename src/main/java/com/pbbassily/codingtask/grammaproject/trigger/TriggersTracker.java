package com.pbbassily.codingtask.grammaproject.trigger;

import com.google.common.annotations.VisibleForTesting;
import com.pbbassily.codingtask.grammaproject.job.Job;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class TriggersTracker {

    private final Map<Long, Set<Trigger>> nextEpocToWaitingTriggersMap = new HashMap<>();
    private final static Logger logger = LogManager.getLogger(TriggersTracker.class);

    public void addTrigger(Trigger trigger) {
        long theAwaitedTimeStamp = trigger.getNextFireUpTimeStamp();
        logger.debug("\n" + trigger.getName()+ " scheduled at: "+ theAwaitedTimeStamp);
        Set<Trigger> triggers;
        if (nextEpocToWaitingTriggersMap.containsKey(theAwaitedTimeStamp)) {
            triggers = nextEpocToWaitingTriggersMap.get(theAwaitedTimeStamp);
        } else {
            triggers = new HashSet<>();
        }
        nextEpocToWaitingTriggersMap.put(theAwaitedTimeStamp, triggers);
        triggers.add(trigger);
    }

    public Optional<List<Job>> getFiredTriggers(long timeNow) {
        if (nextEpocToWaitingTriggersMap.containsKey(timeNow)) {
            Set<Trigger> triggers = nextEpocToWaitingTriggersMap.get(timeNow);
            logger.debug("\n" + triggers.size()+ " Triggers fired!");
            triggers.forEach(trigger -> trigger.updateBaseTime(timeNow));
            updateNextEpocToWaitingTriggersMap(timeNow);
            return Optional.of(
                    triggers.stream()
                            .flatMap(trigger -> trigger.getJobsToBeExecuted().stream())
                            .collect(Collectors.toList())
            );
        }
        return Optional.empty();
    }

    @VisibleForTesting
    void updateNextEpocToWaitingTriggersMap(long timeNow) {
        Set<Trigger> triggers = nextEpocToWaitingTriggersMap.remove(timeNow);
        triggers.forEach(this::addTrigger);
    }

    @VisibleForTesting
    Map<Long, Set<Trigger>> getNextEpocToWaitingTriggersMap() {
        return this.nextEpocToWaitingTriggersMap;
    }

}
