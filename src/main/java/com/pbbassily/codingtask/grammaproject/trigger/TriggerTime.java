package com.pbbassily.codingtask.grammaproject.trigger;


import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class TriggerTime {

    @NonNull protected Long timestamp;

    private int week;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public static TriggerTime now() {
        return new TriggerTime(System.currentTimeMillis());
    }


    public static class TriggerTimeBuilder {
        public TriggerTime build() {
            long timestamp = System.currentTimeMillis()
                    + second * 1000
                    + minute * 60 * 1000
                    + hour * 60 * 60 * 1000
                    + day * 24 * 60 * 60 * 1000
                    + week * 7 * 24 * 60 * 60 * 1000;
            return new TriggerTime(timestamp);
        }
    }
}
