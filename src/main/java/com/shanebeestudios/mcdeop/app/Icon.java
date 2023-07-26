package com.shanebeestudios.mcdeop.app;

import com.shanebeestudios.mcdeop.McDeob;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@SuppressWarnings({"unused", "ConstantConditions"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Icon {
    public static final ImageIcon DOCK_LOGO_1024 = getAppIcon(1024, 1024);
    public static final ImageIcon DOCK_LOGO_512 = getAppIcon(512, 512);
    public static final ImageIcon DOCK_LOGO_256 = getAppIcon(256, 256);
    public static final ImageIcon DOCK_LOGO_128 = getAppIcon(128, 128);
    public static final ImageIcon DOCK_LOGO_64 = getAppIcon(64, 64);
    public static final ImageIcon DOCK_LOGO_32 = getAppIcon(32, 32);
    public static final ImageIcon DOCK_LOGO_16 = getAppIcon(16, 16);

    public static final List<Image> LOGO_IMAGES = List.of(
            DOCK_LOGO_1024.getImage(),
            DOCK_LOGO_512.getImage(),
            DOCK_LOGO_256.getImage(),
            DOCK_LOGO_128.getImage(),
            DOCK_LOGO_64.getImage(),
            DOCK_LOGO_32.getImage(),
            DOCK_LOGO_16.getImage());

    private static ImageIcon getAppIcon(final int width, final int height) {
        return getScaledIcon("images/1024.png", "main logo", width, height);
    }

    private static ImageIcon getScaledIcon(final String path, final String description, final int w, final int h) {
        final java.net.URL imgURL = McDeob.class.getClassLoader().getResource(path);
        if (imgURL == null) {
            log.error("Couldn't find file: {}", path);
            throw new IllegalStateException("Couldn't find file: " + path);
        }

        final ImageIcon icon = new ImageIcon(imgURL, description);
        final Image image = icon.getImage();
        final Image newImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
}
