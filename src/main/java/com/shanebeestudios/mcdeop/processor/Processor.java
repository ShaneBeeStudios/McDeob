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
import java.util.Locale;
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
    private final Path jarPath;
    private final Path mappingsPath;
    private final Path remappedJar;
    private final Path decompiledJarPath;
    private final Path decompiledZipPath;

    private Processor(final ResourceRequest request, final ProcessorOptions options, final App app) {
        this.request = request;
        this.options = options;
        this.app = app;

        this.remapper = new ReconstructRemapper();
        this.decompiler = new VineflowerDecompiler();

        final Path dataFolderPath = this.getDataFolder();
        this.jarPath = dataFolderPath.resolve("source.jar");
        this.mappingsPath = dataFolderPath.resolve("mappings.txt");
        this.remappedJar = dataFolderPath.resolve("remapped.jar");
        this.decompiledJarPath = dataFolderPath.resolve("decompiled");
        this.decompiledZipPath = dataFolderPath.resolve(Path.of("decompiled.zip"));
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

    private Path getDataFolder() {
        final String versionFolder = String.format(
                "%s-%s",
                this.request.type().name().toLowerCase(Locale.ENGLISH),
                this.request.getVersion().getId());
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

    private void setGuiStatusMessage(final String statusMessage) {
        this.handleGui(gui -> gui.updateStatusBox(statusMessage));
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
            this.setGuiStatusMessage(String.format(
                    "Failed to find JAR URL for version %s-%s",
                    this.request.type(), this.request.getVersion().getId()));
            return false;
        }

        if (this.getMappingsUrl() == null) {
            log.error(
                    "Failed to find mappings URL for version {}-{}",
                    this.request.type(),
                    this.request.getVersion().getId());
            this.setGuiStatusMessage(String.format(
                    "Failed to find mappings URL for version %s-%s",
                    this.request.type(), this.request.getVersion().getId()));
            return false;
        }

        return true;
    }

    @Nullable private URL getJarUrl() {
        return this.request.getJar().orElse(null);
    }

    @Nullable private URL getMappingsUrl() {
        return this.request.getMappings().orElse(null);
    }

    private Optional<App> getApp() {
        return Optional.ofNullable(this.app);
    }

    private CompletableFuture<Void> downloadJar() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.downloadFile(this.getJarUrl(), this.jarPath, "JAR");
            } catch (final IOException e) {
                throw new CompletionException(e);
            }

            return null;
        });
    }

    private CompletableFuture<Void> downloadMappings() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.downloadFile(this.getMappingsUrl(), this.mappingsPath, "mappings");
            } catch (final IOException exception) {
                throw new CompletionException(exception);
            }

            return null;
        });
    }

    private void remapJar() {
        try (final DurationTracker ignored =
                new DurationTracker(duration -> log.info("Remapping completed in {}!", duration))) {
            this.setGuiStatusMessage("Remapping...");

            if (!Files.exists(this.remappedJar)) {
                log.info("Remapping {} file...", this.jarPath.getFileName());
                this.remapper.remap(this.jarPath, this.mappingsPath, this.remappedJar);
            } else {
                log.info("{} already remapped... skipping mapping.", this.remappedJar.getFileName());
            }
        }
    }

    private void decompileJar(final Path jarPath) throws IOException {
        try (final DurationTracker ignored =
                new DurationTracker(duration -> log.info("Decompiling completed in {}!", duration))) {
            log.info("Decompiling final JAR file.");
            this.setGuiStatusMessage("Decompiling... This will take a while!");

            FileUtil.remove(this.decompiledJarPath);
            Files.createDirectories(this.decompiledJarPath);

            this.decompiler.decompile(jarPath, this.decompiledJarPath);

            if (this.options.isZipDecompileOutput()) {
                // Pack the decompiled files into a zip file
                log.info("Packing decompiled files into {}", this.decompiledZipPath);
                this.setGuiStatusMessage("Packing decompiled files ...");
                FileUtil.remove(this.decompiledZipPath);
                FileUtil.zip(this.decompiledJarPath, this.decompiledZipPath);
            }
        }
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
            this.setGuiStatusMessage("Downloading JAR & MAPPINGS...");
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

    public void cleanup() {
        if (this.remapper instanceof final Cleanup cleanup) {
            cleanup.cleanup();
        }

        if (this.decompiler instanceof final Cleanup cleanup) {
            cleanup.cleanup();
        }
    }
}
