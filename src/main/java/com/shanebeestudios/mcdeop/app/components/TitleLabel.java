package com.shanebeestudios.mcdeop.app.components;

import javax.swing.*;

public class TitleLabel extends JLabel implements DynamicDimension {
    public TitleLabel() {
        super("Let's start de-obfuscating some Minecraft", SwingConstants.CENTER);

        this.setHorizontalTextPosition(SwingConstants.CENTER);
    }

    @Override
    public void updateDimension(final int newWidth, final int newHeight) {
        this.setBounds(0, 10, newWidth, 50);
    }
}
