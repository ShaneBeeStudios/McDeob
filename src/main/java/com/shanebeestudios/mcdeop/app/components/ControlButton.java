package com.shanebeestudios.mcdeop.app.components;

import com.shanebeestudios.mcdeop.VersionManager;
import com.shanebeestudios.mcdeop.app.App;
import com.shanebeestudios.mcdeop.launchermeta.data.release.ReleaseManifest;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import com.shanebeestudios.mcdeop.processor.ProcessorOptions;
import com.shanebeestudios.mcdeop.processor.ResourceRequest;
import com.shanebeestudios.mcdeop.processor.SourceType;
import com.shanebeestudios.mcdeop.util.Util;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.Executors;
import javax.swing.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class ControlButton extends JButton {
    private static final String DEFAULT_TEXT = "Start!";
    private static final int WIDTH = 200;
    private static final int HEIGHT = 50;

    private final int heightDivided;

    @Getter
    @Setter
    private boolean ready = true;

    public ControlButton(final App app) {
        super(DEFAULT_TEXT);

        if (Util.isRunningMacOS()) {
            this.heightDivided = HEIGHT / 2;
        } else {
            // makes the spacing of the button look better on windows
            this.heightDivided = Math.round(HEIGHT / 1.25F);
        }

        this.addActionListener(new StartButtonListener(app, this));
    }

    public void reset() {
        this.setText(DEFAULT_TEXT);
        this.setForeground(Color.BLACK);
        this.setReady(true);
    }

    @RequiredArgsConstructor
    static class StartButtonListener implements ActionListener {
        private final App app;
        private final ControlButton button;

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!e.getSource().equals(this.button)) {
                return;
            }

            if (!this.button.isReady()) {
                return;
            }

            final Version version = (Version) this.app.getVersionBox().getSelectedItem();
            if (version == null) {
                this.app.updateStatusBox("INVALID VERSION!");
                this.app.getToolkit().beep();
                Executors.newSingleThreadScheduledExecutor()
                        .schedule(this.button::reset, 1, java.util.concurrent.TimeUnit.SECONDS);
                return;
            }

            final SourceType type = this.app.getServer().isSelected() ? SourceType.SERVER : SourceType.CLIENT;
            final ProcessorOptions options = this.app.getProcessorOptionsGroup().getProcessorOptions();

            final ReleaseManifest releaseManifest;
            try {
                releaseManifest = VersionManager.getInstance().getReleaseManifest(version);
            } catch (final IOException ex) {
                this.app.updateStatusBox("FAILED TO FETCH RELEASE MANIFEST");
                this.app.getToolkit().beep();
                Executors.newSingleThreadScheduledExecutor()
                        .schedule(this.button::reset, 1, java.util.concurrent.TimeUnit.SECONDS);
                return;
            }

            final ResourceRequest resourceRequest = new ResourceRequest(releaseManifest, type);
            this.button.setText("Running...");
            this.button.setForeground(Color.BLUE);
            this.app.start(resourceRequest, options);
        }
    }
}
