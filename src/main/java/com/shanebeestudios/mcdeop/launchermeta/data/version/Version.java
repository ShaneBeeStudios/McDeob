package com.shanebeestudios.mcdeop.launchermeta.data.version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.net.URL;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {
    private final String id;
    private final VersionType type;
    private final URL url;
    private final OffsetDateTime releaseTime;
}
