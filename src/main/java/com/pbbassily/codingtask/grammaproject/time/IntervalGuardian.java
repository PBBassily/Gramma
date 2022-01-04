package com.pbbassily.codingtask.grammaproject.time;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IntervalGuardian {

    @NonNull private final GrammaTime interval;
    private Long initialTimestamp;

    public void setInitialTimestamp(long initialTimestamp) {
        this.initialTimestamp = initialTimestamp;
    }

    public boolean isIntervalElapsed(long currentTime) {
        return  (currentTime - initialTimestamp) >= interval.getValueInMillis();
    }
}
