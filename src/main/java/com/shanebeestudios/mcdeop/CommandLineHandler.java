package com.shanebeestudios.mcdeop;

import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import com.shanebeestudios.mcdeop.processor.Processor;
import com.shanebeestudios.mcdeop.processor.ProcessorOptions;
import com.shanebeestudios.mcdeop.processor.ResourceRequest;
import com.shanebeestudios.mcdeop.processor.SourceType;
import java.io.IOException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandLineHandler {
    private final OptionParser parser;
    private final OptionSet options;

    public CommandLineHandler(final String[] args) {
        this.parser = this.createOptionsParser();
        this.options = this.parser.parse(args);
    }

    private OptionParser createOptionsParser() {
        final OptionParser parser = new OptionParser();
        parser.accepts("help", "Shows help and exits");
        parser.accepts("versions", "Prints a list of all Minecraft versions available to deobfuscate");
        parser.accepts("version", "Minecraft version for which we're deobfuscating")
                .withRequiredArg()
                .ofType(String.class);
        parser.accepts("type", "What we should deobfuscate: client or server")
                .withRequiredArg()
                .ofType(String.class);
        parser.accepts("remap", "Marks that we should remap the deobfuscated source");
        parser.accepts("decompile", "Marks that we should decompile the deobfuscated source");
        parser.accepts("zip", "Marks that we should zip the decompiled source");

        return parser;
    }

    private boolean verifyOptions() {
        if (this.options.has("help")) {
            try {
                this.parser.printHelpOn(System.out);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
            return false;
        }

        if (this.options.has("versions")) {
            System.out.println("Available Minecraft versions to deobfuscate:");
            final VersionManager versionManager = new VersionManager();
            for (final Version version : versionManager.getVersions()) {
                System.out.println(" - " + version.getId());
            }
            return false;
        }

        if (!this.options.has("version")) {
            log.error("No version specified, shutting down...");
            return false;
        }

        if (!this.options.has("type")) {
            log.error("No type specified, shutting down...");
            return false;
        }

        return true;
    }

    public void run() {
        if (!this.verifyOptions()) {
            return;
        }

        final String versionString = (String) this.options.valueOf("version");
        final String typeString = (String) this.options.valueOf("type");

        final SourceType type;
        try {
            type = SourceType.valueOf(typeString.toUpperCase());
        } catch (final IllegalArgumentException e) {
            log.error("Invalid type specified, shutting down...");
            System.exit(1);
            return;
        }

        final VersionManager versionManager = new VersionManager();
        versionManager
                .getVersion(versionString)
                .map(version -> {
                    try {
                        return versionManager.getReleaseManifest(version);
                    } catch (final IOException e) {
                        log.error("Failed to fetch release manifest", e);
                    }

                    return null;
                })
                .map(manifest -> new ResourceRequest(manifest, type))
                .ifPresentOrElse(
                        request -> {
                            final boolean remap = this.options.has("remap");
                            final boolean decompile = this.options.has("decompile");
                            final boolean zip = this.options.has("zip");
                            final ProcessorOptions processorOptions = ProcessorOptions.builder()
                                    .remap(remap)
                                    .decompile(decompile)
                                    .zipDecompileOutput(zip)
                                    .build();

                            final Thread processorThread = new Thread(
                                    () -> Processor.runProcessor(request, processorOptions, null), "Processor");
                            processorThread.start();
                        },
                        () -> {
                            log.error("Invalid or unsupported version was specified, shutting down...");
                            System.exit(1);
                        });
    }
}
