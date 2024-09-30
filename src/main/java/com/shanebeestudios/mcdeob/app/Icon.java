package com.shanebeestudios.mcdeob.app;

import com.shanebeestudios.mcdeob.McDeob;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Icon {

    private static final List<Image> LOGO_IMAGES = new ArrayList<>();

    static {
        URL imgURL = McDeob.class.getClassLoader().getResource("images/1024.png");
        if (imgURL != null) {
            for (int size = 16; size <= 1024; size *= 2) {
                ImageIcon imageIcon = new ImageIcon(imgURL, "main logo x" + size);
                Image newImage = imageIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                LOGO_IMAGES.add(newImage);
            }
        } else {
            System.err.println("Couldn't find file: 'images/1024.png'");
        }
    }

    public static List<Image> getLogoImages() {
        return LOGO_IMAGES;
    }

    public static Image getLogoForMacOs() {
        return LOGO_IMAGES.get(LOGO_IMAGES.size() - 1);
    }

}
