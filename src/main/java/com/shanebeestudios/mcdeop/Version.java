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

    // Snapshot
    SERVER_20w14infinite(Type.SERVER, "20w14infinite", "c0711cd9608d1af3d6f05ac423dd8f4199780225", "428c32326e6fd1e4a2bade80e6f277b75e497eee"),
    CLIENT_20w14infinite(Type.CLIENT, "20w14infinite", "cc5cb23748614a6396ffb77427b4f11f4b6ae99b", "9c696595b65e342a25ff96d7375b744c8a8aa51d"),
    SERVER_20w20a(Type.SERVER, "20w20a", "f06a943eb107494688b4447b97514af6d7311623", "f1fe695debf3ab5ff71dc3685e5d6729f8a07690"),
    CLIENT_20w20a(Type.CLIENT, "20w20a", "2b171fb288bfd9c845f99015cbccf1d38bdaef65", "539b7ecdeee283c4afa0c40889392e670906cf94"),
    SERVER_20w20b(Type.SERVER, "20w20b", "0393774fb1f9db8288a56dbbcf45022b71f7939f", "deda26821d352131195c159d3fc733366ba71739"),
    CLIENT_20w20b(Type.CLIENT, "20w20b", "ff70a84c9cdc99a866c4b06410ec46f541dcf988", "61350a5e369f9b329ae796ca090cfac2c4438e67"),
    SERVER_20w21a(Type.SERVER, "20w21a", "03b8fa357937d0bdb6650ec8cc74506ec2fd91a7", "cd36be8de62b1a50f174b06767b49b9f79f3b807"),
    CLIENT_20w21a(Type.CLIENT, "20w21a", "96efb012b75620e881d5b97deb6fd9f02caab4e5", "a41221343ccf3bd58dec5b5fc091ce98b27003cb"),
    SERVER_20w22a(Type.SERVER, "20w22a", "c4a62eb36917aaa06dc8e20a2a35264d5fda123b", "306108bb986b3e7d41f63028f1ec2817a476426d"),
    CLIENT_20w22a(Type.CLIENT, "20w22a", "905dbb8972c73e2e62fbfa31b828ba299e136743", "9ccc34d58d4f2ddbf13ca10c5d5f31ff89607936"),
    SERVER_1_16_pre_1(Type.SERVER, "1.16-pre1", "ce66f98cabc1038ceba3b715b7dad5f70e27f88a", "106642bcb88d66eb293eb737e7440842f851eb43"),
    CLIENT_1_16_pre_1(Type.CLIENT, "1.16-pre1", "a0041360ae7579fb553d26aa5e8bdb869815e54a", "744e4a4bb6613c6c1ba7493cf968278392fe8d01"),
    SERVER_1_16_pre_2(Type.SERVER, "1.16-pre2", "8daeb71269eb164097d7d7ab1fa93fc93ab125c3", "c7b54e3ab9d29d13b4efc1ff43aa70325536c384"),
    CLIENT_1_16_pre_2(Type.CLIENT, "1.16-pre2", "a1382f4cf8815e3085efb1782cf32ed5cb621b86", "b7b693fc488eb874f3597648b6fe02182d56f467"),
    SERVER_1_16_pre_3(Type.SERVER, "1.16-pre3", "0b5653b65bc494fa55349682cebf50abf0d72ad9", "c1323c8127c52e228918e82fd4b74d40fb2897a2"),
    CLIENT_1_16_pre_3(Type.CLIENT, "1.16-pre3", "0d7c195c685f8a6172696c9a7f8f4e5d76185528", "5be40d7307349ae9d8a7cb3066d0bb5b4817226d"),
    SERVER_1_16_pre_4(Type.SERVER, "1.16-pre4", "018cdde89f8ff3831ce27c6c8dbf6831e99e0e75", "78ec69bde1ba4a229b2aafe1bc5c749d9cfee963"),
    CLIENT_1_16_pre_4(Type.CLIENT, "1.16-pre4", "67bd747f23f658a442766588d044074c65c41332", "16f51796f36dffb414086b46ac9dbb1d3e885d02"),
    SERVER_1_16_pre_5(Type.SERVER, "1.16-pre5", "56081523bca4f7074f111d1e8a9fd0a86d072a2b", "62339bd42867bf2ae7c77b6988ea4d521d2aaf73"),
    CLIENT_1_16_pre_5(Type.CLIENT, "1.16-pre5", "424242726ad6c828f47af0ef2a8dc3ac7a91ef96", "5fbebb6c08969909fbb4e41665a61a44f3f24712"),
    SERVER_1_16_pre_6(Type.SERVER, "1.16-pre6", "8984939f42371a7e614fa48669e308c4cc9ba228", "25ece62bca4dad450c00f91de8803cf90a72a3e7"),
    CLIENT_1_16_pre_6(Type.CLIENT, "1.16-pre6", "44aa1f291392b002cb7096c71913306f57e318e0", "412dc3f3ca371f4945f8db4edeeab57e1b77465a"),
    SERVER_1_16_pre_7(Type.SERVER, "1.16-pre7", "577f7287642309a2a32e80be395329118dfddb3f", "5440a2df052cc134b8ef8ba8357dd254222c1f94"),
    CLIENT_1_16_pre_7(Type.CLIENT, "1.16-pre7", "62473fe623906a01b2f7cf4f7c1396ab23697e5f", "94e705bf660947337173cea6cafe8a614e52f227"),
    SERVER_1_16_pre_8(Type.SERVER, "1.16-pre8", "d6a747371b200216653be9b4140cd2862eddbb0e", "82bf7b8ffb8e9ead5fe03e4324ba542a1e5ecc7c"),
    CLIENT_1_16_pre_8(Type.CLIENT, "1.16-pre8", "23b14c93185398af577353c47af04248991b72b4", "a8c539fd4bef5f07f5feec4c18a4b870c167a1a0"),
    SERVER_1_16_rc_1(Type.SERVER, "1.16-rc1", "7213e5ba8fe8d352141cf3dde907c26c43480092", "c3d6c25f90fc1cfa535925d2fe6ae0722c3fed58"),
    CLIENT_1_16_rc_1(Type.CLIENT, "1.16-rc1", "a056316b66aea50c0555d5f438cf839b6cdde000", "c68f223b19fd1be84ba2f7b55546c7cbf64e5078"),

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
