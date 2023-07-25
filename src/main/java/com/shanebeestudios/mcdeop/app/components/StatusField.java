package com.shanebeestudios.mcdeop.app.components;

import javax.swing.*;

public class StatusField extends JTextField implements DynamicDimension {
    public StatusField() {
        super("Status!");

        this.setEditable(false);
    }

    @Override
    public void updateDimension(final int newWidth, final int newHeight) {
        final int width = (int) (newWidth * 0.90);
        this.setBounds((newWidth / 2) - (width / 2), 160, width, 30);
    }
}
