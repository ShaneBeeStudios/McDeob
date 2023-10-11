package com.shanebeestudios.mcdeop.launchermeta;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shanebeestudios.mcdeop.launchermeta.data.release.ReleaseManifest;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import com.shanebeestudios.mcdeop.launchermeta.data.version.VersionManifest;
import com.shanebeestudios.mcdeop.util.RequestUtil;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LauncherMetaManager {
    private static final String VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .build();

    private final URL versionManifestUrl;

    public LauncherMetaManager() {
        try {
            this.versionManifestUrl = new URL(VERSION_MANIFEST_URL);
        } catch (final MalformedURLException e) {
            throw new IllegalStateException("Failed to parse version manifest url", e);
        }
    }

    private <T> T get(final URL url, final Class<T> clazz) throws IOException {
        final Request request = new Request.Builder().url(url).build();

        final Call call = RequestUtil.CLIENT.newCall(request);
        try (Response response = call.execute()) {
            final ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Response body was null");
            }

            return this.objectMapper.readValue(body.bytes(), clazz);
        }
    }

    public VersionManifest getVersionManifest() throws IOException {
        return this.get(this.versionManifestUrl, VersionManifest.class);
    }

    public ReleaseManifest getReleaseManifest(final Version version) throws IOException {
        final ReleaseManifest releaseManifest = this.get(version.getUrl(), ReleaseManifest.class);
        releaseManifest.setVersion(version);
        return releaseManifest;
    }
}
