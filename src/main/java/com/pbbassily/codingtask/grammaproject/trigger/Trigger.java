package com.pbbassily.codingtask.grammaproject.trigger;

import com.pbbassily.codingtask.grammaproject.job.Job;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Builder
@Getter
public class Trigger {

    private TriggerBaseTime baseTime;
    private final TriggerFrequency frequency;
    private final List<Job> jobsToBeExecuted;

    protected long getNextFireUpTimeStamp() {
        long factor = TimeUnit.SECONDS.toMillis(1);
        return ((baseTime.getTimestampInMillis() / factor) * factor) + frequency.getValueInMillis();
    }

    protected void updateBaseTime(long timeNow) {
        this.baseTime = new TriggerBaseTime(timeNow);
    }
}
