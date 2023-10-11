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
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class Processor {
    private final ResourceRequest request;
    private final ProcessorOptions options;
    private final App app;
    private final Remapper remapper;
    private final Decompiler decompiler;
    private final Path dataFolderPath;
    private final Path jarPath;
    private final Path mappingsPath;
    private final Path remappedJar;

    private Processor(final ResourceRequest request, final ProcessorOptions options, final App app) {
        this.request = request;
        this.options = options;
        this.app = app;

        this.dataFolderPath = this.getCorrectDataFolder();
        this.jarPath = this.dataFolderPath.resolve("source.jar");
        this.mappingsPath = this.dataFolderPath.resolve("mappings.txt");
        this.remappedJar = this.dataFolderPath.resolve("remapped.jar");

        this.remapper = new ReconstructRemapper();
        this.decompiler = new VineflowerDecompiler();
    }

    public static void runProcessor(final ResourceRequest request, final ProcessorOptions options, final App app) {
        try {
            final Processor processor = new Processor(request, options, app);
            processor.init();
            processor.cleanup();
        } catch (final Exception e) {
            log.error("Failed to run processor", e);
        } finally {
            Util.forceGC();
        }
    }

    private Path getCorrectDataFolder() {
        final String versionFolder =
                this.request.type().getName() + "-" + this.request.getVersion().getId();
        final Path folderPath = Util.getBaseDataFolder().resolve(versionFolder);

        try {
            Files.createDirectories(folderPath);
        } catch (final IOException ignore) {
        }

        return folderPath;
    }

    private void handleGui(final Consumer<App> guiConsumer) {
        this.getApp().ifPresent(guiConsumer);
    }

    private void downloadFile(final URL url, final Path path, final String fileType) throws IOException {
        try (DurationTracker ignored = new DurationTracker(
                duration -> log.info("Successfully downloaded {} file in {}!", fileType, duration))) {
            log.info("Downloading {} file from Mojang...", fileType);
            final Request httpRequest = new Request.Builder().url(url).build();

            try (Response response = RequestUtil.CLIENT.newCall(httpRequest).execute()) {
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

    private boolean isValid() {
        if (this.getJarUrl() == null) {
            log.error(
                    "Failed to find JAR URL for version {}-{}",
                    this.request.type(),
                    this.request.getVersion().getId());
            this.handleGui(gui -> gui.updateStatusBox("Failed to find JAR URL for version " + this.request.type() + "-"
                    + this.request.getVersion().getId()));
            return false;
        }

        if (this.getMappingsUrl() == null) {
            log.error(
                    "Failed to find mappings URL for version {}-{}",
                    this.request.type(),
                    this.request.getVersion().getId());
            this.handleGui(gui -> gui.updateStatusBox("Failed to find mappings URL for version " + this.request.type()
                    + "-" + this.request.getVersion().getId()));
            return false;
        }

        return true;
    }

    @Nullable protected URL getJarUrl() {
        return this.request.getJar().orElse(null);
    }

    @Nullable protected URL getMappingsUrl() {
        return this.request.getMappings().orElse(null);
    }

    protected Optional<App> getApp() {
        return Optional.ofNullable(this.app);
    }

    public void init() {
        if (!this.isValid()) {
            return;
        }

        try (DurationTracker ignored = new DurationTracker(duration -> {
            log.info("Completed in {}!", duration);
            this.handleGui(gui -> {
                gui.updateStatusBox(String.format("Completed in %s!", duration));
                gui.getControlButton().reset();
            });
        })) {
            this.handleGui(App::toggleControls);

            // Download the JAR and mappings files
            this.handleGui(gui -> gui.updateStatusBox("Downloading JAR & MAPPINGS..."));
            CompletableFuture.allOf(this.downloadJar(), this.downloadMappings()).join();

            if (this.options.isRemap()) {
                this.remapJar();
            }

            if (this.options.isDecompile()) {
                this.decompileJar(this.options.isRemap() ? this.remappedJar : this.jarPath);
            }

        } catch (final IOException e) {
            log.error("Failed to run Processor!", e);
        } finally {
            this.handleGui(App::toggleControls);
        }
    }

    public CompletableFuture<Void> downloadJar() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.downloadFile(this.getJarUrl(), this.jarPath, "JAR");
            } catch (final IOException e) {
                throw new CompletionException(e);
            }

            return null;
        });
    }

    public CompletableFuture<Void> downloadMappings() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.downloadFile(this.getMappingsUrl(), this.mappingsPath, "mappings");
            } catch (final IOException exception) {
                throw new CompletionException(exception);
            }

            return null;
        });
    }

    public void remapJar() {
        try (final DurationTracker ignored =
                new DurationTracker(duration -> log.info("Remapping completed in {}!", duration))) {
            this.handleGui(gui -> gui.updateStatusBox("Remapping..."));

            if (!Files.exists(this.remappedJar)) {
                log.info("Remapping {} file...", this.jarPath.getFileName());
                this.remapper.remap(this.jarPath, this.mappingsPath, this.remappedJar);
            } else {
                log.info("{} already remapped... skipping mapping.", this.remappedJar.getFileName());
            }
        }
    }

    public void decompileJar(final Path jarPath) throws IOException {
        try (final DurationTracker ignored =
                new DurationTracker(duration -> log.info("Decompiling completed in {}!", duration))) {
            log.info("Decompiling final JAR file.");
            this.handleGui(gui -> gui.updateStatusBox("Decompiling... This will take a while!"));

            final String cleanJarName = "decompiled";
            final Path decompileJarDir = this.dataFolderPath.resolve(cleanJarName);
            FileUtil.remove(decompileJarDir);
            Files.createDirectories(decompileJarDir);

            this.decompiler.decompile(jarPath, decompileJarDir);

            if (this.options.isZipDecompileOutput()) {
                // Pack the decompiled files into a zip file
                final Path zipFilePath = this.dataFolderPath.resolve(Path.of(cleanJarName + ".zip"));
                log.info("Packing decompiled files into {}", zipFilePath);
                this.handleGui(gui -> gui.updateStatusBox("Packing decompiled files ..."));
                FileUtil.remove(zipFilePath);
                FileUtil.zip(decompileJarDir, zipFilePath);
            }
        }
    }

    private void cleanup() {
        if (this.remapper instanceof final Cleanup cleanup) {
            cleanup.cleanup();
        }

        if (this.decompiler instanceof final Cleanup cleanup) {
            cleanup.cleanup();
        }
    }
}
