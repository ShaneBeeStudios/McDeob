package com.shanebeestudios.mcdeop.app.components;

import javax.swing.*;

public class ClientVersionTypeButton extends JRadioButton implements DynamicDimension {
    public ClientVersionTypeButton() {
        super("Client");

        this.setSelected(true);
    }

    @Override
    public void updateDimension(final int newWidth, final int newHeight) {
        this.setBounds(newWidth / 2 - (100 - 10), 60, 100, 20);
    }
}
