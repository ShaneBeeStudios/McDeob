package com.shanebeestudios.mcdeop;

import com.shanebeestudios.mcdeop.launchermeta.LauncherMetaManager;
import com.shanebeestudios.mcdeop.launchermeta.data.release.ReleaseManifest;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import com.shanebeestudios.mcdeop.launchermeta.data.version.VersionType;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Slf4j
public class VersionManager {
    private static final OffsetDateTime MINIMUM_RELEASE_TIME = OffsetDateTime.parse("2019-08-28T15:00:00Z");
    private static final Set<String> SPECIAL_VERSIONS = Set.of("1.14.4");

    public static boolean hasMappings(final Version version) {
        return version.getReleaseTime().isAfter(MINIMUM_RELEASE_TIME) || SPECIAL_VERSIONS.contains(version.getId());
    }

    private static final VersionManager INSTANCE = new VersionManager();

    public static VersionManager getInstance() {
        return INSTANCE;
    }

    private final LauncherMetaManager launcherMetaManager = new LauncherMetaManager();

    private boolean fetched = false;
    private final List<Version> versions = new ArrayList<>();

    private void fetch() {
        if (this.fetched) {
            return;
        }

        final List<Version> versions;
        try {
            versions = this.launcherMetaManager.getVersionManifest().getVersions();
        } catch (final IOException e) {
            log.error("Failed to fetch version manifest", e);
            return;
        }

        for (final Version version : versions) {
            if (!hasMappings(version)) {
                continue;
            }

            this.versions.add(version);
        }
        this.versions.sort((o1, o2) -> o2.getReleaseTime().compareTo(o1.getReleaseTime()));

        this.fetched = true;
    }

    public List<Version> getVersions() {
        this.fetch();
        return this.versions;
    }

    public Optional<Version> getLatestVersion() {
        return this.getVersion(version -> true);
    }

    public Optional<Version> getLatestRelease() {
        return this.getVersion(version -> version.getType() == VersionType.RELEASE);
    }

    public Optional<Version> getLatestSnapshot() {
        return this.getVersion(version -> version.getType() == VersionType.SNAPSHOT);
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
