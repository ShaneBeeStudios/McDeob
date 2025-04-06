package com.shanebeestudios.mcdeob;

import com.shanebeestudios.mcdeob.app.App;
import com.shanebeestudios.mcdeob.util.AppLogger;
import com.shanebeestudios.mcdeob.util.Logger;
import com.shanebeestudios.mcdeob.util.TimeSpan;
import com.shanebeestudios.mcdeob.util.Util;
import com.shanebeestudios.mcdeob.version.Version;
import net.md_5.specialsource.Jar;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.SpecialSource;
import net.md_5.specialsource.provider.JarProvider;
import net.md_5.specialsource.provider.JointProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Processor {

    private final @NotNull Version version;
    private final @Nullable String cachedJarName;
    private final Optional<App> appOption;
    private final boolean decompile;
    private final boolean debug;

    private final Path dataFolderPath;
    private Path jarPath;
    private Path mappingsPath;
    private Path remappedJar;

    private String minecraftJarName;
    private String mappingsName;
    private String mappedJarName;

    public Processor(@NotNull Version version, @Nullable String cachedJarName, @Nullable App app, boolean decompile, boolean debug) {
        this.version = version;
        if (cachedJarName != null) {
            if (cachedJarName.endsWith(".jar")) {
                this.cachedJarName = cachedJarName;
            } else {
                this.cachedJarName = cachedJarName + ".jar";
            }
        } else {
            this.cachedJarName = null;
        }
        this.appOption = Optional.ofNullable(app);
        this.decompile = decompile;
        this.debug = debug;
        if (Util.isRunningMacOS()) {
            // If running on macOS, put the output directory in the user home directory.
            // This is due to how macOS APPs work — their '.' directory resolves to one inside the APP itself.
            this.dataFolderPath = Paths.get(System.getProperty("user.home"), "McDeob");
        } else {
            this.dataFolderPath = Paths.get("deobf-work");
        }
        try {
            Files.createDirectories(this.dataFolderPath);
        } catch (IOException ignore) {
        }
    }

    public Path getDataFolderPath() {
        return this.dataFolderPath;
    }

    public @NotNull Version getVersion() {
        return this.version;
    }

    public Path getJarPath() {
        return this.jarPath;
    }

    public void init() {
        TimeSpan timeSpan = TimeSpan.start();

        // Prepare version and check if valid
        String versionName = this.version.getVersion();

        // If the version is invalid, fail
        if (!this.version.prepareVersion()) {
            fail("Invalid version: " + versionName);
            return;
        }

        // If the app is available, turn off controls
        this.appOption.ifPresent(App::toggleControls);

        String versionTypeName = this.version.getType().getName();
        this.minecraftJarName = String.format("minecraft_%s_%s.jar", versionTypeName, versionName);
        this.mappingsName = String.format("mappings_%s_%s.txt", versionTypeName, versionName);
        this.mappedJarName = String.format("remapped_%s_%s.jar", versionTypeName, versionName);

        // Attempt to download jar
        if (!downloadJar()) {
            fail("Failed to retrieve jar.");
            return;
        }

        // Attempt to download mappings
        if (!downloadMappings()) {
            fail("Failed to download mappings.");
            return;
        }

        // Attempt to remap jar
        if (!remapJar()) {
            fail("Failed to remap jar.");
            return;
        }

        // Attempt to decompile
        if (this.decompile && !decompileJar()) {
            fail("Failed to decompile JAR.");
            return;
        }
        cleanup();

        timeSpan.finish();
        Logger.info("Completed in %s!", timeSpan);
        this.appOption.ifPresent(app -> app.finish(timeSpan));
    }

    public boolean downloadJar() {
        // Attempt to use cached jar first
        if (this.cachedJarName != null) {
            this.jarPath = this.dataFolderPath.resolve(this.cachedJarName);
            if (Files.exists(this.jarPath)) {
                Logger.info("Using cached jar!");
                return true;
            } else {
                Logger.error("Cached jar as not found.");
                return false;
            }
        } else { // If no cached jar, continue with donload
            TimeSpan timeSpan = TimeSpan.start();
            Logger.info("Downloading JAR file from Mojang.");
            this.appOption.ifPresent(a -> {
                a.updateStatusBox("Downloading JAR...");
                a.updateButton("Downloading JAR...", Color.BLUE);
            });
            this.jarPath = this.dataFolderPath.resolve(this.minecraftJarName);

            // Try open a connection with Mojang servers for downloads
            final HttpURLConnection connection;
            long connectionContentLength;
            try {
                final URL jarURL = new URL(this.version.getJarURL());
                connection = (HttpURLConnection) jarURL.openConnection();
                connectionContentLength = connection.getContentLengthLong();
            } catch (IOException e) {
                logException("Failed to open connection to Mojang servers", e);
                return false;
            }

            // Check if we already have the jar on the computer
            boolean jarExists;
            try {
                jarExists = Files.exists(jarPath) && Files.size(jarPath) == connectionContentLength;
            } catch (IOException e) {
                logException("Failed to check if jar file exists", e);
                return false;
            }

            // If the jar exists skip download, else attempt to download
            if (jarExists) {
                Logger.warn("Already have JAR, skipping download.");
                return true;
            } else try (final InputStream inputStream = connection.getInputStream()) {
                Files.copy(inputStream, jarPath, REPLACE_EXISTING);
            } catch (IOException e) {
                logException("Failed to download jar", e);
                return false;
            }
            timeSpan.finish();
            Logger.info("Successfully downloaded JAR file in %s!", timeSpan);
            return true;
        }
    }

    public boolean downloadMappings() {
        TimeSpan timeSpan = TimeSpan.start();
        Logger.info("Downloading mappings file from Mojang...");
        this.appOption.ifPresent(app -> {
            app.updateStatusBox("Downloading mappings...");
            app.updateButton("Downloading mappings...", Color.BLUE);
        });

        final HttpURLConnection connection;
        try {
            final URL mappingURL = new URL(this.version.getMapURL());
            connection = (HttpURLConnection) mappingURL.openConnection();
        } catch (IOException e) {
            logException("Failed to open connection to Mojang servers", e);
            return false;
        }
        final long length = connection.getContentLengthLong();
        this.mappingsPath = this.dataFolderPath.resolve(this.mappingsName);
        boolean mappingExists;
        try {
            mappingExists = Files.exists(this.mappingsPath) && Files.size(this.mappingsPath) == length;
        } catch (IOException e) {
            logException("Failed to check if mapping file exists", e);
            return false;
        }
        if (mappingExists) {
            Logger.info("Already have mappings, skipping download.");
            return true;
        } else try (final InputStream inputStream = connection.getInputStream()) {
            Files.copy(inputStream, this.mappingsPath, REPLACE_EXISTING);
        } catch (IOException e) {
            logException("Failed to download mappings", e);
            return false;
        }

        timeSpan.finish();
        Logger.info("Successfully downloaded mappings file in %s!", timeSpan);
        return true;
    }

    public boolean remapJar() {
        TimeSpan timeSpan = TimeSpan.start();
        this.appOption.ifPresent(app -> {
            app.updateStatusBox("Remapping...");
            app.updateButton("Remapping...", Color.BLUE);
        });
        this.remappedJar = this.dataFolderPath.resolve(this.mappedJarName);

        if (Files.exists(this.remappedJar)) {
            Logger.info("%s already remapped... skipping mapping.", this.mappedJarName);
        } else {
            Logger.info("Remapping %s file...", this.minecraftJarName);

            try {
                // SpecialSource remapping
                JarMapping jarMapping = new JarMapping();
                jarMapping.loadMappings(new File(this.mappingsPath.toUri()));

                JointProvider inheritanceProvider = new JointProvider();
                jarMapping.setFallbackInheritanceProvider(inheritanceProvider);

                SpecialSource.kill_lvt = true;
                JarRemapper jarRemapper = new JarRemapper(jarMapping);

                Jar internalJars = Util.getInternalJars(this);

                inheritanceProvider.add(new JarProvider(internalJars));
                jarRemapper.remapJar(internalJars, new File(this.remappedJar.toUri()));
                internalJars.close();

                if (this.version.getMappingType() == Version.MappingType.SEARGE) {
                    // Some versions like 1.12.2 include this nasty giant package
                    Util.stripFileFromJar(this.remappedJar, "it/*");
                }
            } catch (IOException e) {
                logException("Failed to run remapper", e);
                return false;
            }

            timeSpan.finish();
            Logger.info("Remapping completed in %s!", timeSpan);
        }
        return true;
    }

    @SuppressWarnings("resource")
    public boolean decompileJar() {
        TimeSpan timeSpan = TimeSpan.start();
        Logger.info("Decompiling final JAR file.");
        this.appOption.ifPresent(app -> {
            app.updateStatusBox("Decompiler starting...");
            app.updateButton("Decompiling...", Color.BLUE);
        });
        final Path decompileDir;
        try {
            decompileDir = Files.createDirectories(this.dataFolderPath.resolve("final-decompile"));
        } catch (IOException e) {
            logException("Failed to create final-decompile path", e);
            return false;
        }

        // Setup and run FernFlower
        AppLogger appLogger = new AppLogger(this.appOption.orElse(null));
        ConsoleDecompiler decompiler = new ConsoleDecompiler(new File(decompileDir.toUri()), Util.getDecompilerParams(), appLogger);
        decompiler.addSource(new File(this.remappedJar.toUri()));
        decompiler.decompileContext();
        appLogger.stopLogging();

        // Rename jar file to zip
        Util.renameJarsToZips(decompileDir);

        timeSpan.finish();
        Logger.info("Decompiling completed in %s!", timeSpan);
        return true;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void logException(String message, Exception exception) {
        Logger.error(message + ": " + exception.getMessage());
        if (this.debug) exception.printStackTrace();
    }

    private void fail(String failMessage) {
        Logger.error(failMessage);
        this.appOption.ifPresent(app -> app.fail(failMessage));
        cleanup();
    }

    private void cleanup() {
        this.jarPath = null;
        this.mappingsPath = null;
        this.remappedJar = null;
        System.gc();
    }

}
