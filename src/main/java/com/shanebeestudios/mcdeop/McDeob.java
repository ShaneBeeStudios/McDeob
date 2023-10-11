package com.shanebeestudios.mcdeop;

import com.shanebeestudios.mcdeop.app.App;
import com.shanebeestudios.mcdeop.util.Util;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class McDeob {
    public static void main(final String[] args) {
        if (args.length == 0) {
            startGUI();
        } else {
            new CommandLineHandler(args).run();
        }
    }

    public static String getVersion() {
        String version = McDeob.class.getPackage().getImplementationVersion();

        // The version is not available when running in an IDE
        if (version == null) {
            version = "0.0.0";
        }

        return version;
    }

    private static void startGUI() {
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
    }
}
