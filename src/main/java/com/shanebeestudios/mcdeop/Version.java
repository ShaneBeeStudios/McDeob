package com.shanebeestudios.mcdeop;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public enum Version {

    // Release
    SERVER_1_14_4(Type.SERVER, "1.14.4", "3dc3d84a581f14691199cf6831b71ed1296a9fdf", "46a7ba37c15820f00d49eafb38afb4a9bb64a0be"),
    CLIENT_1_14_4(Type.CLIENT, "1.14.4", "8c325a0c5bd674dd747d6ebaa4c791fd363ad8a9", "3a0e42172d082f18c4ee0b4561a6a2ecc9868a58"),
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
    SERVER_1_16_2_pre_2(Type.SERVER, "1.16.2-pre2", "d2cae287324631b2b4bfa609dd01c63cd6d4b78d", "544cc22c04e4d104535a596289c4bf08c7c0efa5"),
    CLIENT_1_16_2_pre_2(Type.CLIENT, "1.16.2-pre2", "ce5762620a698b464d07a6c733cd66c7fa8072a1", "bb48f3ad38b3a3f174ff9fbf70dda00a66dd71da"),
    SERVER_1_16_2_pre_3(Type.SERVER, "1.16.2-pre3", "bd47f78f185a525388e446aa44975c147057ebbd", "e28d7c32ce06496a284819467add0f021eebaea7"),
    CLIENT_1_16_2_pre_3(Type.CLIENT, "1.16.2-pre3", "8cfa861961862c263ff80f2f6478535fd1ed7d8b", "e912e5eda715f1b65ce7c23a7825ea9024cf1c35"),
    SERVER_1_16_2_rc_1(Type.SERVER, "1.16.2-rc1", "203e18d79201b5e8c46019074b07e1c3b4c75f57", "3405a0f2c0ccacd36a8158ae29b16eaa915b5d28"),
    CLIENT_1_16_2_rc_1(Type.CLIENT, "1.16.2-rc1", "dd4982bb447c1d2c7c03419b90dbe8f017c47311", "0eb95ed9d2231b960cce3369aa562cf42e4722fc"),
    SERVER_1_16_2_rc_2(Type.SERVER, "1.16.2-rc2", "45287d794fa2631b8da9b9002696ebe406fbed6b", "3405a0f2c0ccacd36a8158ae29b16eaa915b5d28"),
    CLIENT_1_16_2_rc_2(Type.CLIENT, "1.16.2-rc2", "157f7160b1b41bbaa681fcf8d98542bc27ab4b15", "614f6579d02959886633165f209574a7cf39e723"),

    // Combat Test
    SERVER_COMBAT_TEST_5(Type.SERVER, "combat_test_5", "0cc9a1582949297c8f1ca83b937c8d84ad295ffe", "44b2ca1d4677f3fffaa0333c87a42a9449e0bc52"),
    CLIENT_COMBAT_TEST_5(Type.CLIENT, "combat_test_5", "0ad80b317873e1f64a9a79af09a02ddef62ce67c", "5c9745ba8db67bcc95e6de65a3a6dd1d4c655726"),
    SERVER_COMBAT_TEST_6(Type.SERVER, "combat_test_6", "1c35c493ade7a39e2d02bcc326498aaab96f1a09", "6ddb98ca992d93fd654bf89f801223b1c4d98695"),
    CLIENT_COMBAT_TEST_6(Type.CLIENT, "combat_test_6", "4f08b4667575cbfc35e44c9556a0667bd3d0f5cc", "185a362b6b301bfb71e12f788ce8fb998b806217"),
    SERVER_COMBAT_TEST_7c(Type.SERVER, "combat_test_7c", "53c43fdae7d2ed01bbb31a82d99e31b9348e2a4b", "7f1369734e3e5585d44b211812274f2ad7ac160d"),
    CLIENT_COMBAT_TEST_7c(Type.CLIENT, "combat_test_7c", "14991d31f1002e5cbd97e62310cba614666f876b", "907af4fb6a7d676921f9bcb7aa80f8749bf03592"),

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
