package com.shanebeestudios.mcdeop;

import com.beust.jcommander.JCommander;
import io.github.lxgaming.reconstruct.Reconstruct;
import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Processor {

    private final Version version;
    private final Reconstruct reconstruct;
    private File JAR_FILE;
    private File MAPPINGS_FILE;

    private final String MINECRAFT_JAR_NAME;
    private final String MAPPINGS_NAME;
    private final String MAPPED_JAR_NAME;

    public Processor(Version version) {
        this.version = version;
        MINECRAFT_JAR_NAME = "minecraft_" + version.getType().getName() + "_" + version.getVersion() + ".jar";
        MAPPINGS_NAME = "mappings_" + version.getType().getName() + "_" + version.getVersion() + ".txt";
        MAPPED_JAR_NAME = "remapped_" + version.getType().getName() + "_" + version.getVersion() + ".jar";
        this.reconstruct = new Reconstruct();
    }

    public void init() {
        try {
            long start = System.currentTimeMillis();
            downloadJar();
            downloadMappings();
            remapJar();
            Scanner scanner = new Scanner(System.in);
            Logger.info("Do you want to decompile? Type yes to continue!");
            if (scanner.next().equalsIgnoreCase("yes")) {
                decompileJar();
            } else {
                Logger.info("Ok.... fine then.... I didn't want to do it anyways!");
                Logger.info("Please enjoy your new, deobfuscated Minecraft jar.");
            }
            long finish = System.currentTimeMillis() - start;
            Logger.info("Process finished in " + finish + " milliseconds!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadJar() throws IOException {
        long start = System.currentTimeMillis();
        Logger.info("Downloading jar file from Mojang.");
        JAR_FILE = new File(MINECRAFT_JAR_NAME);
        if (JAR_FILE.exists()) {
            JAR_FILE.delete();
        }
        JAR_FILE.createNewFile();
        URL jar_url = new URL(version.getJar());
        OutputStream jar_out = new BufferedOutputStream(new FileOutputStream(MINECRAFT_JAR_NAME));
        URLConnection connection = jar_url.openConnection();
        InputStream inputStream = connection.getInputStream();
        byte[] buffer = new byte[1024];

        int numRead;
        while ((numRead = inputStream.read(buffer)) != -1) {
            jar_out.write(buffer, 0, numRead);
        }
        inputStream.close();
        jar_out.close();
        long finish = System.currentTimeMillis() - start;
        Logger.info("Successfully downloaded jar file in " + finish + " milliseconds");
    }

    public void downloadMappings() throws IOException {
        long start = System.currentTimeMillis();
        Logger.info("Downloading mappings file from Mojang.");
        MAPPINGS_FILE = new File(MAPPINGS_NAME);
        if (MAPPINGS_FILE.exists()) {
            MAPPINGS_FILE.delete();
        }
        MAPPINGS_FILE.createNewFile();
        URL mapping_url = new URL(version.getMappings());
        OutputStream mapping_out = new BufferedOutputStream(new FileOutputStream(MAPPINGS_NAME));
        URLConnection connection = mapping_url.openConnection();
        InputStream inputStream = connection.getInputStream();
        byte[] buffer = new byte[1024];

        int numRead;
        while ((numRead = inputStream.read(buffer)) != -1) {
            mapping_out.write(buffer, 0, numRead);
        }
        inputStream.close();
        mapping_out.close();
        long finish = System.currentTimeMillis() - start;
        Logger.info("Successfully downloaded mappings file in " + finish + " milliseconds");
    }

    public void remapJar() {
        long start = System.currentTimeMillis();
        File REMAPPED_JAR = new File(MAPPED_JAR_NAME);

        if (!REMAPPED_JAR.exists()) {
            Logger.info("Remapping " + MINECRAFT_JAR_NAME + " file...");
            String[] clientArgs = new String[]{"-jar", JAR_FILE.getAbsolutePath(), "-mapping", MAPPINGS_FILE.getAbsolutePath(), "-output", MAPPED_JAR_NAME};
            String[] serverArgs = new String[]{"-jar", JAR_FILE.getAbsolutePath(), "-mapping", MAPPINGS_FILE.getAbsolutePath(), "-output", MAPPED_JAR_NAME,
                    "-exclude", "com.google.,io.netty.,it.unimi.dsi.fastutil.,javax.,joptsimple.,org.apache."};
            try {
                JCommander.newBuilder()
                        .addObject(reconstruct.getArguments())
                        .build()
                        .parse(version.getType() == Version.Type.SERVER ? serverArgs : clientArgs);
            } catch (Exception ex) {
                reconstruct.getLogger().error("Encountered an error while parsing arguments", ex);
                Runtime.getRuntime().exit(-1);
                return;
            }
            reconstruct.load();
            long finish = System.currentTimeMillis() - start;
            Logger.info("Remapping completed in " + finish + " milliseconds");
        } else {
            Logger.info(MAPPED_JAR_NAME + " already remapped... skipping mapping!");
        }
    }

    public void decompileJar() {
        long start = System.currentTimeMillis();
        Logger.info("Decompiling final jar file.");
        File REMAPPED_JAR = new File(MAPPED_JAR_NAME);
        File DIR = new File(REMAPPED_JAR.getAbsoluteFile().getParent() + "/final-decompile");
        if (!DIR.exists()) {
            DIR.mkdirs();
        }
        // Setup FernFlower to properly decompile the jar file
        String[] args = new String[] {"-dgs=1", "-hdc=0", "-rbr=0", "-asc=1", "-udv=0", REMAPPED_JAR.getAbsolutePath(), DIR.getAbsolutePath()};
        ConsoleDecompiler.main(args);
        long finish = System.currentTimeMillis() - start;
        Logger.info("Decompiling completed in " + finish + " milliseconds");
    }

}
