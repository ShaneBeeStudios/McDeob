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

    public static void initVersions() {
        JSONObject versionManifest;
        try {
            versionManifest = Util.getJsonFromURL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Object o : versionManifest.getJSONArray("versions")) {
            JSONObject versionObject = (JSONObject) o;
            String version = versionObject.getString("id");
            String releaseType = versionObject.getString("type");
            String url = versionObject.getString("url");

            VERSION_MAP.put(version, new Version(version, releaseType, url));

            // Mappings not available before 1.14.4, so we exit
            if (version.equalsIgnoreCase("1.14.4")) {
                break;
            }
        }
    }

    public static Collection<Version> getVersions() {
        return VERSION_MAP.values();
    }

    @Nullable
    public static Version getByVersion(String version) {
        return VERSION_MAP.get(version);
    }

    // Class Stuff
    private final String version;
    private final String releaseType;
    private final String url;

    private Type type;
    private String jarURL;
    private String mapURL;

    public Version(String version, String releaseType, String url) {
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

    public String getReleaseType() {
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

}
