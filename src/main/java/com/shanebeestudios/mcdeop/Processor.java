package com.shanebeestudios.mcdeop;

import com.shanebeestudios.mcdeop.app.App;
import com.shanebeestudios.mcdeop.util.FileUtil;
import com.shanebeestudios.mcdeop.util.TimeStamp;
import com.shanebeestudios.mcdeop.util.Util;
import io.github.lxgaming.reconstruct.common.Reconstruct;
import io.github.lxgaming.reconstruct.common.manager.TransformerManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.function.Consumer;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Slf4j
public class Processor {
    /**
     * {@link Reconstruct} breaks if you run it multiple times because all the transformers are cached with the first config every initialized.
     * This is a nasty hack to clear the static fields in {@link TransformerManager} so that we can run it multiple times.
     */
    @SneakyThrows
    private static void fixReconstruct() {
        log.debug("Fix Reconstruct");
        final Class<TransformerManager> affectedClass = TransformerManager.class;
        final Field[] declaredFields = affectedClass.getDeclaredFields();
        for (final Field field : declaredFields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);
            final Object fieldObject = field.get(affectedClass);
            if (fieldObject instanceof Set) {
                log.debug("Clear Set field in Reconstruct: {}", field.getName());
                ((Set<?>) fieldObject).clear();
            }
        }
    }

    public static void runProcessor(final ResourceRequest version, final boolean decompile, final App app) {
        try {
            fixReconstruct();

            final Processor processor = new Processor(version, decompile, app);
            processor.init();
        } catch (final Exception e) {
            log.error("Failed to run processor", e);
        } finally {
            Util.forceGC();
        }
    }

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
        this.reconstruct = this.createReconstruct();
    }

    private Reconstruct createReconstruct() {
        final ReconConfig config = new ReconConfig();

        config.setInputPath(this.jarPath.toAbsolutePath());
        config.setMappingPath(this.mappingsPath.toAbsolutePath());
        config.setOutputPath(this.remappedJar.toAbsolutePath());

        return new Reconstruct(config);
    }

    private void handleGui(final Consumer<App> guiConsumer) {
        if (this.app != null) {
            guiConsumer.accept(this.app);
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

        try {
            final long start = System.currentTimeMillis();
            this.handleGui(App::toggleControls);

            this.downloadJar();
            this.downloadMappings();
            this.remapJar();
            if (this.decompile) {
                this.decompileJar();
            }

            final TimeStamp timeStamp = TimeStamp.fromNow(start);
            log.info("Completed in {}!", timeStamp);
            this.handleGui(gui -> {
                gui.updateStatusBox(String.format("Completed in %s!", timeStamp));
                gui.updateButton("Start!");
            });

        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            this.handleGui(App::toggleControls);
        }
    }

    public void downloadJar() throws IOException {
        final long start = System.currentTimeMillis();
        log.info("Downloading JAR file from Mojang.");
        this.handleGui(gui -> {
            gui.updateStatusBox("Downloading JAR...");
            gui.updateButton("Downloading JAR...", Color.BLUE);
        });

        final HttpURLConnection connection = (HttpURLConnection) this.jarUrl.openConnection();
        final long length = connection.getContentLengthLong();
        if (Files.exists(this.jarPath) && Files.size(this.jarPath) == length) {
            log.info("Already have JAR, skipping download.");
        } else {
            try (final InputStream inputStream = connection.getInputStream()) {
                Files.copy(inputStream, this.jarPath, REPLACE_EXISTING);
            }
        }

        final TimeStamp timeStamp = TimeStamp.fromNow(start);
        log.info("Successfully downloaded JAR file in {}!", timeStamp);
    }

    public void downloadMappings() throws IOException {
        final long start = System.currentTimeMillis();
        log.info("Downloading mappings file from Mojang...");
        this.handleGui(gui -> {
            gui.updateStatusBox("Downloading mappings...");
            gui.updateButton("Downloading mappings...", Color.BLUE);
        });

        final HttpURLConnection connection = (HttpURLConnection) this.mappingsUrl.openConnection();
        final long length = connection.getContentLengthLong();
        if (Files.exists(this.mappingsPath) && Files.size(this.mappingsPath) == length) {
            log.info("Already have mappings, skipping download.");
        } else {
            try (final InputStream inputStream = connection.getInputStream()) {
                Files.copy(inputStream, this.mappingsPath, REPLACE_EXISTING);
            }
        }

        final TimeStamp timeStamp = TimeStamp.fromNow(start);
        log.info("Successfully downloaded mappings file in {}!", timeStamp);
    }

    public void remapJar() {
        final long start = System.currentTimeMillis();
        this.handleGui(gui -> {
            gui.updateStatusBox("Remapping...");
            gui.updateButton("Remapping...", Color.BLUE);
        });

        if (!Files.exists(this.remappedJar)) {
            log.info("Remapping {} file...", this.minecraftJarName);
            this.reconstruct.load();

            final TimeStamp timeStamp = TimeStamp.fromNow(start);
            log.info("Remapping completed in {}!", timeStamp);
        } else {
            log.info("{} already remapped... skipping mapping.", this.mappedJarName);
        }
    }

    public void decompileJar() throws IOException {
        final long start = System.currentTimeMillis();
        log.info("Decompiling final JAR file.");
        this.handleGui(gui -> {
            gui.updateStatusBox("Decompiling... This will take a while!");
            gui.updateButton("Decompiling...", Color.BLUE);
        });

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
        log.info("Packing decompiled files into {}", zipFilePath);
        FileUtil.remove(zipFilePath);
        FileUtil.zip(decompileJarDir, zipFilePath);

        final TimeStamp timeStamp = TimeStamp.fromNow(start);
        log.info("Decompiling completed in {}!", timeStamp);
    }
}
