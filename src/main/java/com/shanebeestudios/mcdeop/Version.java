package com.shanebeestudios.mcdeop;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public enum Version {

    SERVER_1_15_2(Type.SERVER, "1.15.2", "bb2b6b1aefcd70dfd1892149ac3a215f6c636b07/server.jar", "59c55ae6c2a7c28c8ec449824d9194ff21dc7ff1/server.txt"),
    CLIENT_1_15_2(Type.CLIENT, "1.15.2", "e3f78cd16f9eb9a52307ed96ebec64241cc5b32d/client.jar", "bd9efb5f556f0e44f04adde7aeeba219421585c2/client.txt");

    private final Type type;
    private final String version;
    private final String jar;
    private final String mappings;

    Version(Type type, String version, String jar, String mappings) {
        this.type = type;
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

    public Type getType() {
        return type;
    }

    private static final String OBJECTS = "https://launcher.mojang.com/v1/objects/";
    private static final Map<String, Version> SERVER_VERSION_MAP = new HashMap<>();
    private static final Map<String, Version> CLIENT_VERSION_MAP = new HashMap<>();

    static {
        for (Version ver : values()) {
            if (ver.jar.contains("server")) {
                SERVER_VERSION_MAP.put(ver.getVersion(), ver);
            } else {
                CLIENT_VERSION_MAP.put(ver.getVersion(), ver);
            }
        }
    }

    public static Version getByVersion(String ver, Type type) {
        if (type == Type.SERVER) {
            return SERVER_VERSION_MAP.get(ver);
        } else {
            return CLIENT_VERSION_MAP.get(ver);
        }
    }

    public enum Type {
        SERVER, CLIENT
    }

}
