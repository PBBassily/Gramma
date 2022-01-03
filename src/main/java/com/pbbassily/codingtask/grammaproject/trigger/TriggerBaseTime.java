package com.pbbassily.codingtask.grammaproject.trigger;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TriggerBaseTime {

    @NonNull private Long timestamp;

    private int week;
    private int day;
    private int hour;
    private int minute;
    private int second;

    Long getTimestampInMillis() {
        return timestamp;
    }

    public static TriggerBaseTime now() {
        return new TriggerBaseTime(System.currentTimeMillis());
    }

    public static class TriggerBaseTimeBuilder {
        public TriggerBaseTime build() {
            long timestamp = System.currentTimeMillis()
                    + second * 1000
                    + minute * 60 * 1000
                    + hour * 60 * 60 * 1000
                    + day * 24 * 60 * 60 * 1000
                    + week * 7 * 24 * 60 * 60 * 1000;
            return new TriggerBaseTime(timestamp);
        }
    }
}
