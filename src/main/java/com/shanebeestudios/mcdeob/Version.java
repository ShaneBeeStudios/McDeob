package com.shanebeestudios.mcdeob;

import com.shanebeestudios.mcdeob.util.Logger;
import com.shanebeestudios.mcdeob.util.Util;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Version {

    // Static Stuff
    private static final Map<String, Version> VERSION_MAP = new LinkedHashMap<>();
    private static final Map<String, Version> RELEASE_MAP = new LinkedHashMap<>();
    private static final Map<String, Version> SNAPSHOT_MAP = new LinkedHashMap<>();

    public static void initVersions() {
        JSONObject mojangManifest = Util.getJsonFromURL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        JSONObject seargeManifest = Util.getJsonFromURL("https://raw.githubusercontent.com/ShaneBeeStudios/Mappings/refs/heads/main/mappings/versions.json");

        boolean searge = false;
        List<String> seargeVersions = new ArrayList<>();
        for (Object o : seargeManifest.getJSONArray("versions")) {
            seargeVersions.add(o.toString());
        }

        for (Object o : mojangManifest.getJSONArray("versions")) {
            JSONObject versionObject = (JSONObject) o;
            String id = versionObject.getString("id");
            String type = versionObject.getString("type");
            String url = versionObject.getString("url");

            if (searge && !seargeVersions.contains(id)) {
                continue;
            }

            ReleaseType releaseType = type.equalsIgnoreCase("release") ? ReleaseType.RELEASE : ReleaseType.SNAPSHOT;
            Version version = new Version(id, releaseType, url, searge);
            VERSION_MAP.put(id, version);
            if (releaseType == ReleaseType.RELEASE) {
                RELEASE_MAP.put(id, version);
            } else {
                SNAPSHOT_MAP.put(id, version);
            }

            // Mojang mappings not available before 1.14.4
            // so we use searge mappings
            if (id.equalsIgnoreCase("1.14.4")) {
                searge = true;
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
    private final boolean searge;

    private Type type;
    private String jarURL;
    private String mapURL;

    public Version(String version, ReleaseType releaseType, String url, boolean searge) {
        this.version = version;
        this.releaseType = releaseType;
        this.url = url;
        this.searge = searge;
    }

    public boolean prepareVersion() {
        JSONObject versionInfo = Util.getJsonFromURL(this.url);
        JSONObject downloads = versionInfo.getJSONObject("downloads");
        Logger.info("Downloads: " + downloads.toString(2));
        if (downloads.has("server_mappings")) {
            String typeName = this.type.getName();
            this.jarURL = downloads.getJSONObject(typeName).getString("url");
            this.mapURL = downloads.getJSONObject(typeName + "_mappings").getString("url");
            return true;
        } else if (this.searge) {
            String typeName = this.type.getName();
            this.jarURL = downloads.getJSONObject(typeName).getString("url");
            this.mapURL = String.format("https://raw.githubusercontent.com/ShaneBeeStudios/Mappings/refs/heads/main/mappings/mappings_%s_%s.txt", typeName, this.version);
            return true;
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

    public boolean isSearge() {
        return this.searge;
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
