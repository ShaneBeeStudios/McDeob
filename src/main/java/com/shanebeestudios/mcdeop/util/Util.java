package com.shanebeestudios.mcdeop.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Util {
    public static boolean isRunningMacOS() {
        return System.getProperty("os.name").contains("Mac OS");
    }
}
