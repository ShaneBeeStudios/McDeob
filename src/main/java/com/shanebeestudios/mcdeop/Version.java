package com.shanebeestudios.mcdeop;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public enum Version {

    // Release
    SERVER_1_14_4(Type.SERVER, "1.14.4", "3dc3d84a581f14691199cf6831b71ed1296a9fdf", "448ccb7b455f156bb5cb9cdadd7f96cd68134dbd"),
    CLIENT_1_14_4(Type.CLIENT, "1.14.4", "8c325a0c5bd674dd747d6ebaa4c791fd363ad8a9", "c0c8ef5131b7beef2317e6ad80ebcd68c4fb60fa"),
    SERVER_1_15_1(Type.SERVER, "1.15.1", "4d1826eebac84847c71a77f9349cc22afd0cf0a1", "47f8a03f5492223753f5f2b531d4938813903684"),
    CLIENT_1_15_1(Type.CLIENT, "1.15.1", "8b11614bea9293592a947ea8f4fd72981ea66677", "cc77cb804c2cc0fa151b0745df4c5de8e64d1bb5"),
    SERVER_1_15_2(Type.SERVER, "1.15.2", "bb2b6b1aefcd70dfd1892149ac3a215f6c636b07", "59c55ae6c2a7c28c8ec449824d9194ff21dc7ff1"),
    CLIENT_1_15_2(Type.CLIENT, "1.15.2", "e3f78cd16f9eb9a52307ed96ebec64241cc5b32d", "bd9efb5f556f0e44f04adde7aeeba219421585c2"),

    // Snapshot
    SERVER_20w14infinite(Type.SERVER, "20w14infinite", "c0711cd9608d1af3d6f05ac423dd8f4199780225", "428c32326e6fd1e4a2bade80e6f277b75e497eee"),
    CLIENT_20w14infinite(Type.CLIENT, "20w14infinite", "cc5cb23748614a6396ffb77427b4f11f4b6ae99b", "9c696595b65e342a25ff96d7375b744c8a8aa51d"),
    SERVER_20w16a(Type.SERVER, "20w16a", "754bbd654d8e6bd90cd7a1464a9e68a0624505dd", "713d312463ed7186fff90ec72f922810ea8ad010"),
    CLIENT_20w16a(Type.CLIENT, "20w16a", "5edab2c289420b7201b8f780c2dc4669b77fcd1e", "1f6a60bcbdc5f35d7c03093c09a3f92f119c04a5"),
    SERVER_20w17a(Type.SERVER, "20w17a", "0b7e36b084577fb26148c6341d590ac14606db21", "073daf6a9d136b57c63a11460761d59ec8ed93f4"),
    CLIENT_20w17a(Type.CLIENT, "20w17a", "0f9a4d6f9bc5b8fe3a3b5e1d6787f856de3d4f72", "4154bab4c16af203c027c51646a87e17034cd096"),
    SERVER_20w18a(Type.SERVER, "20w18a", "4d84832bdc7f62aa96b0d5d5a73b1272e8c474b5", "a35a93d06fb0d36f77c0c49901a8c9d4b9901c84"),
    CLIENT_20w18a(Type.CLIENT, "20w18a", "472c80b7df1ef0818da70ef7d13ad512866afd09", "b93fd42cb6d6666accd68b786051b607876e8e1c"),
    SERVER_20w19a(Type.SERVER, "20w19a", "fbb3ad3e7b25e78723434434077995855141ff07", "0fe3dacac40270c705676901f8884bd30230a7e4"),
    CLIENT_20w19a(Type.CLIENT, "20w19a", "10788e762e437780f17b16832b59647548b34ac1", "2fe5956ac11142d1ec3551e4bad01b69cb74f52f"),
    SERVER_20w20a(Type.SERVER, "20w20a", "f06a943eb107494688b4447b97514af6d7311623", "f1fe695debf3ab5ff71dc3685e5d6729f8a07690"),
    CLIENT_20w20a(Type.CLIENT, "20w20a", "2b171fb288bfd9c845f99015cbccf1d38bdaef65", "539b7ecdeee283c4afa0c40889392e670906cf94"),
    SERVER_20w20b(Type.SERVER, "20w20b", "0393774fb1f9db8288a56dbbcf45022b71f7939f", "deda26821d352131195c159d3fc733366ba71739"),
    CLIENT_20w20b(Type.CLIENT, "20w20b", "ff70a84c9cdc99a866c4b06410ec46f541dcf988", "61350a5e369f9b329ae796ca090cfac2c4438e67"),
    SERVER_20w21a(Type.SERVER, "20w21a", "03b8fa357937d0bdb6650ec8cc74506ec2fd91a7", "cd36be8de62b1a50f174b06767b49b9f79f3b807"),
    CLIENT_20w21a(Type.CLIENT, "20w21a", "96efb012b75620e881d5b97deb6fd9f02caab4e5", "a41221343ccf3bd58dec5b5fc091ce98b27003cb"),
    SERVER_20w22a(Type.SERVER, "20w22a", "c4a62eb36917aaa06dc8e20a2a35264d5fda123b", "306108bb986b3e7d41f63028f1ec2817a476426d"),
    CLIENT_20w22a(Type.CLIENT, "20w22a", "905dbb8972c73e2e62fbfa31b828ba299e136743", "9ccc34d58d4f2ddbf13ca10c5d5f31ff89607936"),

    // Combat Test
    SERVER_COMBAT_TEST_5(Type.SERVER, "combat_test_5", "0cc9a1582949297c8f1ca83b937c8d84ad295ffe", "44b2ca1d4677f3fffaa0333c87a42a9449e0bc52"),
    CLIENT_COMBAT_TEST_5(Type.CLIENT, "combat_test_5", "0ad80b317873e1f64a9a79af09a02ddef62ce67c", "5c9745ba8db67bcc95e6de65a3a6dd1d4c655726");

    private final Type type;
    private final String version;
    private final String jar;
    private final String mappings;

    Version(Type type, String version, String jar, String mappings) {
        this.type = type;
        this.version = version;
        this.jar = jar + "/" + type.getName() + ".jar";
        this.mappings = mappings + "/" + type.getName()  + ".txt";
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

    @Override
    public String toString() {
        return "Version{" +
                "type=" + type +
                ", version='" + version + '\'' +
                '}';
    }

    private static final String OBJECTS = "https://launcher.mojang.com/v1/objects/";
    private static final Map<String, Version> SERVER_VERSION_MAP = new HashMap<>();
    private static final Map<String, Version> CLIENT_VERSION_MAP = new HashMap<>();

    static {
        for (Version ver : values()) {
            if (ver.type == Type.SERVER) {
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
        SERVER("server"), CLIENT("client");

        private final String name;

        Type(String name) {
           this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}
