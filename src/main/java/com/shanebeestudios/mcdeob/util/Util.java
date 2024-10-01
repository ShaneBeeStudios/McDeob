package com.shanebeestudios.mcdeob.util;

import com.shanebeestudios.mcdeob.Processor;
import net.md_5.specialsource.Jar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler;
import org.jetbrains.java.decompiler.main.decompiler.ThreadedPrintStreamLogger;
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
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

    public static boolean isRunningMacOS() {
        return System.getProperty("os.name").contains("Mac OS");
    }

    /**
     * Get a JsonObject from a URL
     *
     * @param url URL to grab data from
     * @return JsonObject with retrieved data
     * @throws IOException Error thrown if data cannot be retrieved
     */
    public static JSONObject getJsonFromURL(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final JSONObject jsonObject = new JSONObject(new JSONTokener(in));
        in.close();
        return jsonObject;
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

    /**
     * Create a FernFlower decompiler
     *
     * @param source      Source path for decompiling
     * @param destination Destination path for where file will output
     * @return A new decompiler
     */
    public static @NotNull ConsoleDecompiler getConsoleDecompiler(Path source, Path destination) {
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("dgs", "1"); // decompile generic signatures
        settings.put("hdc", "0"); // hide empty default constructor
        settings.put("asc", "1"); // encode non-ASCII characters in string and character literals as Unicode escapes
        settings.put("udv", "0"); // reconstruct variable names from debug information, if present
        settings.put("rsy", "1"); // hide synthetic class members
        settings.put("aoa", "1"); // (not listed in FernFlower's list)
        IFernflowerLogger logger = new ThreadedPrintStreamLogger(System.out);
        ConsoleDecompiler decompiler = new ConsoleDecompiler(new File(destination.toUri()), settings, logger);
        decompiler.addSource(new File(source.toUri()));
        return decompiler;
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

}
