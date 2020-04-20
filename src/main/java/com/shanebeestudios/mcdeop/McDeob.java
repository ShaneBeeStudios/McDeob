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

@SuppressWarnings("ResultOfMethodCallIgnored")
public class McDeob {

    private final Version version;
    private final Reconstruct reconstruct;
    private File JAR_FILE;
    private File MAPPINGS_FILE;

    private final String MINECRAFT_JAR_NAME;
    private final String MAPPINGS_NAME;
    private final String MAPPED_JAR_NAME;

    public McDeob(Version version) {
        this.version = version;
        String type = version.isServer() ? "server" : "client";
        MINECRAFT_JAR_NAME = "minecraft_" + type + "_" + version.getVersion() + ".jar";
        MAPPINGS_NAME = "mappings_" + type + "_" + version.getVersion() + ".txt";
        MAPPED_JAR_NAME = "remapped_" + type;
        this.reconstruct = new Reconstruct();
    }

    public void init() {
        try {
            downloadJar();
            downloadMappings();
            remapJar();
            decompileJar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadJar() throws IOException {
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
        Logger.info("Successfully downloaded jar file!");
    }

    public void downloadMappings() throws IOException {
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
        Logger.info("Successfully downloaded mappings file!");
    }

    public void remapJar() {
        Logger.info("Remapping jar file.");
        String[] clientArgs = new String[] {"-jar", JAR_FILE.getAbsolutePath(), "-mapping", MAPPINGS_FILE.getAbsolutePath(), "-output", "final_product.jar"};
        String[] serverArgs = new String[] {"-jar", JAR_FILE.getAbsolutePath(), "-mapping", MAPPINGS_FILE.getAbsolutePath(), "-output", "final_product.jar",
                "-exclude", "com.google.,io.netty.,it.unimi.dsi.fastutil.,javax.,joptsimple.,org.apache."};
        try {
            JCommander.newBuilder()
                    .addObject(reconstruct.getArguments())
                    .build()
                    .parse(version.isServer() ? serverArgs : clientArgs);
        } catch (Exception ex) {
            reconstruct.getLogger().error("Encountered an error while parsing arguments", ex);
            Runtime.getRuntime().exit(-1);
            return;
        }

        reconstruct.load();
        Logger.info("Remapping complete!");
    }

    public void decompileJar() {
        Logger.info("Decompiling final jar file.");
        File REMAPPED_JAR = new File(MAPPED_JAR_NAME);
        File DIR = new File(REMAPPED_JAR.getAbsoluteFile().getParent() + "/final-decompile");
        if (!DIR.exists()) {
            DIR.mkdirs();
        }
        String[] args = new String[] {"-dgs=1", "-hdc=0", "-rbr=0", "-asc=1", "-udv=0",
                REMAPPED_JAR.getAbsolutePath(), DIR.getAbsolutePath()};
        ConsoleDecompiler.main(args);
        Logger.info("Decompiling complete!");
    }

}
