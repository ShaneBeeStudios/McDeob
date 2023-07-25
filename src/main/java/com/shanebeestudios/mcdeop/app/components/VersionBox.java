package com.shanebeestudios.mcdeop.app.components;

import com.shanebeestudios.mcdeop.VersionManager;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class VersionBox extends JComboBox<Version> implements DynamicDimension {
    public VersionBox() {
        this.setRenderer(new VersionRenderer());

        for (final Version version : this.getVersions()) {
            this.addItem(version);
        }

        this.setSelectedIndex(0);
        this.setBackground(Color.lightGray);
    }

    private Collection<Version> getVersions() {
        return VersionManager.getInstance().getVersions();
    }

    @Override
    public void updateDimension(final int newWidth, final int newHeight) {
        this.setBounds((newWidth / 2) - this.getWidth() / 2, 95, this.getPreferredSize().width, 25);
    }

    private static class VersionRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                final JList list,
                final Object value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus) {
            final JLabel label =
                    (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            final Version version = (Version) value;
            label.setText(version.getId());
            return label;
        }
    }
}
