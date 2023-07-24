package com.shanebeestudios.mcdeop;

public enum SourceType {
    SERVER,
    CLIENT;

    public String getName() {
        return this.name().toLowerCase();
    }
}
