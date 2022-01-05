package com.pbbassily.codingtask.grammaproject.trigger;

import com.pbbassily.codingtask.grammaproject.job.Job;
import com.pbbassily.codingtask.grammaproject.time.GrammaTime;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Builder
@Getter
public class Trigger {

    private TriggerBaseTime baseTime;
    private final GrammaTime frequency;
    private final List<Job> jobsToBeExecuted;
    private final String name;

    protected long getNextFireUpTimeStamp() {
        long factor = TimeUnit.SECONDS.toMillis(1);
        return ((baseTime.getTimestampInMillis() / factor) * factor) + frequency.getValueInMillis();
    }

    protected void updateBaseTime(long timeNow) {
        this.baseTime = new TriggerBaseTime(timeNow);
    }
}
