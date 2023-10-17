package de.timmi6790.launchermeta.data.release;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.timmi6790.launchermeta.data.version.Version;
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
