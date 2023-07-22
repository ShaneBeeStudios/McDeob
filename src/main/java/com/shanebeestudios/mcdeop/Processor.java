package com.shanebeestudios.mcdeop;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import com.shanebeestudios.mcdeop.app.App;
import com.shanebeestudios.mcdeop.util.FileUtil;
import com.shanebeestudios.mcdeop.util.Logger;
import com.shanebeestudios.mcdeop.util.TimeStamp;
import com.shanebeestudios.mcdeop.util.Util;
import io.github.lxgaming.reconstruct.common.Reconstruct;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Processor {
    private static final URL LATEST_INFO;

    static {
        try {
            LATEST_INFO = new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        } catch (MalformedURLException e) {
            throw new AssertionError("Impossible");
        }
    }

    private final Version version;
    private final boolean decompile;
    private final App app;

    private Reconstruct reconstruct;
    private Path jarPath;
    private Path mappingsPath;
    private Path remappedJar;
    private String mappingsUrl;
    private String jarUrl;

    private String minecraftJarName;
    private String mappingsName;
    private String mappedJarName;

    private Path dataFolderPath = Paths.get("deobf-work");

    public Processor(Version version, boolean decompile, App app) {
        this.version = version;
        this.app = app;
        if (Util.isRunningMacOS()) {
            // If running on macOS, put the output directory in the user home directory.
            // This is due to how macOS APPs work — their '.' directory resolves to one inside the APP itself.
            dataFolderPath = Paths.get(System.getProperty("user.home"), "McDeob");
        }
        try {
            Files.createDirectories(dataFolderPath);
        } catch (IOException ignore) {
        }

        minecraftJarName =
                String.format("minecraft_%s_%s.jar", version.getType().getName(), version.getVersion());
        mappingsName = String.format("mappings_%s_%s.txt", version.getType().getName(), version.getVersion());
        mappedJarName = String.format("remapped_%s_%s.jar", version.getType().getName(), version.getVersion());
        jarUrl = version.getJar();
        mappingsUrl = version.getMappings();
        this.decompile = decompile;
        this.reconstruct = new Reconstruct(new ReconConfig());
    }

    public void init() {
        try {
            long start = System.currentTimeMillis();
            if (app != null) {
                app.toggleControls();
            }

            if (version.isLatest()) {
                String trueVersion = prepareLatest();
                if (app != null) {
                    app.addVersionBox(trueVersion);
                }
            }

            downloadJar();
            downloadMappings();
            remapJar();
            if (decompile) {
                decompileJar();
            }
            cleanup();

            TimeStamp timeStamp = TimeStamp.fromNow(start);
            Logger.info("Completed in %s!", timeStamp);
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
        Logger.info("Downloading JAR file from Mojang.");
        if (app != null) {
            app.updateStatusBox("Downloading JAR...");
            app.updateButton("Downloading JAR...", Color.BLUE);
        }

        jarPath = dataFolderPath.resolve(minecraftJarName);
        final URL parsedURL = new URL(jarUrl);
        final HttpURLConnection connection = (HttpURLConnection) parsedURL.openConnection();
        final long length = connection.getContentLengthLong();
        if (Files.exists(jarPath) && Files.size(jarPath) == length) {
            Logger.info("Already have JAR, skipping download.");
        } else
            try (final InputStream inputStream = connection.getInputStream()) {
                Files.copy(inputStream, jarPath, REPLACE_EXISTING);
            }

        TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Successfully downloaded JAR file in %s!", timeStamp);
    }

    public void downloadMappings() throws IOException {
        long start = System.currentTimeMillis();
        Logger.info("Downloading mappings file from Mojang...");
        if (app != null) {
            app.updateStatusBox("Downloading mappings...");
            app.updateButton("Downloading mappings...", Color.BLUE);
        }
        final URL parsedURL = new URL(mappingsUrl);
        final HttpURLConnection connection = (HttpURLConnection) parsedURL.openConnection();
        final long length = connection.getContentLengthLong();
        mappingsPath = dataFolderPath.resolve(mappingsName);
        if (Files.exists(mappingsPath) && Files.size(mappingsPath) == length) {
            Logger.info("Already have mappings, skipping download.");
        } else
            try (final InputStream inputStream = connection.getInputStream()) {
                Files.copy(inputStream, mappingsPath, REPLACE_EXISTING);
            }

        TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Successfully downloaded mappings file in %s!", timeStamp);
    }

    public void remapJar() {
        long start = System.currentTimeMillis();
        if (app != null) {
            app.updateStatusBox("Remapping...");
            app.updateButton("Remapping...", Color.BLUE);
        }
        remappedJar = dataFolderPath.resolve(mappedJarName);

        if (!Files.exists(remappedJar)) {
            Logger.info("Remapping %s file...", minecraftJarName);
            reconstruct.getConfig().setInputPath(jarPath.toAbsolutePath());
            reconstruct.getConfig().setMappingPath(mappingsPath.toAbsolutePath());
            reconstruct.getConfig().setOutputPath(remappedJar.toAbsolutePath());
            reconstruct.load();

            TimeStamp timeStamp = TimeStamp.fromNow(start);
            Logger.info("Remapping completed in %s!", timeStamp);
        } else {
            Logger.info("%s already remapped... skipping mapping.", mappedJarName);
        }
    }

    public void decompileJar() throws IOException {
        final long start = System.currentTimeMillis();
        Logger.info("Decompiling final JAR file.");
        if (this.app != null) {
            this.app.updateStatusBox("Decompiling... This will take a while!");
            this.app.updateButton("Decompiling...", Color.BLUE);
        }
        final Path decompileDir = this.dataFolderPath.resolve("final-decompile");
        Files.createDirectories(decompileDir);

        final String cleanJarName = this.remappedJar.getFileName().toString().replace(".jar", "");
        final Path decompileJarDir = decompileDir.resolve(cleanJarName);
        FileUtil.remove(decompileJarDir);
        Files.createDirectories(decompileJarDir);

        // Setup FernFlower to properly decompile the jar file
        final String[] args = {
            "-asc=1", // Encode non-ASCII characters in string and character literals as Unicode escapes
            "-tcs=1", // Simplify boolean constants in ternary operations
            "-jvn=1", // Use jad variable naming
            this.remappedJar.toAbsolutePath().toString(),
            decompileJarDir.toAbsolutePath().toString()
        };

        ConsoleDecompiler.main(args);

        // Pack the decompiled files into a zip file
        final Path zipFilePath = decompileDir.resolve(Path.of(cleanJarName + ".zip"));
        Logger.info("Packing decompiled files into %s", zipFilePath);
        FileUtil.remove(zipFilePath);
        FileUtil.zip(decompileJarDir, zipFilePath);

        final TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Decompiling completed in %s!", timeStamp);
    }

    public String prepareLatest() throws IOException {
        if (app != null) {
            app.updateStatusBox("Fetching version list from Mojang...");
            app.updateButton("Fetching...");
        }

        URLConnection connection = LATEST_INFO.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final JSONObject versions = new JSONObject(new JSONTokener(in));
        in.close();

        if (!versions.has("latest")) {
            String s = "Failed! Could not locate the latest data from the downloaded manifest file!";
            Logger.error(s);
            if (app != null) {
                app.updateStatusBox(s);
                app.updateButton("Exit");
            }
            throw new InvalidObjectException(s);
        }

        String type = version == Version.SERVER_LATEST_RELEASE || version == Version.CLIENT_LATEST_RELEASE
                ? "release"
                : "snapshot";

        String trueVersion = versions.getJSONObject("latest").getString(type);
        Logger.info("Finding the URL for " + trueVersion);
        if (app != null) {
            app.updateStatusBox("Finding the URL for " + trueVersion);
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
            String s = "Failed! Could not find information for version " + trueVersion + "!";
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
        JSONObject versionManifest = new JSONObject(new JSONTokener(in));
        in.close();

        JSONObject downloads = versionManifest.getJSONObject("downloads");
        String clientOrServer = version.getType().getName().toLowerCase();
        jarUrl = downloads.getJSONObject(clientOrServer).getString("url");
        mappingsUrl = downloads.getJSONObject(clientOrServer + "_mappings").getString("url");

        minecraftJarName = String.format("minecraft_%s_%s.jar", clientOrServer, trueVersion);
        mappingsName = String.format("mappings_%s_%s.txt", clientOrServer, trueVersion);
        mappedJarName = String.format("remapped_%s_%s.jar", clientOrServer, trueVersion);

        Logger.info("Found the jar and mappings url for " + trueVersion + "!");
        if (app != null) {
            app.updateStatusBox("Found the JAR and mappings URL for " + trueVersion + "!");
        }
        return trueVersion;
    }

    private void cleanup() {
        jarPath = null;
        mappingsPath = null;
        remappedJar = null;
        reconstruct = null;
    }
}
