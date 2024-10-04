package com.shanebeestudios.mcdeob;

import com.shanebeestudios.mcdeob.app.App;
import com.shanebeestudios.mcdeob.util.Logger;
import com.shanebeestudios.mcdeob.util.Util;
import com.shanebeestudios.mcdeob.version.Version;
import com.shanebeestudios.mcdeob.version.Versions;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import javax.swing.*;
import java.io.IOException;

public class McDeob {

    public static void main(String[] args) {
        Util.printSystemInfo();
        if (args.length == 0) {
            try {
                if (Util.isRunningMacOS()) {
                    System.setProperty("apple.awt.application.appearance", "system");
                } else {
                    // makes the window prettier on other systems than macs
                    // swing's look and feel is ew
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }

            new App();
            return;
        }

        OptionParser parser = new OptionParser();
        parser.accepts("help", "Shows help and exits");
        parser.accepts("versions", "Prints a list of all Minecraft versions available to deobfuscate");
        parser.accepts("releases", "Prints a list of all Minecraft full releases available to deobfuscate");
        parser.accepts("snapshots", "Prints a list of all Minecraft snapshots available to deobfuscate");
        parser.accepts("version", "Minecraft version for which we're deobfuscating")
            .withRequiredArg()
            .ofType(String.class);
        parser.accepts("type", "What we should deobfuscate: client or server")
            .withRequiredArg()
            .ofType(String.class);
        parser.accepts("decompile", "Marks that we should decompile the deobfuscated source");

        OptionSet options = null;
        try {
            options = parser.parse(args);
        } catch (OptionException ex) {
            Logger.warn("Failed to parse arguments, available arguments:");
        }
        if (options == null || options.has("help")) {
            try {
                parser.printHelpOn(System.out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        }

        // Initialize versions after help might have been used
        Versions.initVersions();
        if (options.has("versions")) {
            System.out.println("Available Minecraft versions to deobfuscate:");
            for (Version version : Versions.getAllVersions()) {
                System.out.println(" - " + version.getVersion() + " (" + version.getReleaseType().getName() + ")");
            }
            System.exit(0);
        }
        if (options.has("releases")) {
            System.out.println("Available Minecraft full releases to deobfuscate:");
            for (Version version : Versions.getReleaseVersions()) {
                System.out.println(" - " + version.getVersion());
            }
            System.exit(0);
        }
        if (options.has("snapshots")) {
            System.out.println("Available Minecraft snapshots to deobfuscate:");
            for (Version version : Versions.getSnapshotVersions()) {
                System.out.println(" - " + version.getVersion());
            }
            System.exit(0);
        }

        if (!options.has("version")) {
            Logger.error("No version specified, shutting down...");
            Logger.warn("See '-versions' for available versions");
            System.exit(1);
        }

        if (!options.has("type")) {
            Logger.error("No type specified, shutting down...");
            Logger.warn("Please use '-type' and 'server' or 'client'.");
            System.exit(1);
        }

        String versionString = (String) options.valueOf("version");
        String typeString = (String) options.valueOf("type");

        Version.Type type;
        try {
            type = Version.Type.valueOf(typeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            Logger.error("Invalid type specified: '%s'", typeString);
            Logger.warn("Please use '-type' and 'server' or 'client'.");
            System.exit(1);
            return;
        }

        Version version = Versions.getByVersion(versionString);
        if (version == null) {
            Logger.error("Invalid or unsupported version was specified: '%s'", versionString);
            Logger.warn("See '-versions' for available versions");
            System.exit(1);
        }
        version.setType(type);

        boolean decompile = options.has("decompile");

        Thread processorThread = new Thread(() -> {
            Processor processor = new Processor(version, decompile, null);
            processor.init();
        }, "Processor");
        processorThread.start();
    }

}
