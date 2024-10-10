package com.shanebeestudios.mcdeob.util;

import com.shanebeestudios.mcdeob.Processor;
import net.md_5.specialsource.Jar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Util {

    public static final Color TITLE_LOADING_COLOR = new Color(227, 184, 43);
    public static final Color TITLE_READY_COLOR = new Color(63, 199, 82);
    public static final Color TITLE_FAIL_COLOR = new Color(204, 25, 25);

    public static boolean isRunningMacOS() {
        return System.getProperty("os.name").contains("Mac OS");
    }

    /**
     * Get a JsonObject from a URL
     *
     * @param url URL to grab data from
     * @return JsonObject with retrieved data
     */
    @Nullable
    public static JSONObject getJsonFromURL(String url) {
        try {
            URLConnection connection = URI.create(url).toURL().openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final JSONObject jsonObject = new JSONObject(new JSONTokener(in));
            in.close();
            return jsonObject;
        } catch (IOException ex) {
            //noinspection CallToPrintStackTrace
            ex.printStackTrace();
        }
        return null;
    }

    public static File copyInputStreamToFile(InputStream inputStream, String pathName) throws IOException {
        File returnFile = new File(pathName);
        try (FileOutputStream outputStream = new FileOutputStream(returnFile, false)) {
            int read;
            byte[] bytes = new byte[8192];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
        return returnFile;
    }

    public static @NotNull Map<String, Object> getDecompilerParams() {
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("dgs", "1"); // decompile generic signatures
        settings.put("hdc", "0"); // hide empty default constructor
        settings.put("asc", "1"); // encode non-ASCII characters in string and character literals as Unicode escapes
        settings.put("udv", "0"); // reconstruct variable names from debug information, if present
        settings.put("rsy", "1"); // hide synthetic class members
        settings.put("aoa", "1"); // (not listed in FernFlower's list)
        return settings;
    }

    public static void renameJarsToZips(Path directory) {
        try (final Stream<Path> stream = Files.list(directory)) {
            for (final Path path : (Iterable<Path>) stream::iterator) {
                final String filename = path.getFileName().toString();
                if (!filename.contains(".jar")) continue;
                final int index = filename.lastIndexOf('.');
                Files.move(path, path.resolveSibling(Path.of(filename.substring(0, index) + ".zip")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the server/client jar.
     * <p>Newer versions of the Minecraft server have bundled several
     * jars inside the main server.jar file.</p>
     *
     * @param processor Processor that holds data for file paths
     * @return Internal jar files inside Minecraft jars
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static Jar getInternalJars(Processor processor) {
        Jar jarFile;
        try {
            jarFile = Jar.init(new File(processor.getJarPath().toUri()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String ver = processor.getVersion().getVersion();

        // Client and older server's don't use the bundler
        if (!jarFile.containsResource("META-INF/versions/" + ver + "/server-" + ver + ".jar")) {
            return jarFile;
        } else {
            try {
                List<File> files = new ArrayList<>();

                // Include Mojang libraries and server jar
                for (String entryName : jarFile.getEntryNames()) {
                    if ((entryName.contains("server-") || entryName.contains("libraries/com/mojang")) && entryName.contains(".jar")) {
                        String pathName = processor.getDataFolderPath() + "/" + entryName.substring(entryName.lastIndexOf('/') + 1);
                        File file = Util.copyInputStreamToFile(jarFile.getResource(entryName), pathName);
                        files.add(file);
                    }
                }
                Jar internalJar = Jar.init(files);
                // Cleanup files now that they're jars
                files.forEach(File::delete);
                jarFile.close();
                return internalJar;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    public static void stripFileFromJar(Path path, String fileName) {
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"zip", "-d", path.toAbsolutePath().toString(), fileName});
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // This does nothing, but for some reason without it, it won't strip out the files?!?!?!
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException ignore) {
        }
    }

    public static void printSystemInfo() {
        Thread.currentThread().setName("McDeob");
        long maxMemory = Runtime.getRuntime().maxMemory();
        Logger.info("Memory: %sMB", maxMemory / 1024 / 1024);
        String javaVersion = System.getProperty("java.vm.version");
        String javaName = System.getProperty("java.vm.name");
        String javaVendor = System.getProperty("java.vm.vendor");
        Logger.info("Java: %s [%s - %s]", javaVersion, javaVendor, javaName);
        String osArch = System.getProperty("os.arch");
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        Logger.info("OS: %s [%s - v.%s]", osName, osArch, osVersion);
    }

}
