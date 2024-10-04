package com.shanebeestudios.mcdeob.version;

import com.shanebeestudios.mcdeob.util.Util;
import org.json.JSONObject;

import java.util.Locale;

public class Version {

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

    @Override
    public String toString() {
        return this.version;
    }

}
