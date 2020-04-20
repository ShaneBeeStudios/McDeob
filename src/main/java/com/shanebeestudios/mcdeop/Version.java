package com.shanebeestudios.mcdeop;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public enum Version {

    SERVER_1_15_2("1.15.2", "bb2b6b1aefcd70dfd1892149ac3a215f6c636b07/server.jar", "59c55ae6c2a7c28c8ec449824d9194ff21dc7ff1/server.txt");

    private final String version;
    private final String jar;
    private final String mappings;

    Version(String version, String jar, String mappings) {
        this.version = version;
        this.jar = jar;
        this.mappings = mappings;
    }

    public String getVersion() {
        return version;
    }

    public String getJar() {
        return OBJECTS + jar;
    }

    public String getMappings() {
        return OBJECTS + mappings;
    }

    public boolean isServer() {
        return jar.contains("server");
    }

    private static final String OBJECTS = "https://launcher.mojang.com/v1/objects/";
    private static final Map<String, Version> VERSION_MAP = new HashMap<>();

    static {
        for (Version ver : values()) {
            VERSION_MAP.put(ver.getVersion(), ver);
        }
    }

    public static Version getByVersion(String ver) {
        return VERSION_MAP.get(ver);
    }

}
