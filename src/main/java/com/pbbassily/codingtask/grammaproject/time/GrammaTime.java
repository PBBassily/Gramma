package com.pbbassily.codingtask.grammaproject.time;

import lombok.Builder;

@Builder
public class GrammaTime {

    private final int value;
    private final GrammaTimeUnit unit;


    public long getValueInMillis() {
        switch (unit) {
            case WEEK:
                return value * 7 * 24 * 60 * 60 * 1000;
            case DAY:
                return value * 24 * 60 * 60 * 1000;
            case HOUR:
                return value * 60 * 60 * 1000;
            case MINUTE:
                return value * 60 * 1000;
            default:
                return value * 1000;
        }
    }

     public enum GrammaTimeUnit {
        SECOND,
        MINUTE,
        HOUR,
        DAY,
        WEEK,
    }

    public static class GrammaTimeBuilder {

        public GrammaTime.GrammaTimeBuilder value(int value) {

            if (value <= 0)
                throw new IllegalArgumentException("Frequency can not be less than or equal zero!");

            this.value = value;
            return this;
        }
    }
}
