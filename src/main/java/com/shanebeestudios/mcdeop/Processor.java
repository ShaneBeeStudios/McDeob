package com.shanebeestudios.mcdeop;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import com.shanebeestudios.mcdeop.app.App;
import com.shanebeestudios.mcdeop.util.FileUtil;
import com.shanebeestudios.mcdeop.util.Logger;
import com.shanebeestudios.mcdeop.util.TimeStamp;
import com.shanebeestudios.mcdeop.util.Util;
import io.github.lxgaming.reconstruct.common.Reconstruct;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler;

// TODO: Reconstruct breaks after the first run.
@SuppressWarnings("ResultOfMethodCallIgnored")
public class Processor {
    private final ResourceRequest version;
    private final boolean decompile;
    private final App app;

    private final Reconstruct reconstruct;
    private final Path jarPath;
    private final Path mappingsPath;
    private final Path remappedJar;
    private final URL mappingsUrl;
    private final URL jarUrl;

    private final String minecraftJarName;
    private final String mappedJarName;

    private final Path dataFolderPath;

    public Processor(final ResourceRequest version, final boolean decompile, final App app) {
        this.version = version;
        this.app = app;
        if (Util.isRunningMacOS()) {
            // If running on macOS, put the output directory in the user home directory.
            // This is due to how macOS APPs work — their '.' directory resolves to one inside the APP itself.
            this.dataFolderPath = Paths.get(System.getProperty("user.home"), "McDeob");
        } else {
            this.dataFolderPath = Paths.get("deobf-work");
        }

        try {
            Files.createDirectories(this.dataFolderPath);
        } catch (final IOException ignore) {
        }

        this.minecraftJarName = String.format(
                "minecraft_%s_%s.jar",
                version.getType().getName(), version.getVersion().getId());
        final String mappingsName = String.format(
                "mappings_%s_%s.txt",
                version.getType().getName(), version.getVersion().getId());
        this.mappedJarName = String.format(
                "remapped_%s_%s.jar",
                version.getType().getName(), version.getVersion().getId());

        this.remappedJar = this.dataFolderPath.resolve(this.mappedJarName);
        this.mappingsPath = this.dataFolderPath.resolve(mappingsName);
        this.jarPath = this.dataFolderPath.resolve(this.minecraftJarName);

        this.jarUrl = version.getJar().orElse(null);
        this.mappingsUrl = version.getMappings().orElse(null);
        this.decompile = decompile;
        this.reconstruct = this.createReconstruct();
    }

    private Reconstruct createReconstruct() {
        final ReconConfig config = new ReconConfig();

        config.setInputPath(this.jarPath.toAbsolutePath());
        config.setMappingPath(this.mappingsPath.toAbsolutePath());
        config.setOutputPath(this.remappedJar.toAbsolutePath());

        return new Reconstruct(config);
    }

    public void init() {
        if (this.jarUrl == null) {
            Logger.error(
                    "Failed to find JAR URL for version %s-%s",
                    this.version.getType(), this.version.getVersion().getId());
            if (this.app != null) {
                this.app.updateStatusBox("Failed to find JAR URL for version " + this.version.getType() + "-"
                        + this.version.getVersion().getId());
            }
            this.cleanup();
            return;
        }

        if (this.mappingsUrl == null) {
            Logger.error(
                    "Failed to find mappings URL for version %s-%s",
                    this.version.getType(), this.version.getVersion().getId());
            if (this.app != null) {
                this.app.updateStatusBox("Failed to find mappings URL for version " + this.version.getType() + "-"
                        + this.version.getVersion().getId());
            }
            this.cleanup();
            return;
        }

        try {
            final long start = System.currentTimeMillis();
            if (this.app != null) {
                this.app.toggleControls();
            }

            this.downloadJar();
            this.downloadMappings();
            this.remapJar();
            if (this.decompile) {
                this.decompileJar();
            }
            this.cleanup();

            final TimeStamp timeStamp = TimeStamp.fromNow(start);
            Logger.info("Completed in %s!", timeStamp);
            if (this.app != null) {
                this.app.updateStatusBox(String.format("Completed in %s!", timeStamp));
                this.app.updateButton("Start!");
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (this.app != null) {
                this.app.toggleControls();
            }
        }
    }

    public void downloadJar() throws IOException {
        final long start = System.currentTimeMillis();
        Logger.info("Downloading JAR file from Mojang.");
        if (this.app != null) {
            this.app.updateStatusBox("Downloading JAR...");
            this.app.updateButton("Downloading JAR...", Color.BLUE);
        }

        final HttpURLConnection connection = (HttpURLConnection) this.jarUrl.openConnection();
        final long length = connection.getContentLengthLong();
        if (Files.exists(this.jarPath) && Files.size(this.jarPath) == length) {
            Logger.info("Already have JAR, skipping download.");
        } else {
            try (final InputStream inputStream = connection.getInputStream()) {
                Files.copy(inputStream, this.jarPath, REPLACE_EXISTING);
            }
        }

        final TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Successfully downloaded JAR file in %s!", timeStamp);
    }

    public void downloadMappings() throws IOException {
        final long start = System.currentTimeMillis();
        Logger.info("Downloading mappings file from Mojang...");
        if (this.app != null) {
            this.app.updateStatusBox("Downloading mappings...");
            this.app.updateButton("Downloading mappings...", Color.BLUE);
        }
        final HttpURLConnection connection = (HttpURLConnection) this.mappingsUrl.openConnection();
        final long length = connection.getContentLengthLong();
        if (Files.exists(this.mappingsPath) && Files.size(this.mappingsPath) == length) {
            Logger.info("Already have mappings, skipping download.");
        } else {
            try (final InputStream inputStream = connection.getInputStream()) {
                Files.copy(inputStream, this.mappingsPath, REPLACE_EXISTING);
            }
        }

        final TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Successfully downloaded mappings file in %s!", timeStamp);
    }

    public void remapJar() {
        final long start = System.currentTimeMillis();
        if (this.app != null) {
            this.app.updateStatusBox("Remapping...");
            this.app.updateButton("Remapping...", Color.BLUE);
        }

        if (!Files.exists(this.remappedJar)) {
            Logger.info("Remapping %s file...", this.minecraftJarName);
            this.reconstruct.load();

            final TimeStamp timeStamp = TimeStamp.fromNow(start);
            Logger.info("Remapping completed in %s!", timeStamp);
        } else {
            Logger.info("%s already remapped... skipping mapping.", this.mappedJarName);
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

    private void cleanup() {
        // TODO: Re-implement better cleanup system
        /*
        this.jarPath = null;
        this.mappingsPath = null;
        this.remappedJar = null;
        this.reconstruct = null;
        System.gc();

         */
    }
}
