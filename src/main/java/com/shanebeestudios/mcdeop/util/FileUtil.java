package com.shanebeestudios.mcdeop.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil
{
    public static void remove(final Path path) throws IOException
    {
        if (!path.toFile().exists())
        {
            return;
        }

        try (Stream<Path> files = Files.walk(path))
        {
            files.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    public static void zip(final Path sourceDirPath, final Path zipFilePath) throws IOException
    {
        final Path zipFile = Files.createFile(zipFilePath);
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(zipFile));
             Stream<Path> paths = Files.walk(sourceDirPath))
        {
            for (final Iterator<Path> it = paths.iterator(); it.hasNext(); )
            {
                final Path path = it.next();
                if (Files.isDirectory(path))
                {
                    continue;
                }

                final ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                zs.putNextEntry(zipEntry);
                Files.copy(path, zs);
                zs.closeEntry();
            }
        }
    }
}
