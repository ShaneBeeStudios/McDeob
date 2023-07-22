package com.shanebeestudios.mcdeop.app;

import com.shanebeestudios.mcdeop.McDeob;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

@SuppressWarnings({"unused", "ConstantConditions"})
public class Icon {

    public static final ImageIcon DOCK_LOGO_1024 = getScaledIcon("images/1024.png", "main logo", 1024, 1024);
    public static final ImageIcon DOCK_LOGO_512 = getScaledIcon("images/1024.png", "main logo", 512, 512);
    public static final ImageIcon DOCK_LOGO_256 = getScaledIcon("images/1024.png", "main logo", 256, 256);
    public static final ImageIcon DOCK_LOGO_128 = getScaledIcon("images/1024.png", "main logo", 128, 128);
    public static final ImageIcon DOCK_LOGO_64 = getScaledIcon("images/1024.png", "main logo", 64, 64);
    public static final ImageIcon DOCK_LOGO_32 = getScaledIcon("images/1024.png", "main logo", 32, 32);
    public static final ImageIcon DOCK_LOGO_16 = getScaledIcon("images/1024.png", "main logo", 16, 16);

    public static final List<Image> LOGO_IMAGES = new ArrayList<>();

    static {
        LOGO_IMAGES.add(DOCK_LOGO_1024.getImage());
        LOGO_IMAGES.add(DOCK_LOGO_512.getImage());
        LOGO_IMAGES.add(DOCK_LOGO_256.getImage());
        LOGO_IMAGES.add(DOCK_LOGO_128.getImage());
        LOGO_IMAGES.add(DOCK_LOGO_64.getImage());
        LOGO_IMAGES.add(DOCK_LOGO_32.getImage());
        LOGO_IMAGES.add(DOCK_LOGO_16.getImage());
    }

    private static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = McDeob.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private static ImageIcon getScaledIcon(String path, String description, int w, int h) {
        java.net.URL imgURL = McDeob.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            ImageIcon i = new ImageIcon(imgURL, description);
            Image image = i.getImage();
            Image newImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(newImage);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
