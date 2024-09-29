package com.shanebeestudios.mcdeop;

import com.shanebeestudios.mcdeop.app.App;
import com.shanebeestudios.mcdeop.util.Logger;
import com.shanebeestudios.mcdeop.util.TimeStamp;
import com.shanebeestudios.mcdeop.util.Util;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Processor {

    private final Version version;
    private final boolean decompile;
    private final App app;

    private Path jarPath;
    private Path mappingsPath;
    private Path remappedJar;

    private String minecraftJarName;
    private String mappingsName;
    private String mappedJarName;

    private Path dataFolderPath = Paths.get("deobf-work");

    public Processor(Version version, boolean decompile, App app) {
        this.version = version;
        this.decompile = decompile;
        this.app = app;
        if (Util.isRunningMacOS()) {
            // If running on macOS, put the output directory in the user home directory.
            // This is due to how macOS APPs work — their '.' directory resolves to one inside the APP itself.
            this.dataFolderPath = Paths.get(System.getProperty("user.home"), "McDeob");
        }
        try {
            Files.createDirectories(this.dataFolderPath);
        } catch (IOException ignore) {
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void init() {
        try {
            long start = System.currentTimeMillis();

            // Prepare version and check if valid
            String versionName = this.version.getVersion();
            if (!this.version.prepareVersion()) {
                if (this.app != null) {
                    this.app.fail();
                } else {
                    System.out.println("Invalid version: " + versionName);
                }
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
        if (this.app != null) {
            this.app.updateStatusBox("Downloading JAR...");
            this.app.updateButton("Downloading JAR...", Color.BLUE);
        }

        this.jarPath = this.dataFolderPath.resolve(this.minecraftJarName);
        final URL jarURL = new URL(this.version.getJarURL());
        final HttpURLConnection connection = (HttpURLConnection) jarURL.openConnection();
        final long length = connection.getContentLengthLong();
        if (Files.exists(jarPath) && Files.size(jarPath) == length) {
            Logger.info("Already have JAR, skipping download.");
        } else try (final InputStream inputStream = connection.getInputStream()) {
            Files.copy(inputStream, jarPath, REPLACE_EXISTING);
        }

        TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Successfully downloaded JAR file in %s!", timeStamp);
    }

    public void downloadMappings() throws IOException {
        long start = System.currentTimeMillis();
        Logger.info("Downloading mappings file from Mojang...");
        if (this.app != null) {
            this.app.updateStatusBox("Downloading mappings...");
            this.app.updateButton("Downloading mappings...", Color.BLUE);
        }
        final URL mappingURL = new URL(this.version.getMapURL());
        final HttpURLConnection connection = (HttpURLConnection) mappingURL.openConnection();
        final long length = connection.getContentLengthLong();
        this.mappingsPath = this.dataFolderPath.resolve(this.mappingsName);
        if (Files.exists(this.mappingsPath) && Files.size(this.mappingsPath) == length) {
            Logger.info("Already have mappings, skipping download.");
        } else try (final InputStream inputStream = connection.getInputStream()) {
            Files.copy(inputStream, this.mappingsPath, REPLACE_EXISTING);
        }

        TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Successfully downloaded mappings file in %s!", timeStamp);
    }

    public void remapJar() {
        long start = System.currentTimeMillis();
        if (this.app != null) {
            this.app.updateStatusBox("Remapping...");
            this.app.updateButton("Remapping...", Color.BLUE);
        }
        this.remappedJar = this.dataFolderPath.resolve(this.mappedJarName);

        if (!Files.exists(remappedJar)) {
            Logger.info("Remapping %s file...", this.minecraftJarName);

            try {
                // SpecialSource remapping
                JarMapping jarMapping = new JarMapping();
                jarMapping.loadMappings(new File(this.mappingsPath.toUri()));

                JointProvider inheritanceProvider = new JointProvider();
                jarMapping.setFallbackInheritanceProvider(inheritanceProvider);

                Jar inJar = Jar.init(new File(this.jarPath.toUri()));

                SpecialSource.kill_lvt = true;
                JarRemapper jarRemapper = new JarRemapper(jarMapping);

                Jar internal = getInternalJars(inJar);
                inheritanceProvider.add(new JarProvider(internal));
                jarRemapper.remapJar(internal, new File(this.remappedJar.toUri()));
                inJar.close();
                internal.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            TimeStamp timeStamp = TimeStamp.fromNow(start);
            Logger.info("Remapping completed in %s!", timeStamp);
        } else {
            Logger.info("%s already remapped... skipping mapping.", this.mappedJarName);
        }
    }

    public void decompileJar() throws IOException {
        long start = System.currentTimeMillis();
        Logger.info("Decompiling final JAR file.");
        if (this.app != null) {
            this.app.updateStatusBox("Decompiling... This will take a while!");
            this.app.updateButton("Decompiling...", Color.BLUE);
        }
        final Path decompileDir = this.dataFolderPath.resolve("final-decompile");
        Files.createDirectories(decompileDir);

        // Setup FernFlower to properly decompile the jar file
        String[] args = new String[]{
            "-dgs=1", "-hdc=0", "-rbr=0",
            "-asc=1", "-udv=0", "-rsy=1", "-aoa=1",
            this.remappedJar.toAbsolutePath().toString(),
            decompileDir.toAbsolutePath().toString()
        };

        ConsoleDecompiler.main(args);

        // Rename jar file to zip
        try (final Stream<Path> stream = Files.list(decompileDir)) {
            for (final Path path : (Iterable<Path>) stream::iterator) {
                final String filename = path.getFileName().toString();
                if (!filename.contains(".jar")) continue;
                final int index = filename.lastIndexOf('.');
                Files.move(path, path.resolveSibling(Path.of(filename.substring(0, index) + ".zip")));
            }
        }

        TimeStamp timeStamp = TimeStamp.fromNow(start);
        Logger.info("Decompiling completed in %s!", timeStamp);
    }

    private void cleanup() {
        this.jarPath = null;
        this.mappingsPath = null;
        this.remappedJar = null;
        System.gc();
    }

    // Server jars have a bundled setup now
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Jar getInternalJars(Jar jarFile) {
        String ver = this.version.getVersion();

        // Client and older server's don't use the bundler
        if (!jarFile.containsResource("META-INF/versions/" + ver + "/server-" + ver + ".jar")) {
            return jarFile;
        } else {
            try {
                List<File> files = new ArrayList<>();

                // Include Mojang libraries and server jar
                for (String entryName : jarFile.getEntryNames()) {
                    if ((entryName.contains("server-") || entryName.contains("libraries/com/mojang")) && entryName.contains(".jar")) {
                        String pathName = this.dataFolderPath + "/" + entryName.substring(entryName.lastIndexOf('/') + 1);
                        File file = Util.copyInputStreamToFile(jarFile.getResource(entryName), pathName);
                        files.add(file);
                    }
                }
                Jar internalJar = Jar.init(files);
                // Cleanup files now that they're jars
                files.forEach(File::delete);
                return internalJar;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
