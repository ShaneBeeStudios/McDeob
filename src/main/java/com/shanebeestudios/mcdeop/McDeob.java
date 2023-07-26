package com.shanebeestudios.mcdeop;

import com.shanebeestudios.mcdeop.app.App;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import com.shanebeestudios.mcdeop.processor.Processor;
import com.shanebeestudios.mcdeop.processor.ProcessorOptions;
import com.shanebeestudios.mcdeop.processor.ResourceRequest;
import com.shanebeestudios.mcdeop.processor.SourceType;
import com.shanebeestudios.mcdeop.util.Util;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;

@Slf4j
public class McDeob {
    public static String getVersion() {
        String version = McDeob.class.getPackage().getImplementationVersion();

        // The version is not available when running in an IDE
        if (version == null) {
            version = "0.0.0";
        }

        return version;
    }

    public static void main(final String[] args) {
        if (args.length == 0) {
            try {
                if (Util.isRunningMacOS()) {
                    System.setProperty("apple.awt.application.appearance", "system");
                } else {
                    // makes the window prettier on other systems than macs
                    // swing's look and feel is ew
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
            } catch (ClassNotFoundException
                    | InstantiationException
                    | IllegalAccessException
                    | UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }

            new App();
            return;
        }

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

        final OptionSet options = parser.parse(args);
        if (options.has("help")) {
            try {
                parser.printHelpOn(System.out);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        }

        if (options.has("versions")) {
            System.out.println("Available Minecraft versions to deobfuscate:");
            for (final Version version : VersionManager.getInstance().getVersions()) {
                System.out.println(" - " + version.getId());
            }
            System.exit(0);
        }

        if (!options.has("version")) {
            log.error("No version specified, shutting down...");
            System.exit(1);
        }

        if (!options.has("type")) {
            log.error("No type specified, shutting down...");
            System.exit(1);
        }

        final String versionString = (String) options.valueOf("version");
        final String typeString = (String) options.valueOf("type");

        final SourceType type;
        try {
            type = SourceType.valueOf(typeString.toUpperCase());
        } catch (final IllegalArgumentException e) {
            log.error("Invalid type specified, shutting down...");
            System.exit(1);
            return;
        }

        VersionManager.getInstance()
                .getVersion(versionString)
                .map(version -> {
                    try {
                        return VersionManager.getInstance().getReleaseManifest(version);
                    } catch (final IOException e) {
                        log.error("Failed to fetch release manifest", e);
                    }

                    return null;
                })
                .map(manifest -> new ResourceRequest(manifest, type))
                .ifPresentOrElse(
                        request -> {
                            final boolean remap = options.has("remap");
                            final boolean decompile = options.has("decompile");
                            final boolean zip = options.has("zip");
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
