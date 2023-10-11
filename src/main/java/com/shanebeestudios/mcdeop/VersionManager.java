package com.shanebeestudios.mcdeop;

import com.shanebeestudios.mcdeop.launchermeta.LauncherMetaManager;
import com.shanebeestudios.mcdeop.launchermeta.data.release.ReleaseManifest;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VersionManager {
    private static final OffsetDateTime MINIMUM_RELEASE_TIME = OffsetDateTime.parse("2019-08-28T15:00:00Z");
    private static final Set<String> SPECIAL_VERSIONS = Set.of("1.14.4");

    private final LauncherMetaManager launcherMetaManager = new LauncherMetaManager();

    @Getter(lazy = true)
    private final List<Version> versions = this.fetchVersions();

    private boolean hasMappings(final Version version) {
        return version.getReleaseTime().isAfter(MINIMUM_RELEASE_TIME) || SPECIAL_VERSIONS.contains(version.getId());
    }

    private List<Version> fetchVersions() {
        final List<Version> fetchedVersions;
        try {
            fetchedVersions = this.launcherMetaManager.getVersionManifest().getVersions();
        } catch (final IOException e) {
            log.error("Failed to fetch version manifest", e);
            return List.of();
        }

        // Remove versions without mappings
        fetchedVersions.removeIf(version -> !this.hasMappings(version));

        // Sort versions after time
        fetchedVersions.sort((o1, o2) -> o2.getReleaseTime().compareTo(o1.getReleaseTime()));

        return fetchedVersions;
    }

    public Optional<Version> getVersion(final String id) {
        return this.getVersion(version -> version.getId().equals(id));
    }

    public Optional<Version> getVersion(final Predicate<Version> predicate) {
        for (final Version version : this.getVersions()) {
            if (predicate.test(version)) {
                return Optional.ofNullable(version);
            }
        }

        return Optional.empty();
    }

    public ReleaseManifest getReleaseManifest(final Version version) throws IOException {
        return this.launcherMetaManager.getReleaseManifest(version);
    }
}
