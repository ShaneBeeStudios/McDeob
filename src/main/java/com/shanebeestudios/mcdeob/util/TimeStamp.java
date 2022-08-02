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
        String m = minutes == 1 ? "minute" : "minutes";
        String s = seconds == 1 ? "second" : "seconds";
        return String.format("%s %s, %s %s", minutes, m, seconds, s);
    }

}
