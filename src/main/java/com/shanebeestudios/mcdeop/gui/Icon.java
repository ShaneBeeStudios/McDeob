package com.shanebeestudios.mcdeop.gui;

import com.shanebeestudios.mcdeop.McDeob;

import javax.swing.*;
import java.awt.*;

public class Icon {

    public static final ImageIcon DOCK_LOGO = getScaledIcon("images/1024.png", "main logo", 1024, 1024);

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
