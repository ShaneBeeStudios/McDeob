package com.shanebeestudios.mcdeop.util;

import java.text.ChoiceFormat;
import java.time.Duration;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.ObjLongConsumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DurationTracker implements AutoCloseable {
    private static final ChoiceFormat MINUTES_FORMAT =
            new ChoiceFormat(new double[] {1, 2}, new String[] {"minute", "minutes"});
    private static final ChoiceFormat SECONDS_FORMAT =
            new ChoiceFormat(new double[] {1, 2}, new String[] {"second", "seconds"});

    private final Consumer<String> consumer;
    private final long start = System.currentTimeMillis();
    private long end = 0;

    private void stop() {
        this.end = System.currentTimeMillis();
    }

    private String getFormattedDuration() {
        final Duration duration = Duration.ofMillis(this.end - this.start);
        final long minutes = duration.toMinutesPart();
        final long seconds = duration.toSecondsPart();

        if (minutes == 0 && seconds == 0) {
            return "less than a second";
        }

        final StringJoiner joiner = new StringJoiner(", ");
        final ObjLongConsumer<ChoiceFormat> timeConsumer = (format, time) -> {
            if (time > 0) {
                joiner.add(time + " " + format.format(time));
            }
        };

        timeConsumer.accept(MINUTES_FORMAT, minutes);
        timeConsumer.accept(SECONDS_FORMAT, seconds);

        return joiner.toString();
    }

    @Override
    public void close() {
        this.stop();
        this.consumer.accept(this.getFormattedDuration());
    }
}
