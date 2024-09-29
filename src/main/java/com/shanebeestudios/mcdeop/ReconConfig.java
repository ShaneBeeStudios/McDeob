package com.shanebeestudios.mcdeop;

import io.github.lxgaming.reconstruct.common.configuration.Config;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class ReconConfig implements Config {

    private int threads = Runtime.getRuntime().availableProcessors();
    private Path input, output, map;

    public ReconConfig(Processor processor) {
        this.input = processor.jarPath;
        this.output = processor.remappedJar;
        this.map = processor.mappingsPath;
    }

    @Override
    public boolean isDebug() {
        return true;
    }

    @Override
    public boolean isTrace() {
        return true;
    }

    @Override
    public int getThreads() {
        return this.threads;
    }

    @Override
    public void setThreads(int threads) {
        this.threads = threads;
    }

    @Override
    public Collection<String> getTransformers() {
        return null;
    }

    @Override
    public Path getInputPath() {
        return input;
    }

    @Override
    public void setInputPath(Path inputPath) {
        this.input = inputPath;
    }

    @Override
    public Path getMappingPath() {
        return map;
    }

    @Override
    public void setMappingPath(Path mappingPath) {
        this.map = mappingPath;
    }

    @Override
    public Path getOutputPath() {
        return output;
    }

    @Override
    public void setOutputPath(Path outputPath) {
        this.output = outputPath;
    }

    @Override
    public Collection<String> getExcludedPackages() {
        return List.of("com.google.", "io.netty.", "it.unimi.dsi.fastutil.", "javax.", "joptsimple.", "org.apache.");
    }

}
