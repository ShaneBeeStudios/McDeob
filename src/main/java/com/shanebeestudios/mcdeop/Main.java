package com.shanebeestudios.mcdeop;

public class Main {

    public static void main(String[] args) {
        Thread.currentThread().setName("McDeob");
        if (args.length == 1) {
            String ver = args[0];
            Version version = Version.getByVersion(ver);
            if (version == null) {
                System.exit(0);
            }
            Logger.info("Loading version: " + version.getVersion());
            McDeob mcDeob = new McDeob(version);
            mcDeob.init();
        } else {
            Logger.info("Please enter a version!");
            System.exit(0);
        }
    }

}
