package com.shanebeestudios.mcdeop;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public enum Version {

    // Release
    SERVER_1_14_4(Type.SERVER, "1.14.4", "3dc3d84a581f14691199cf6831b71ed1296a9fdf/server.jar", "448ccb7b455f156bb5cb9cdadd7f96cd68134dbd/server.txt"),
    CLIENT_1_14_4(Type.CLIENT, "1.14.4", "8c325a0c5bd674dd747d6ebaa4c791fd363ad8a9/client.jar", "c0c8ef5131b7beef2317e6ad80ebcd68c4fb60fa/client.txt"),
    SERVER_1_15_2(Type.SERVER, "1.15.2", "bb2b6b1aefcd70dfd1892149ac3a215f6c636b07/server.jar", "59c55ae6c2a7c28c8ec449824d9194ff21dc7ff1/server.txt"),
    CLIENT_1_15_2(Type.CLIENT, "1.15.2", "e3f78cd16f9eb9a52307ed96ebec64241cc5b32d/client.jar", "bd9efb5f556f0e44f04adde7aeeba219421585c2/client.txt"),

    // Snapshot
    SERVER_20w14infinite(Type.SERVER, "20w14infinite", "c0711cd9608d1af3d6f05ac423dd8f4199780225/server.jar", "428c32326e6fd1e4a2bade80e6f277b75e497eee/server.txt"),
    CLIENT_20w14infinite(Type.CLIENT, "20w14infinite", "cc5cb23748614a6396ffb77427b4f11f4b6ae99b/client.jar", "9c696595b65e342a25ff96d7375b744c8a8aa51d/client.txt"),
    SERVER_20w16a(Type.SERVER, "20w16a", "754bbd654d8e6bd90cd7a1464a9e68a0624505dd/server.jar", "713d312463ed7186fff90ec72f922810ea8ad010/server.txt"),
    CLIENT_20w16a(Type.CLIENT, "20w16a", "5edab2c289420b7201b8f780c2dc4669b77fcd1e/client.jar", "1f6a60bcbdc5f35d7c03093c09a3f92f119c04a5/client.txt");

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
