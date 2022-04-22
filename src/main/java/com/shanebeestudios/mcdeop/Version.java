package com.shanebeestudios.mcdeop;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public enum Version {

    // Release
    SERVER_1_14_4(Type.SERVER, "1.14.4", "3dc3d84a581f14691199cf6831b71ed1296a9fdf", "46a7ba37c15820f00d49eafb38afb4a9bb64a0be"),
    CLIENT_1_14_4(Type.CLIENT, "1.14.4", "8c325a0c5bd674dd747d6ebaa4c791fd363ad8a9", "3a0e42172d082f18c4ee0b4561a6a2ecc9868a58"),
    SERVER_1_15(Type.SERVER, "1.15", "e9f105b3c5c7e85c7b445249a93362a22f62442d", "c79e5ee9c5167b730266910d4c5bafbaf27c2f52"),
    CLIENT_1_15(Type.CLIENT, "1.15", "7b07fd09d1e3aae1bc7a1304fedc73bfe5d81800", "a0bea08f0b96f6f30a350cd75270be63f7d0aac4"),
    SERVER_1_15_1(Type.SERVER, "1.15.1", "4d1826eebac84847c71a77f9349cc22afd0cf0a1", "d10e23f8def30fcf7d0a0d027f48f2731d80208f"),
    CLIENT_1_15_1(Type.CLIENT, "1.15.1", "8b11614bea9293592a947ea8f4fd72981ea66677", "24c55605b6ebd4ee0b28d41d609b68599ef23c67"),
    SERVER_1_15_2(Type.SERVER, "1.15.2", "bb2b6b1aefcd70dfd1892149ac3a215f6c636b07", "e018f7413ad5b98d7427bc3027c95c78845e891b"),
    CLIENT_1_15_2(Type.CLIENT, "1.15.2", "e3f78cd16f9eb9a52307ed96ebec64241cc5b32d", "e101497d78faca35dec18f632de16c25899b6f08"),
    SERVER_1_16(Type.SERVER, "1.16", "a0d03225615ba897619220e256a266cb33a44b6b", "a11471890ef5bdc4025dd7a587a46f106d56a7da"),
    CLIENT_1_16(Type.CLIENT, "1.16", "228fdf45541c4c2fe8aec4f20e880cb8fcd46621", "c04e0f0d37414fc022ca31062acb0ff1d67be331"),
    SERVER_1_16_1(Type.SERVER, "1.16.1", "a412fd69db1f81db3f511c1463fd304675244077", "a11471890ef5bdc4025dd7a587a46f106d56a7da"),
    CLIENT_1_16_1(Type.CLIENT, "1.16.1", "c9abbe8ee4fa490751ca70635340b7cf00db83ff", "588f9a7260759c0c10e193162f76fde005a46fe2"),
    SERVER_1_16_2(Type.SERVER, "1.16.2", "c5f6fb23c3876461d46ec380421e42b289789530", "0dbbb5aae568c2d9aa34e3be11e7b525054265d9"),
    CLIENT_1_16_2(Type.CLIENT, "1.16.2", "653e97a2d1d76f87653f02242d243cdee48a5144", "88fbd1c70c9244d23e6166a9703cc456d6f115e6"),
    SERVER_1_16_3(Type.SERVER, "1.16.3", "f02f4473dbf152c23d7d484952121db0b36698cb", "e75ff1e729aec4a3ec6a94fe1ddd2f5a87a2fd00"),
    CLIENT_1_16_3(Type.CLIENT, "1.16.3", "1321521b2caf934f7fc9665aab7e059a7b2bfcdf", "faac5028fbca3859db970cc4ca041aeec55f6d9d"),
    SERVER_1_16_4(Type.SERVER, "1.16.4", "35139deedbd5182953cf1caa23835da59ca3d7cd", "d9ae0e8e28475254855430ff051daaa0dd041a08"),
    CLIENT_1_16_4(Type.CLIENT, "1.16.4", "1952d94a0784e7abda230aae6a1e8fc0522dba99", "0837de813d1a6b67e23da3c520a84e872c8d2f0e"),
    SERVER_1_16_5(Type.SERVER, "1.16.5", "1b557e7b033b583cd9f66746b7a9ab1ec1673ced", "41285beda6d251d190f2bf33beadd4fee187df7a"),
    CLIENT_1_16_5(Type.CLIENT, "1.16.5", "37fd3c903861eeff3bc24b71eed48f828b5269c8", "374c6b789574afbdc901371207155661e0509e17"),
    SERVER_1_17(Type.SERVER, "1.17", "0a269b5f2c5b93b1712d0f5dc43b6182b9ab254e", "84d80036e14bc5c7894a4fad9dd9f367d3000334"),
    CLIENT_1_17(Type.CLIENT, "1.17", "1cf89c77ed5e72401b869f66410934804f3d6f52", "227d16f520848747a59bef6f490ae19dc290a804"),
    SERVER_1_17_1(Type.SERVER, "1.17.1", "a16d67e5807f57fc4e550299cf20226194497dc2", "f6cae1c5c1255f68ba4834b16a0da6a09621fe13"),
    CLIENT_1_17_1(Type.CLIENT, "1.17.1", "8d9b65467c7913fcf6f5b2e729d44a1e00fde150", "e4d540e0cba05a6097e885dffdf363e621f87d3f"),
    SERVER_1_18(Type.SERVER, "1.18", "3cf24a8694aca6267883b17d934efacc5e44440d", "a8fe854e35a69df7289d3f03fc0821f6363f2238"),
    CLIENT_1_18(Type.CLIENT, "1.18", "d49eb6caed53d23927648c97451503442f9e26fd", "e824c89c612c0b9cb438ef739c44726c59bbf679"),
    SERVER_1_18_1(Type.SERVER, "1.18.1", "125e5adf40c659fd3bce3e66e67a16bb49ecc1b9", "9717df2acd926bd4a9a7b2ce5f981bb7e4f7f04a"),
    CLIENT_1_18_1(Type.CLIENT, "1.18.1", "7e46fb47609401970e2818989fa584fd467cd036", "99ade839eacf69b8bed88c91bd70ca660aee47bb"),
    SERVER_1_18_2(Type.SERVER, "1.18.2", "c8f83c5655308435b3dcf03c06d9fe8740a77469", "e562f588fea155d96291267465dc3323bfe1551b"),
    CLIENT_1_18_2(Type.CLIENT, "1.18.2", "2e9a3e3107cca00d6bc9c97bf7d149cae163ef21", "a661c6a55a0600bd391bdbbd6827654c05b2109c"),

    // Snapshot
    SERVER_1_19_22w14a(Type.SERVER, "1.19 22w14a", "cf4f3a6492c0a84e2e852fe0ea714080923ab6ad", "7b9857a6a7f3d7910700528627a8912947512178"),
    CLIENT_1_19_22w14a(Type.CLIENT, "1.19 22w14a", "1d186d967576d4e6043e5b1664da242c1b686a85", "9b86041edb4aa515fc8951f16e3d3be3e0c0533e"),
    SERVER_1_19_22w15a(Type.SERVER, "1.19 22w15a", "2760f745a00711bcc19bf78d6056019f69318d03", "496416e9ac476347b47655089b7819877977f737"),
    CLIENT_1_19_22w15a(Type.CLIENT, "1.19 22w15a", "b5b14c791de1b44ce2d6de13dfd18b9ced843618", "5ca2f62eacc890b09b9b9b45cd1edfe36cdee34a"),
    SERVER_1_19_22w16b(Type.SERVER, "1.19 22w16b", "a54810e8b1a7a043fa54a462309d680ad67da479", "54d27d0b812eab6ffc04e6d3d0236fa7920a5c87"),
    CLIENT_1_19_22w16b(Type.CLIENT, "1.19 22w16b", "8ebd18862d0de2389d1b3dab0bf660f20851bc3e", "b40c6ecf3d0785d109d10eea1814ce11f5ec9da4"),

    // Experimental
    SERVER_1_19_exp_1(Type.SERVER, "1.19 Experimental 1", "973e76b8067bab5410fef92bf4412ada1f7b0f97", "7c6d4d114bfb0cb482a549dda1b587aece9c04af"),
    CLIENT_1_19_exp_1(Type.CLIENT, "1.19 Experimental 1", "17f03648f0956343d4ae35260067d200a5aabc56", "668d017c0c3bfc47a7b4c9e75e0841b31123c5e0"),

    // Combat Test
    SERVER_COMBAT_TEST_8c(Type.SERVER, "combat_test_8c", "b707c44ac1503ad179fde86c78d41aa4d0cc78a5", "d89f9e0eb8fbe6f2c91e749e8b59391cd0dd96d4"),
    CLIENT_COMBAT_TEST_8c(Type.CLIENT, "combat_test_8c", "177472ace3ff5d98fbd63b4bcd5bbef5b035a018", "5ea38a7b8d58837c97214f2a46e5e12151d51f83"),

    // April Fools
    SERVER_20w14infinite(Type.SERVER, "20w14infinite", "c0711cd9608d1af3d6f05ac423dd8f4199780225", "a94a32e698caff0f5c5762b3dca045ddcd587071"),
    CLIENT_20w14infinite(Type.CLIENT, "20w14infinite", "cc5cb23748614a6396ffb77427b4f11f4b6ae99b", "3d91233a24c5de720f0eb41927a0b00e45e89caa");

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
