package de.timmi6790.launchermeta;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.timmi6790.launchermeta.data.release.ReleaseManifest;
import de.timmi6790.launchermeta.data.version.Version;
import de.timmi6790.launchermeta.data.version.VersionManifest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.inject.Inject;
import okhttp3.*;

public class LauncherMeta {
    private static final String VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .build();

    private final URL versionManifestUrl;

    @Inject
    public LauncherMeta(final OkHttpClient httpClient) {
        try {
            this.versionManifestUrl = new URL(VERSION_MANIFEST_URL);
        } catch (final MalformedURLException e) {
            throw new IllegalStateException("Failed to parse version manifest url", e);
        }

        this.httpClient = httpClient;
    }

    private <T> T get(final URL url, final Class<T> clazz) throws IOException {
        final Request request = new Request.Builder().url(url).build();

        final Call call = this.httpClient.newCall(request);
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
