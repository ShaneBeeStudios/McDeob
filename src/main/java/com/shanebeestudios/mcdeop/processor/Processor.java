package com.shanebeestudios.mcdeop.processor;

import com.shanebeestudios.mcdeop.app.App;
import com.shanebeestudios.mcdeop.processor.decompiler.Decompiler;
import com.shanebeestudios.mcdeop.processor.decompiler.VineflowerDecompiler;
import com.shanebeestudios.mcdeop.processor.remapper.ReconstructRemapper;
import com.shanebeestudios.mcdeop.processor.remapper.Remapper;
import com.shanebeestudios.mcdeop.util.DurationTracker;
import com.shanebeestudios.mcdeop.util.FileUtil;
import com.shanebeestudios.mcdeop.util.RequestUtil;
import com.shanebeestudios.mcdeop.util.Util;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Slf4j
public class Processor {
    public static void runProcessor(final ResourceRequest version, final boolean decompile, final App app) {
        try {
            final Processor processor = new Processor(version, decompile, app);
            processor.init();
            processor.cleanup();
        } catch (final Exception e) {
            log.error("Failed to run processor", e);
        } finally {
            Util.forceGC();
        }
    }

    private final ResourceRequest version;
    private final boolean decompile;
    private final App app;

    private final Remapper remapper;
    private final Decompiler decompiler;

    private final Path jarPath;
    private final Path mappingsPath;
    private final Path remappedJar;
    private final URL mappingsUrl;
    private final URL jarUrl;

    private final String minecraftJarName;
    private final String mappedJarName;

    private final Path dataFolderPath;

    private Processor(final ResourceRequest version, final boolean decompile, final App app) {
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

        this.remapper = new ReconstructRemapper();
        this.decompiler = new VineflowerDecompiler();
    }

    private void handleGui(final Consumer<App> guiConsumer) {
        if (this.app != null) {
            guiConsumer.accept(this.app);
        }
    }

    private void downloadFile(final URL url, final Path path, final String fileType) throws IOException {
        try (DurationTracker ignored = new DurationTracker(
                duration -> log.info("Successfully downloaded {} file in {}!", fileType, duration))) {
            log.info("Downloading {} file from Mojang...", fileType);
            final Request request = new Request.Builder().url(url).build();

            try (Response response = RequestUtil.CLIENT.newCall(request).execute()) {
                if (response.body() == null) {
                    throw new IOException("Response body was null");
                }

                final long length = response.body().contentLength();
                if (Files.exists(path) && Files.size(path) == length) {
                    log.info("Already have {}, skipping download.", path.getFileName());
                    return;
                }

                FileUtil.remove(path);
                try (BufferedSink sink = Okio.buffer(Okio.sink(path))) {
                    sink.writeAll(response.body().source());
                }
            }
        }
    }

    public void init() {
        if (this.jarUrl == null) {
            log.error(
                    "Failed to find JAR URL for version {}-{}",
                    this.version.getType(),
                    this.version.getVersion().getId());
            this.handleGui(gui -> gui.updateStatusBox("Failed to find JAR URL for version " + this.version.getType()
                    + "-" + this.version.getVersion().getId()));
            return;
        }

        if (this.mappingsUrl == null) {
            log.error(
                    "Failed to find mappings URL for version {}-{}",
                    this.version.getType(),
                    this.version.getVersion().getId());
            this.handleGui(gui -> gui.updateStatusBox("Failed to find mappings URL for version "
                    + this.version.getType() + "-" + this.version.getVersion().getId()));
            return;
        }

        try (DurationTracker ignored = new DurationTracker(duration -> {
            log.info("Completed in {}!", duration);
            this.handleGui(gui -> {
                gui.updateStatusBox(String.format("Completed in %s!", duration));
                gui.updateButton("Start!");
            });
        })) {
            this.handleGui(App::toggleControls);

            // Download the JAR and mappings files
            this.handleGui(gui -> {
                gui.updateStatusBox("Downloading JAR & MAPPINGS...");
                gui.updateButton("Downloading JAR & MAPPINGS...", Color.BLUE);
            });
            CompletableFuture.allOf(this.downloadJar(), this.downloadMappings()).join();

            this.remapJar();
            if (this.decompile) {
                this.decompileJar();
            }

        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            this.handleGui(App::toggleControls);
        }
    }

    public CompletableFuture<Void> downloadJar() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.downloadFile(this.jarUrl, this.jarPath, "JAR");
            } catch (final IOException e) {
                throw new CompletionException(e);
            }

            return null;
        });
    }

    public CompletableFuture<Void> downloadMappings() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.downloadFile(this.mappingsUrl, this.mappingsPath, "mappings");
            } catch (final IOException e) {
                throw new CompletionException(e);
            }

            return null;
        });
    }

    public void remapJar() {
        try (DurationTracker ignored =
                new DurationTracker(duration -> log.info("Remapping completed in {}!", duration))) {
            this.handleGui(gui -> {
                gui.updateStatusBox("Remapping...");
                gui.updateButton("Remapping...", Color.BLUE);
            });

            if (!Files.exists(this.remappedJar)) {
                log.info("Remapping {} file...", this.minecraftJarName);
                this.remapper.remap(this.jarPath, this.mappingsPath, this.remappedJar);
            } else {
                log.info("{} already remapped... skipping mapping.", this.mappedJarName);
            }
        }
    }

    public void decompileJar() throws IOException {
        try (DurationTracker ignored =
                new DurationTracker(duration -> log.info("Decompiling completed in {}!", duration))) {
            log.info("Decompiling final JAR file.");
            this.handleGui(gui -> {
                gui.updateStatusBox("Decompiling... This will take a while!");
                gui.updateButton("Decompiling...", Color.BLUE);
            });

            final Path decompileDir = this.dataFolderPath.resolve("final-decompile");
            Files.createDirectories(decompileDir);

            final String cleanJarName =
                    this.remappedJar.getFileName().toString().replace(".jar", "");
            final Path decompileJarDir = decompileDir.resolve(cleanJarName);
            FileUtil.remove(decompileJarDir);
            Files.createDirectories(decompileJarDir);

            this.decompiler.decompile(this.remappedJar, decompileJarDir);

            // Pack the decompiled files into a zip file
            final Path zipFilePath = decompileDir.resolve(Path.of(cleanJarName + ".zip"));
            log.info("Packing decompiled files into {}", zipFilePath);
            FileUtil.remove(zipFilePath);
            FileUtil.zip(decompileJarDir, zipFilePath);
        }
    }

    private void cleanup() {
        if (this.remapper != null && this.remapper instanceof final Cleanup cleanup) {
            cleanup.cleanup();
        }

        if (this.decompiler != null && this.decompiler instanceof final Cleanup cleanup) {
            cleanup.cleanup();
        }
    }
}
