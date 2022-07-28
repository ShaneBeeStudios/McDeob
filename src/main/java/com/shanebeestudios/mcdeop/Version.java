package com.shanebeestudios.mcdeop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public enum Version {

    // Latest
    SERVER_LATEST_RELEASE(Type.SERVER, "Latest_Release", null, null),
    CLIENT_LATEST_RELEASE(Type.CLIENT, "Latest_Release", null, null),
    SERVER_LATEST_SNAPSHOT(Type.SERVER, "Latest_Snapshot", null, null),
    CLIENT_LATEST_SNAPSHOT(Type.CLIENT, "Latest_Snapshot", null, null),

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
    SERVER_1_19(Type.SERVER, "1.19", "e00c4052dac1d59a1188b2aa9d5a87113aaf1122", "1c1cea17d5cd63d68356df2ef31e724dd09f8c26"),
    CLIENT_1_19(Type.CLIENT, "1.19", "c0898ec7c6a5a2eaa317770203a1554260699994", "150346d1c0b4acec0b4eb7f58b86e3ea1aa730f3"),
    SERVER_1_19_1(Type.SERVER, "1.19.1", "8399e1211e95faa421c1507b322dbeae86d604df", "3565648cdd47ae15738fb804a95a659137d7cfd3"),
    CLIENT_1_19_1(Type.CLIENT, "1.19.1", "90d438c3e432add8848a9f9f368ce5a52f6bc4a7", "fc8e22d42c0e4eb1899e2acf7e97eae917e1cb94"),

    // Snapshot

    // Combat Test
    SERVER_COMBAT_TEST_8c(Type.SERVER, "Combat_Test_8c", "b707c44ac1503ad179fde86c78d41aa4d0cc78a5", "d89f9e0eb8fbe6f2c91e749e8b59391cd0dd96d4"),
    CLIENT_COMBAT_TEST_8c(Type.CLIENT, "Combat_Test_8c", "177472ace3ff5d98fbd63b4bcd5bbef5b035a018", "5ea38a7b8d58837c97214f2a46e5e12151d51f83"),

    // April Fools
    SERVER_20w14infinite(Type.SERVER, "20w14infinite", "c0711cd9608d1af3d6f05ac423dd8f4199780225", "a94a32e698caff0f5c5762b3dca045ddcd587071"),
    CLIENT_20w14infinite(Type.CLIENT, "20w14infinite", "cc5cb23748614a6396ffb77427b4f11f4b6ae99b", "3d91233a24c5de720f0eb41927a0b00e45e89caa"),
    SERVER_22w13one(Type.SERVER, "22w13oneBlockAtATime", "5f48eea55c7fd1881d9c63835b15dfb5bbcd3a67", "2c55055b906935ffe1e7e7cb80d1a8b031eb9f95"),
    CLIENT_22w13one(Type.CLIENT, "22w13oneBlockAtATime", "6548ac25265c2726a2cd24dec3820c07ee2b0b4b", "b1bbd68dbf48041dd994f1a4c1a3eacfa46e344b");

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

    public boolean isLatest() {
        return this == CLIENT_LATEST_RELEASE || this == SERVER_LATEST_RELEASE
                || this == CLIENT_LATEST_SNAPSHOT || this == SERVER_LATEST_SNAPSHOT;
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
    private static final List<Version> AVAILABLE_VERSIONS = new ArrayList<>();

    public static List<Version> getVersions() {
        return AVAILABLE_VERSIONS;
    }

    static {
        for (Version ver : values()) {
            if (ver.type == Type.SERVER) {
                SERVER_VERSION_MAP.put(ver.getVersion().toLowerCase(), ver);
                AVAILABLE_VERSIONS.add(ver);
            } else {
                CLIENT_VERSION_MAP.put(ver.getVersion().toLowerCase(), ver);
            }
        }
    }

    public static Version getByVersion(String ver, Type type) {
        if (type == Type.SERVER) {
            return SERVER_VERSION_MAP.get(ver.toLowerCase());
        } else {
            return CLIENT_VERSION_MAP.get(ver.toLowerCase());
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
