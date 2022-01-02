package com.pbbassily.codingtask.grammaproject.trigger;

import com.pbbassily.codingtask.grammaproject.job.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Builder
@Getter
public class Trigger {

    private long baseTimestamp;
    private final long frequency;
    private final List<Job> jobsToBeExecuted;

    protected long getNextFireUpTimeStamp() {
        return baseTimestamp +  (frequency * 1000);
    }

    protected void updateBaseTime(long timeNow) {
        this.baseTimestamp = timeNow;
    }

    public static class TriggerBuilder {

        public TriggerBuilder frequency(long frequency) {

            if (frequency <= 0)
                throw new IllegalArgumentException("Frequency can not be less than or equal zero!");

            this.frequency = frequency;
            return this;
        }
    }
}
