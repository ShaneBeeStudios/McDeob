package com.shanebeestudios.mcdeob.util;

public class Util {

    public static boolean isRunningMacOS() {
        return System.getProperty("os.name").contains("Mac OS");
    }

}
