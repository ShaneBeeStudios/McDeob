package com.shanebeestudios.mcdeob.util;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Util {

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

}
