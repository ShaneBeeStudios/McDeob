package com.shanebeestudios.mcdeob.util;

import java.time.Duration;

public class TimeStamp {

    public static TimeStamp fromNow(long start) {
        return new TimeStamp(System.currentTimeMillis() - start);
    }

    private final Duration duration;

    public TimeStamp(long millis) {
        this.duration = Duration.ofMillis(millis);
    }

    @Override
    public String toString() {
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        final StringBuilder sb = new StringBuilder();
        if (minutes > 0) {
            sb.append(minutes);
            sb.append(minutes == 1 ? " minute, " : " minutes, ");
        }

        if (seconds > 0) {
            sb.append(seconds);
            sb.append(seconds == 1 ? " second" : " seconds");
        }

        if (minutes == 0 && seconds == 0) {
            sb.append("less than a second");
        }

        return sb.toString();
    }

}
