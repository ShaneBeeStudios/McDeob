package com.shanebeestudios.mcdeop.app.components;

import com.shanebeestudios.mcdeop.VersionManager;
import com.shanebeestudios.mcdeop.app.App;
import com.shanebeestudios.mcdeop.launchermeta.data.release.ReleaseManifest;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import com.shanebeestudios.mcdeop.processor.ResourceRequest;
import com.shanebeestudios.mcdeop.processor.SourceType;
import com.shanebeestudios.mcdeop.util.Util;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ControlButton extends JButton implements DynamicDimension {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 50;

    private final int heightDivided;

    public ControlButton(final App app) {
        super("Start!");

        if (Util.isRunningMacOS()) {
            this.heightDivided = HEIGHT / 2;
        } else {
            // makes the spacing of the button look better on windows
            this.heightDivided = Math.round(HEIGHT / 1.25F);
        }

        this.addActionListener(new StartButtonListener(app, this));
    }

    @Override
    public void updateDimension(final int newWidth, final int newHeight) {
        this.setBounds((newWidth / 2) - (WIDTH / 2), 180 + this.heightDivided, WIDTH, HEIGHT);
    }

    @RequiredArgsConstructor
    static class StartButtonListener implements ActionListener {
        private final App app;
        private final JButton button;

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!e.getSource().equals(this.button)) {
                return;
            }

            if (!this.button.getText().equalsIgnoreCase("Start!")) {
                return;
            }

            final Version version = (Version) this.app.getVersionBox().getSelectedItem();
            if (version == null) {
                this.app.updateButton("INVALID VERSION!", Color.RED);
                this.app.getToolkit().beep();
                final Timer timer = new Timer(1000, e1 -> this.app.updateButton("Start!"));
                timer.setRepeats(false);
                timer.start();
                return;
            }

            final SourceType type = this.app.getServer().isSelected() ? SourceType.SERVER : SourceType.CLIENT;
            final boolean decomp = this.app.getDecompile().isSelected();

            final ReleaseManifest releaseManifest;
            try {
                releaseManifest = VersionManager.getInstance().getReleaseManifest(version);
            } catch (final IOException ex) {
                this.app.updateButton("FAILED TO FETCH RELEASE MANIFEST", Color.RED);
                this.app.getToolkit().beep();
                final Timer timer = new Timer(1000, e1 -> this.app.updateButton("Start!"));
                timer.setRepeats(false);
                timer.start();
                return;
            }

            final ResourceRequest resourceRequest = new ResourceRequest(releaseManifest, type);
            this.app.updateButton("Starting...", Color.BLUE);
            this.app.start(resourceRequest, decomp);
        }
    }
}
