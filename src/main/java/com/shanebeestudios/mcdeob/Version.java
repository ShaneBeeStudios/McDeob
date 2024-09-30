package com.shanebeestudios.mcdeob;

import com.shanebeestudios.mcdeob.util.Util;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Version {

    // Static Stuff
    private static final Map<String, Version> VERSION_MAP = new LinkedHashMap<>();
    private static final Map<String, Version> RELEASE_MAP = new LinkedHashMap<>();
    private static final Map<String, Version> SNAPSHOT_MAP = new LinkedHashMap<>();

    public static void initVersions() {
        JSONObject versionManifest;
        try {
            versionManifest = Util.getJsonFromURL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Object o : versionManifest.getJSONArray("versions")) {
            JSONObject versionObject = (JSONObject) o;
            String id = versionObject.getString("id");
            String type = versionObject.getString("type");
            String url = versionObject.getString("url");

            ReleaseType releaseType = type.equalsIgnoreCase("release") ? ReleaseType.RELEASE : ReleaseType.SNAPSHOT;
            Version version = new Version(id, releaseType, url);
            VERSION_MAP.put(id, version);
            if (releaseType == ReleaseType.RELEASE) {
                RELEASE_MAP.put(id, version);
            } else {
                SNAPSHOT_MAP.put(id, version);
            }

            // Mappings not available before 1.14.4, so we exit
            if (id.equalsIgnoreCase("1.14.4")) {
                break;
            }
        }
    }

    public static Collection<Version> getAllVersions() {
        return VERSION_MAP.values();
    }

    public static Collection<Version> getReleaseVersions() {
        return RELEASE_MAP.values();
    }

    public static Collection<Version> getSnapshotVersions() {
        return SNAPSHOT_MAP.values();
    }

    @Nullable
    public static Version getByVersion(String version) {
        return VERSION_MAP.get(version);
    }

    // Class Stuff
    private final String version;
    private final ReleaseType releaseType;
    private final String url;

    private Type type;
    private String jarURL;
    private String mapURL;

    public Version(String version, ReleaseType releaseType, String url) {
        this.version = version;
        this.releaseType = releaseType;
        this.url = url;
    }

    public boolean prepareVersion() {
        try {
            JSONObject versionInfo = Util.getJsonFromURL(this.url);
            JSONObject downloads = versionInfo.getJSONObject("downloads");
            if (downloads.has("server_mappings")) {
                String typeName = this.type.getName();
                this.jarURL = downloads.getJSONObject(typeName).getString("url");
                this.mapURL = downloads.getJSONObject(typeName + "_mappings").getString("url");
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String getVersion() {
        return this.version;
    }

    public ReleaseType getReleaseType() {
        return this.releaseType;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getJarURL() {
        return this.jarURL;
    }

    public String getMapURL() {
        return this.mapURL;
    }

    public enum Type {
        SERVER,
        CLIENT;

        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public enum ReleaseType {
        RELEASE,
        SNAPSHOT;

        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

}
