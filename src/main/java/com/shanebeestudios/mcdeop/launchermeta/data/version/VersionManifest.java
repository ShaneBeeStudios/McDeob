package com.shanebeestudios.mcdeop.launchermeta.data.version;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public class VersionManifest {
    private final Latest latest;
    private final List<Version> versions;

    @Data
    @Jacksonized
    @Builder
    public static class Latest {
        private final String release;
        private final String snapshot;
    }
}
