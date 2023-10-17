package com.shanebeestudios.mcdeop.processor;

import de.timmi6790.launchermeta.data.release.DownloadInfo;
import de.timmi6790.launchermeta.data.release.ReleaseManifest;
import de.timmi6790.launchermeta.data.version.Version;
import java.net.URL;
import java.util.Optional;

public record ResourceRequest(ReleaseManifest manifest, SourceType type) {
    public Version getVersion() {
        return this.manifest.getVersion();
    }

    public Optional<URL> getJar() {
        return Optional.ofNullable(
                switch (this.type) {
                    case SERVER -> this.manifest.getDownloads().getServer().getUrl();
                    case CLIENT -> this.manifest.getDownloads().getClient().getUrl();
                });
    }

    public Optional<URL> getMappings() {
        return switch (this.type) {
            case SERVER -> this.manifest.getDownloads().getServerMappings().map(DownloadInfo::getUrl);
            case CLIENT -> this.manifest.getDownloads().getClientMappings().map(DownloadInfo::getUrl);
        };
    }
}
