package com.shanebeestudios.mcdeop.util;

import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Util {
    public static boolean isRunningMacOS() {
        return System.getProperty("os.name").contains("Mac OS");
    }

    public static Path getBaseDataFolder() {
        if (Util.isRunningMacOS()) {
            // If running on macOS, put the output directory in the user home directory.
            // This is due to how macOS APPs work — their '.' directory resolves to one inside the APP itself.
            return Paths.get(System.getProperty("user.home"), "McDeob");
        } else {
            return Paths.get("versions");
        }
    }

    // https://stackoverflow.com/a/6915221
    public static void forceGC() {
        Object obj = new Object();
        final WeakReference<Object> ref = new WeakReference<>(obj);
        obj = null;
        while (ref.get() != null) {
            System.gc();
        }
    }
}
