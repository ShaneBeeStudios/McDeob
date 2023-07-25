package com.shanebeestudios.mcdeop.app.components;

import javax.swing.*;

public class ServerVersionTypeButton extends JRadioButton implements DynamicDimension {
    public ServerVersionTypeButton() {
        super("Server");
    }

    @Override
    public void updateDimension(final int newWidth, final int newHeight) {
        this.setBounds(newWidth / 2 + 10, 60, 100, 20);
    }
}
