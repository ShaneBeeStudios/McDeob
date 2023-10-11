package com.shanebeestudios.mcdeop.launchermeta.data.release;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseManifest {
    private final Downloads downloads;
    private final String mainClass;
    private Version version;
}
