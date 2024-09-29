package com.shanebeestudios.mcdeob;

import com.shanebeestudios.mcdeob.app.App;
import com.shanebeestudios.mcdeob.util.Logger;
import com.shanebeestudios.mcdeob.util.Util;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import javax.swing.*;
import java.io.IOException;

public class McDeob {

    public static void main(String[] args) {
        Version.initVersions();
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
        parser.accepts("version", "Minecraft version for which we're deobfuscating")
            .withRequiredArg()
            .ofType(String.class);
        parser.accepts("type", "What we should deobfuscate: client or server")
            .withRequiredArg()
            .ofType(String.class);
        parser.accepts("decompile", "Marks that we should decompile the deobfuscated source");

        OptionSet options = parser.parse(args);
        if (options.has("help")) {
            try {
                parser.printHelpOn(System.out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        }

        if (options.has("versions")) {
            System.out.println("Available Minecraft versions to deobfuscate:");
            for (Version version : Version.getVersions()) {
                System.out.println(" - " + version.getVersion());
            }
            System.exit(0);
        }

        if (!options.has("version")) {
            Logger.error("No version specified, shutting down...");
            System.exit(1);
        }

        if (!options.has("type")) {
            Logger.error("No type specified, shutting down...");
            System.exit(1);
        }

        String versionString = (String) options.valueOf("version");
        String typeString = (String) options.valueOf("type");

        Version.Type type;
        try {
            type = Version.Type.valueOf(typeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            Logger.error("Invalid type specified, shutting down...");
            System.exit(1);
            return;
        }

        Version version = Version.getByVersion(versionString);
        if (version == null) {
            Logger.error("Invalid or unsupported version was specified, shutting down...");
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
