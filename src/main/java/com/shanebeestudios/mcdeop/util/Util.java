package com.shanebeestudios.mcdeop.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.ref.WeakReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Util {
    public static boolean isRunningMacOS() {
        return System.getProperty("os.name").contains("Mac OS");
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
