package com.shanebeestudios.mcdeop.launchermeta.data.release;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Downloads {
    private final DownloadInfo client;

    @JsonProperty("client_mappings")
    private final DownloadInfo clientMappings;

    private final DownloadInfo server;

    @JsonProperty("server_mappings")
    private final DownloadInfo serverMappings;

    public Optional<DownloadInfo> getClientMappings() {
        return Optional.ofNullable(this.clientMappings);
    }

    public Optional<DownloadInfo> getServerMappings() {
        return Optional.ofNullable(this.serverMappings);
    }
}
