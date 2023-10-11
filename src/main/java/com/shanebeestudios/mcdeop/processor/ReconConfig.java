package com.shanebeestudios.mcdeop.processor;

import io.github.lxgaming.reconstruct.common.configuration.Config;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class ReconConfig implements Config {
    Path input;
    Path output;
    Path map;
    private int threads = Runtime.getRuntime().availableProcessors();

    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public boolean isTrace() {
        return false;
    }

    @Override
    public int getThreads() {
        return this.threads;
    }

    @Override
    public void setThreads(final int threads) {
        this.threads = threads;
    }

    @Override
    public Collection<String> getTransformers() {
        return List.of();
    }

    @Override
    public Path getInputPath() {
        return this.input;
    }

    @Override
    public void setInputPath(final Path inputPath) {
        this.input = inputPath;
    }

    @Override
    public Path getMappingPath() {
        return this.map;
    }

    @Override
    public void setMappingPath(final Path mappingPath) {
        this.map = mappingPath;
    }

    @Override
    public Path getOutputPath() {
        return this.output;
    }

    @Override
    public void setOutputPath(final Path outputPath) {
        this.output = outputPath;
    }

    @Override
    public Collection<String> getExcludedPackages() {
        return List.of("com.google.", "io.netty.", "it.unimi.dsi.fastutil.", "javax.", "joptsimple.", "org.apache.");
    }
}
