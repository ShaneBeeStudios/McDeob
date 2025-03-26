package com.shanebeestudios.mcdeob;

import com.shanebeestudios.mcdeob.app.App;
import com.shanebeestudios.mcdeob.util.AppLogger;
import com.shanebeestudios.mcdeob.util.Logger;
import com.shanebeestudios.mcdeob.util.TimeStamp;
import com.shanebeestudios.mcdeob.util.Util;
import com.shanebeestudios.mcdeob.version.Version;
import net.md_5.specialsource.Jar;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.SpecialSource;
import net.md_5.specialsource.provider.JarProvider;
import net.md_5.specialsource.provider.JointProvider;
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
import java.io.FileOutputStream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Processor {

    private final Version version;
    private final boolean decompile;
    private final App app;

    private final Path dataFolderPath;
    private Path jarPath;
    private Path mappingsPath;
    private Path remappedJar;

    private String minecraftJarName;
    private String mappingsName;
    private String mappedJarName;

    public Processor(Version version, boolean decompile, App app) {
        this.version = version;
        this.decompile = decompile;
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
        } catch (IOException ignore) {
        }
    }

    public Path getDataFolderPath() {
        return this.dataFolderPath;
    }

    public Version getVersion() {
        return this.version;
    }

    public Path getJarPath() {
        return this.jarPath;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void init() {
        try {
            long start = System.currentTimeMillis();

            // Prepare version and check if valid
            String versionName = this.version.getVersion();
            if (!this.version.prepareVersion()) {
                if (this.app != null) this.app.fail();
                System.out.println("Invalid version: " + versionName);
                return;
            }
            if (this.app != null) {
                this.app.toggleControls();
            }

            String versionTypeName = this.version.getType().getName();
            this.minecraftJarName = String.format("minecraft_%s_%s.jar", versionTypeName, versionName);
            this.mappingsName = String.format("mappings_%s_%s.txt", versionTypeName, versionName);
            this.mappedJarName = String.format("remapped_%s_%s.jar", versionTypeName, versionName);

            downloadJar();
            downloadMappings();
            remapJar();
            if (this.decompile) {
                decompileJar();
            }
            cleanup();

            TimeStamp timeStamp = TimeStamp.fromNow(start);
            Logger.info("Completed in %s!", timeStamp);
            if (this.app != null) {
                this.app.updateStatusBox(String.format("Completed in %s!", timeStamp));
                this.app.updateButton("Start!");
                this.app.toggleControls();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadJar() throws IOException {
        long start = System.currentTimeMillis();
        Logger.info("Downloading JAR file from Mojang.");
        
        this.jarPath = this.dataFolderPath.resolve(this.minecraftJarName);
        final URL jarURL = new URL(this.version.getJarURL());
        final HttpURLConnection connection = (HttpURLConnection) jarURL.openConnection();
        final long contentLength = connection.getContentLengthLong();

        if (this.app != null) {
            this.app.updateButton("Downloading JAR...", Color.BLUE);
        }

        if (Files.exists(jarPath) && Files.size(jarPath) == contentLength) {
            Logger.info("Already have JAR, skipping download.");
            if (this.app != null) {
                this.app.resetProgressBar();
            }
            return;
        }

        try (final InputStream inputStream = connection.getInputStream()) {
            // Create a buffered output stream
            try (FileOutputStream outputStream = new FileOutputStream(this.jarPath.toFile())) {
                byte[] buffer = new byte[4096];
                long downloadedBytes = 0;
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    downloadedBytes += bytesRead;

                    // Update progress
                    if (this.app != null) {
                        int progress = (int) ((downloadedBytes * 100) / contentLength);
                        this.app.updateProgressBar(progress, 100, "Downloading JAR...");
                    }
                }
            }
        }

        if (this.app != null) {
            this.app.resetProgressBar();
        }

        TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Successfully downloaded JAR file in %s!", timeStamp);
    }

    public void downloadMappings() throws IOException {
        long start = System.currentTimeMillis();
        Logger.info("Downloading mappings file from Mojang...");
        
        final URL mappingURL = new URL(this.version.getMapURL());
        final HttpURLConnection connection = (HttpURLConnection) mappingURL.openConnection();
        final long contentLength = connection.getContentLengthLong();
        this.mappingsPath = this.dataFolderPath.resolve(this.mappingsName);

        if (this.app != null) {
            this.app.updateButton("Downloading mappings...", Color.BLUE);
        }

        if (Files.exists(this.mappingsPath) && Files.size(this.mappingsPath) == contentLength) {
            Logger.info("Already have mappings, skipping download.");
            if (this.app != null) {
                this.app.resetProgressBar();
            }
            return;
        }

        try (final InputStream inputStream = connection.getInputStream()) {
            // Create a buffered output stream
            try (FileOutputStream outputStream = new FileOutputStream(this.mappingsPath.toFile())) {
                byte[] buffer = new byte[4096];
                long downloadedBytes = 0;
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    downloadedBytes += bytesRead;

                    if (this.app != null) {
                        int progress = (int) ((downloadedBytes * 100) / contentLength);
                        this.app.updateProgressBar(progress, 100, "Downloading mappings...");
                    }
                }
            }
        }

        if (this.app != null) {
            this.app.resetProgressBar();
        }

        TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Successfully downloaded mappings file in %s!", timeStamp);
    }

    public void remapJar() {
        long start = System.currentTimeMillis();
        if (this.app != null) {
            this.app.updateButton("Remapping...", Color.BLUE);
        }
        this.remappedJar = this.dataFolderPath.resolve(this.mappedJarName);

        if (!Files.exists(remappedJar)) {
            Logger.info("Remapping %s file...", this.minecraftJarName);

            try {
                if (this.app != null) {
                    this.app.updateStatusBox("Moving to Remapping");
                }
        
                JarMapping jarMapping = new JarMapping();
                
                if (this.app != null) {
                    this.app.updateProgressBar(10, 100, "Preparing mappings...");
                }
                jarMapping.loadMappings(new File(this.mappingsPath.toUri()));

                if (this.app != null) {
                    this.app.updateProgressBar(30, 100, "Setting up inheritance...");
                }
                JointProvider inheritanceProvider = new JointProvider();
                jarMapping.setFallbackInheritanceProvider(inheritanceProvider);

                SpecialSource.kill_lvt = true;
                JarRemapper jarRemapper = new JarRemapper(jarMapping);

                if (this.app != null) {
                    this.app.updateProgressBar(50, 100, "Loading internal jars...");
                }
                Jar internalJars = Util.getInternalJars(this);

                inheritanceProvider.add(new JarProvider(internalJars));

                if (this.app != null) {
                    this.app.updateProgressBar(70, 100, "Remapping JAR...");
                }
                jarRemapper.remapJar(internalJars, new File(this.remappedJar.toUri()));

                if (this.app != null) {
                    this.app.updateProgressBar(90, 100, "Finalizing...");
                }
                internalJars.close();

                if (this.version.getMappingType() == Version.MappingType.SEARGE) {
                    Util.stripFileFromJar(this.remappedJar, "it/*");
                }
                
                if (this.app != null) {
                    this.app.updateStatusBox("Remapping Complete");
                }
            } catch (IOException e) {
                if (this.app != null) {
                    this.app.updateStatusBox("Remapping Failed");
                }
                throw new RuntimeException(e);
            }

            TimeStamp timeStamp = TimeStamp.fromNow(start);
            Logger.info("Remapping completed in %s!", timeStamp);
        } else {
            Logger.info("%s already remapped... skipping mapping.", this.mappedJarName);
            if (this.app != null) {
                this.app.resetProgressBar();
            }
        }
    }

    @SuppressWarnings("resource")
    public void decompileJar() throws IOException {
        long start = System.currentTimeMillis();
        Logger.info("Decompiling final JAR file.");
        if (this.app != null) {
            this.app.updateStatusBox("Decompiler starting...");
            this.app.updateButton("Decompiling...", Color.BLUE);
        }
        final Path decompileDir = Files.createDirectories(this.dataFolderPath.resolve("final-decompile"));

        try {
            if (this.app != null) {
                this.app.updateStatusBox("Starting Decompilation");
            }
            // Setup and run FernFlower
            AppLogger appLogger = new AppLogger(this.app);
            ConsoleDecompiler decompiler = new ConsoleDecompiler(new File(decompileDir.toUri()), Util.getDecompilerParams(), appLogger);
            decompiler.addSource(new File(this.remappedJar.toUri()));
            decompiler.decompileContext();
            appLogger.stopLogging();

            // Rename jar file to zip
            Util.renameJarsToZips(decompileDir);

            TimeStamp timeStamp = TimeStamp.fromNow(start);
            Logger.info("Decompiling completed in %s!", timeStamp);
            if (this.app != null) {
                this.app.updateStatusBox("Decompilation Complete");
            }
        } catch (Exception e) {
            if (this.app != null) {
                this.app.updateStatusBox("Decompilation Failed");
            }
            throw e;
        }
    }

    private void cleanup() {
        this.jarPath = null;
        this.mappingsPath = null;
        this.remappedJar = null;
        System.gc();
    }

}
