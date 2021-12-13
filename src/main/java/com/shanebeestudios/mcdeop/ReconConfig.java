package com.shanebeestudios.mcdeop;

import io.github.lxgaming.reconstruct.common.configuration.Config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReconConfig implements Config {

    private int threads = Runtime.getRuntime().availableProcessors();
    Path input, output, map;



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
        List<String> exclude = new ArrayList<>();
        exclude.add("com.google.");
        exclude.add("io.netty.");
        exclude.add("it.unimi.dsi.fastutil.");
        exclude.add("javax.");
        exclude.add("joptsimple.");
        exclude.add("org.apache.");
        return exclude;
    }

}
