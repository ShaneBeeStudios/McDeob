package com.shanebeestudios.mcdeop;

import com.shanebeestudios.mcdeop.app.App;
import com.shanebeestudios.mcdeop.util.Logger;
import com.shanebeestudios.mcdeop.util.TimeStamp;
import com.shanebeestudios.mcdeop.util.Util;
import io.github.lxgaming.reconstruct.common.Reconstruct;
import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Processor {

    private final Version version;
    private final boolean decompile;
    private final App app;

    private Reconstruct reconstruct;
    private File JAR_FILE;
    private File MAPPINGS_FILE;
    private File REMAPPED_JAR;
    private String MAPPINGS_URL;
    private String JAR_URL;

    private String MINECRAFT_JAR_NAME;
    private String MAPPINGS_NAME;
    private String MAPPED_JAR_NAME;

    private Path DATA_FOLDER_PATH = Paths.get(".", "deobf-work");

    public Processor(Version version, boolean decompile, App app) {
        this.version = version;
        this.app = app;
        if (Util.isRunningMacOS()) {
            // If running on macOS, put the decompile folder in the user's home folder
            // This is mainly due to how the Mac APP works
            DATA_FOLDER_PATH = Paths.get(System.getProperty("user.home"), "McDeob");
        }
        if (Files.notExists(DATA_FOLDER_PATH)) {
            try {
                Files.createDirectory(DATA_FOLDER_PATH);
            } catch (IOException ignore) {
            }
        }

        MINECRAFT_JAR_NAME = String.format("minecraft_%s_%s.jar", version.getType().getName(), version.getVersion());
        MAPPINGS_NAME = String.format("mappings_%s_%s.txt", version.getType().getName(), version.getVersion());
        MAPPED_JAR_NAME = String.format("remapped_%s_%s.jar", version.getType().getName(), version.getVersion());
        JAR_URL = version.getJar();
        MAPPINGS_URL = version.getMappings();
        this.decompile = decompile;
        this.reconstruct = new Reconstruct(new ReconConfig());
    }

    public void init() {
        try {
            long start = System.currentTimeMillis();
            if (app != null) {
                app.toggleControls();
            }

            String trueVersion;
            if (version.isLatest()) {
                trueVersion = prepareLatest();
            } else {
                trueVersion = version.getVersion();
            }
            if (app != null) {
                app.updateVerBox(trueVersion);
            }
            downloadJar();
            downloadMappings();
            remapJar();
            if (decompile) {
                decompileJar();
            }
            cleanup();

            TimeStamp timeStamp = TimeStamp.fromNow(start);
            Logger.info("Process finished in %s!", timeStamp);
            if (app != null) {
                app.updateStatusBox(String.format("Completed in %s!", timeStamp));
                app.updateButton("Start!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (app != null) {
                app.toggleControls();
            }
        }
    }

    public void downloadJar() throws IOException {
        long start = System.currentTimeMillis();
        Logger.info("Downloading jar file from Mojang.");
        if (app != null) {
            app.updateStatusBox("Downloading jar");
            app.updateButton("Downloading jar", Color.BLUE);
        }
        JAR_FILE = new File(DATA_FOLDER_PATH.toString(), MINECRAFT_JAR_NAME);
        if (JAR_FILE.exists()) {
            JAR_FILE.delete();
        }
        JAR_FILE.createNewFile();
        URL jar_url = new URL(JAR_URL);
        OutputStream jar_out = new BufferedOutputStream(new FileOutputStream(JAR_FILE));
        URLConnection connection = jar_url.openConnection();
        InputStream inputStream = connection.getInputStream();
        byte[] buffer = new byte[1024];

        int numRead;
        while ((numRead = inputStream.read(buffer)) != -1) {
            jar_out.write(buffer, 0, numRead);
        }
        inputStream.close();
        jar_out.close();

        TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Successfully downloaded jar file in %s", timeStamp);
    }

    public void downloadMappings() throws IOException {
        long start = System.currentTimeMillis();
        Logger.info("Downloading mappings file from Mojang.");
        if (app != null) {
            app.updateStatusBox("Downloading mappings");
            app.updateButton("Downloading mappings", Color.BLUE);
        }
        MAPPINGS_FILE = new File(DATA_FOLDER_PATH.toString(), MAPPINGS_NAME);
        if (MAPPINGS_FILE.exists()) {
            MAPPINGS_FILE.delete();
        }
        MAPPINGS_FILE.createNewFile();

        URL mapping_url = new URL(MAPPINGS_URL);
        OutputStream mapping_out = new BufferedOutputStream(new FileOutputStream(MAPPINGS_FILE));
        URLConnection connection = mapping_url.openConnection();
        InputStream inputStream = connection.getInputStream();
        byte[] buffer = new byte[1024];

        int numRead;
        while ((numRead = inputStream.read(buffer)) != -1) {
            mapping_out.write(buffer, 0, numRead);
        }
        inputStream.close();
        mapping_out.close();

        TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Successfully downloaded mappings file in %s", timeStamp);
    }

    public void remapJar() {
        long start = System.currentTimeMillis();
        if (app != null) {
            app.updateStatusBox("Remapping...");
            app.updateButton("Remapping...", Color.BLUE);
        }
        REMAPPED_JAR = new File(DATA_FOLDER_PATH.toString(), MAPPED_JAR_NAME);

        if (!REMAPPED_JAR.exists()) {
            Logger.info("Remapping %s file...", MINECRAFT_JAR_NAME);
            reconstruct.getConfig().setInputPath(JAR_FILE.getAbsoluteFile().toPath());
            reconstruct.getConfig().setMappingPath(MAPPINGS_FILE.getAbsoluteFile().toPath());
            reconstruct.getConfig().setOutputPath(REMAPPED_JAR.getAbsoluteFile().toPath());
            reconstruct.load();

            TimeStamp timeStamp = TimeStamp.fromNow(start);
            Logger.info("Remapping completed in %s", timeStamp);
        } else {
            Logger.info("%s already remapped... skipping mapping!", MAPPED_JAR_NAME);
        }
    }

    public void decompileJar() {
        long start = System.currentTimeMillis();
        Logger.info("Decompiling final jar file.");
        if (app != null) {
            app.updateStatusBox("Decompiling... This will take a while!");
            app.updateButton("Decompiling...", Color.BLUE);
        }
        File DIR = new File(DATA_FOLDER_PATH.toString(), "final-decompile");
        if (!DIR.exists()) {
            DIR.mkdirs();
        }
        // Setup FernFlower to properly decompile the jar file
        String[] args = new String[]{"-dgs=1", "-hdc=0", "-rbr=0", "-asc=1", "-udv=0", REMAPPED_JAR.getAbsolutePath(), DIR.getAbsolutePath()};
        ConsoleDecompiler.main(args);

        // Rename jar file to zip
        for (File file : Objects.requireNonNull(DIR.listFiles())) {
            int i = file.getName().lastIndexOf(".");
            String name = file.getName().substring(0, i);
            file.renameTo(new File(file.getParentFile(), name + ".zip"));
        }

        TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Decompiling completed in %s", timeStamp);
    }

    public String prepareLatest() throws IOException {
        if (app != null) {
            app.updateStatusBox("Fetching version list from mojang...");
            app.updateButton("Fetching...");
        }

        URL latestInfo = new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        URLConnection connection = latestInfo.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        JSONObject versions = new JSONObject(response.toString());
        if (!versions.has("latest")) {
            String s = "Failed! Could not locate the latest data from the downloaded manifest file!";
            Logger.error(s);
            if (app != null) {
                app.updateStatusBox(s);
                app.updateButton("Exit");
            }
            throw new InvalidObjectException(s);
        }

        String type = version == Version.SERVER_LATEST_RELEASE || version == Version.CLIENT_LATEST_RELEASE ? "release" : "snapshot";

        String trueVersion = versions.getJSONObject("latest").getString(type);
        Logger.info("Finding the url for " + trueVersion);
        if (app != null) {
            app.updateStatusBox("Finding the url for " + trueVersion);
        }

        String versionURL = null;

        JSONArray versionArray = versions.getJSONArray("versions");
        for (int i = 0; i < versionArray.length(); i++) {
            JSONObject innerVersion = versionArray.getJSONObject(i);

            String id = innerVersion.getString("id");
            if (id.equalsIgnoreCase(trueVersion)) {
                versionURL = innerVersion.getString("url");
                break;
            }
        }

        if (versionURL == null) {
            String s = "Failed! Could not find information for version " + trueVersion;
            Logger.error(s);
            if (app != null) {
                app.updateStatusBox(s);
                app.updateButton("Exit");
            }
            throw new InvalidObjectException(s);
        }

        Logger.info("Download data for " + trueVersion + "...");
        if (app != null) {
            app.updateStatusBox("Download data for " + trueVersion + "...");
        }

        connection = new URL(versionURL).openConnection();
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        JSONObject versionManifest = new JSONObject(response.toString());
        JSONObject downloads = versionManifest.getJSONObject("downloads");
        String clientOrServer = version.getType().getName().toLowerCase();
        JAR_URL = downloads.getJSONObject(clientOrServer).getString("url");
        MAPPINGS_URL = downloads.getJSONObject(clientOrServer + "_mappings").getString("url");

        MINECRAFT_JAR_NAME = String.format("minecraft_%s_%s.jar", clientOrServer, trueVersion);
        MAPPINGS_NAME = String.format("mappings_%s_%s.txt", clientOrServer, trueVersion);
        MAPPED_JAR_NAME = String.format("remapped_%s_%s.jar", clientOrServer, trueVersion);

        Logger.info("Found the jar and mappings url for " + trueVersion + "!");
        if (app != null) {
            app.updateStatusBox("Found the jar and mappings url for " + trueVersion + "!");
        }
        return trueVersion;
    }

    private void cleanup() {
        JAR_FILE = null;
        MAPPINGS_FILE = null;
        REMAPPED_JAR = null;
        reconstruct = null;
        System.gc();
    }

}
