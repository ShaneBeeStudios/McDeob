package com.shanebeestudios.mcdeop.util;

import java.time.Duration;
import java.util.function.Consumer;

public class DurationTracker implements AutoCloseable {
    private final Consumer<String> consumer;
    private final long start = System.currentTimeMillis();
    private long end = 0;

    public DurationTracker(final Consumer<String> formatedDurationConsumer) {
        this.consumer = formatedDurationConsumer;
    }

    private void stop() {
        this.end = System.currentTimeMillis();
    }

    private String getFormattedDuration() {
        final Duration duration = Duration.ofMillis(this.end - this.start);
        final long minutes = duration.toMinutesPart();
        final long seconds = duration.toSecondsPart();
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

    @Override
    public void close() {
        this.stop();
        this.consumer.accept(this.getFormattedDuration());
    }
}
