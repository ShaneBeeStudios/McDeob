package com.shanebeestudios.mcdeob.util;

import java.time.Duration;

/**
 * Represents a span in time
 */
public class TimeSpan {

    /**
     * Start and create a new TimeSpan
     *
     * @return New TimeSpan
     */
    public static TimeSpan start() {
        return new TimeSpan();
    }

    private final long start;
    private Duration duration;

    private TimeSpan() {
        this.start = System.currentTimeMillis();
        this.duration = Duration.ZERO;
    }

    /**
     * Finish timing
     */
    public void finish() {
        this.duration = Duration.ofMillis(System.currentTimeMillis() - this.start);
    }

    @Override
    public String toString() {
        long minutes = this.duration.toMinutesPart();
        long seconds = this.duration.toSecondsPart();
        final StringBuilder sb = new StringBuilder();
        if (minutes > 0) {
            sb.append(minutes);
            sb.append(minutes == 1 ? " minute" : " minutes");
            if (seconds > 0) sb.append(", ");
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
