package com.shanebeestudios.mcdeop.processor;

public enum SourceType {
    SERVER,
    CLIENT;

    public String getName() {
        return this.name().toLowerCase();
    }
}
