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
    SERVER_1_16(Type.SERVER, "1.16", "a0d03225615ba897619220e256a266cb33a44b6b", "618315f1fc2f56fe003612bb1fee1ad4060768a0"),
    CLIENT_1_16(Type.CLIENT, "1.16", "228fdf45541c4c2fe8aec4f20e880cb8fcd46621", "800eb4264594307d026ff911eabd1ee6fc9265f8"),
    SERVER_1_16_1(Type.SERVER, "1.16.1", "a412fd69db1f81db3f511c1463fd304675244077", "618315f1fc2f56fe003612bb1fee1ad4060768a0"),
    CLIENT_1_16_1(Type.CLIENT, "1.16.1", "c9abbe8ee4fa490751ca70635340b7cf00db83ff", "539e30896eaacb9bf002a0e7e60f159856d369f7"),

    // Snapshot
    SERVER_1_16_pre_8(Type.SERVER, "1.16-pre8", "d6a747371b200216653be9b4140cd2862eddbb0e", "82bf7b8ffb8e9ead5fe03e4324ba542a1e5ecc7c"),
    CLIENT_1_16_pre_8(Type.CLIENT, "1.16-pre8", "23b14c93185398af577353c47af04248991b72b4", "a8c539fd4bef5f07f5feec4c18a4b870c167a1a0"),
    SERVER_1_16_rc_1(Type.SERVER, "1.16-rc1", "7213e5ba8fe8d352141cf3dde907c26c43480092", "c3d6c25f90fc1cfa535925d2fe6ae0722c3fed58"),
    CLIENT_1_16_rc_1(Type.CLIENT, "1.16-rc1", "a056316b66aea50c0555d5f438cf839b6cdde000", "c68f223b19fd1be84ba2f7b55546c7cbf64e5078"),
    SERVER_20w27a(Type.SERVER, "20w27a", "40efae0a2412154f44a99f158752b8417b384f06", "17e0fd0e37436037b9edf6c34d94f8d2c06c7818"),
    CLIENT_20w27a(Type.CLIENT, "20w27a", "83f30681e76d3438105159c645d9f2f6df88a4d8", "61f5a006a56522f3eaa4faf155088f9dfd081ef5"),
    SERVER_20w28a(Type.SERVER, "20w28a", "1e36d315d96c29d8d32aa8fecbfb8efa4243a746", "0d80cc20b2d18516090166f2d00e9b96bf530e76"),
    CLIENT_20w28a(Type.CLIENT, "20w28a", "1af89d3bb666c848e5640715b6aaa4c0fc5ae7d0", "a148f69e5d66f1e67e7223ec19ad018481f82920"),
    SERVER_20w29a(Type.SERVER, "20w29a", "ea9a65a38e000fe76b51fa36e923c09d5d8fa473", "4fcc48eb10113d6c4f51a3b469ae3089f8c37a19"),
    CLIENT_20w29a(Type.CLIENT, "20w29a", "4a7182b743f15e3c787f8e62e47eb2dd93604167", "2d6ed3fdc3598d397951b34c180c8a9b8b98fc8a"),
    SERVER_20w30a(Type.SERVER, "20w30a", "db5da41d9c5f6c0d839661269d70f5b8c1ff7d0a", "bd3035b7f4004634f3a79a388a0a2c7f5130f1cb"),
    CLIENT_20w30a(Type.CLIENT, "20w30a", "60762a56f1552578d27b91df594ba2a8953dabcc", "a5a8ae491becf7fa134e2962f8abaeb277d200e6"),
    SERVER_1_16_2_pre_1(Type.SERVER, "1.16.2-pre1", "d4434bf4f2f0572a4eb54b3da1b1b3069a4e9ef2", "40c5c9e8df1823e4d34f5148a16c018245d761db"),
    CLIENT_1_16_2_pre_1(Type.CLIENT, "1.16.2-pre1", "18caa7718c665b6d8597f7979bbdcb078ea117a0", "3e9070cb333fbf94d81019fd9732f33fa3bab1e7"),

    // Combat Test
    SERVER_COMBAT_TEST_5(Type.SERVER, "combat_test_5", "0cc9a1582949297c8f1ca83b937c8d84ad295ffe", "44b2ca1d4677f3fffaa0333c87a42a9449e0bc52"),
    CLIENT_COMBAT_TEST_5(Type.CLIENT, "combat_test_5", "0ad80b317873e1f64a9a79af09a02ddef62ce67c", "5c9745ba8db67bcc95e6de65a3a6dd1d4c655726"),

    // April Fools
    SERVER_20w14infinite(Type.SERVER, "20w14infinite", "c0711cd9608d1af3d6f05ac423dd8f4199780225", "428c32326e6fd1e4a2bade80e6f277b75e497eee"),
    CLIENT_20w14infinite(Type.CLIENT, "20w14infinite", "cc5cb23748614a6396ffb77427b4f11f4b6ae99b", "9c696595b65e342a25ff96d7375b744c8a8aa51d");

    private final Type type;
    private final String version;
    private final String jar;
    private final String mappings;

    Version(Type type, String version, String jar, String mappings) {
        this.type = type;
        this.version = version;
        this.jar = jar + "/" + type.getName() + ".jar";
        this.mappings = mappings + "/" + type.getName() + ".txt";
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
