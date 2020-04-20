package com.shanebeestudios.mcdeop;

public class Main {

    public static void main(String[] args) {
        Thread.currentThread().setName("McDeob");
        if (args.length == 2) {
            String ver = args[0];
            String t = args[1];
            if (!t.equalsIgnoreCase("server") && !t.equalsIgnoreCase("client")) {
                Logger.info("Type: " + t + " not recognized... please use 'server' or 'client'");
            }
            Version.Type type = Version.Type.valueOf(t.toUpperCase());
            Version version = Version.getByVersion(ver, type);
            if (version == null) {
                Logger.info("Version " + ver + " is not available!!!");
                printVersions();
                return;
            }
            Logger.info("Loading version: " + version.getVersion());
            McDeob mcDeob = new McDeob(version);
            mcDeob.init();
        } else {
            Logger.info("Please enter a version (ex: 1.15.2) and type (server or client)!");
            printVersions();
        }
    }

    private static void printVersions() {
        Logger.info("Available versions:");
        for (Version vers : Version.values()) {
            Logger.info(" - " + vers.getVersion() + " (" + vers.getType().toString().toLowerCase() + ")");
        }
    }

}
