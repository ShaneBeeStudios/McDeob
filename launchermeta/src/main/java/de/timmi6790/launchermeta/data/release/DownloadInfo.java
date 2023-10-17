package de.timmi6790.launchermeta.data.release;

import java.net.URL;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public class DownloadInfo {
    private final String sha1;
    private final long size;
    private final URL url;
}
