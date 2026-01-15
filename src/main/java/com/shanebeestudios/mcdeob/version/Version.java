package com.shanebeestudios.mcdeob.version;

import com.shanebeestudios.mcdeob.util.Util;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Locale;

public class Version {

    private final String version;
    private final ReleaseType releaseType;
    private final String url;
    private final MappingType mappingType;

    private Type type;
    private String jarURL;
    private @Nullable String mapURL;

    public Version(String version, ReleaseType releaseType, String url, MappingType mappingType) {
        this.version = version;
        this.releaseType = releaseType;
        this.url = url;
        this.mappingType = mappingType;
    }

    public boolean prepareVersion() {
        JSONObject versionInfo = Util.getJsonFromURL(this.url);
        if (versionInfo == null) return false;

        JSONObject downloads = versionInfo.getJSONObject("downloads");
        String typeName = this.type.getName();
        this.jarURL = downloads.getJSONObject(typeName).getString("url");
        if (this.mappingType == MappingType.UNOBFUSCATED) {
            this.mapURL = null;
            return true;
        } else if (this.mappingType == MappingType.MOJANG) {
            JSONObject mapObject = downloads.getJSONObject(typeName + "_mappings");
            this.mapURL = mapObject.getString("url");
            return true;
        } else if (this.mappingType == MappingType.SEARGE) {
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

    public @Nullable String getMapURL() {
        return this.mapURL;
    }

    public MappingType getMappingType() {
        return this.mappingType;
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

    public enum MappingType {
        UNOBFUSCATED,
        MOJANG,
        SEARGE
    }

    @Override
    public String toString() {
        return this.version;
    }

}
