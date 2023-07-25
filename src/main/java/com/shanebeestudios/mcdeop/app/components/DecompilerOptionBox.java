package com.shanebeestudios.mcdeop.app.components;

import javax.swing.*;

public class DecompilerOptionBox extends JCheckBox implements DynamicDimension {
    public DecompilerOptionBox() {
        super("Decompile?");

        this.setSelected(false);
    }

    @Override
    public void updateDimension(final int newWidth, final int newHeight) {
        this.setBounds((newWidth / 2) - 60, 125, 120, 30);
    }
}
