package com.shanebeestudios.mcdeop.app.components;

import com.shanebeestudios.mcdeop.VersionManager;
import com.shanebeestudios.mcdeop.app.App;
import com.shanebeestudios.mcdeop.launchermeta.data.release.ReleaseManifest;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import com.shanebeestudios.mcdeop.processor.ProcessorOptions;
import com.shanebeestudios.mcdeop.processor.ResourceRequest;
import com.shanebeestudios.mcdeop.processor.SourceType;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.Executors;
import javax.swing.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
public class ControlButton extends JButton {
    private static final String DEFAULT_TEXT = "Start!";

    @Setter
    private boolean ready = true;

    public ControlButton(final App app, final VersionManager versionManager) {
        super(DEFAULT_TEXT);

        this.addActionListener(new StartButtonListener(app, versionManager));
    }

    public void reset() {
        this.setText(DEFAULT_TEXT);
        this.setForeground(Color.BLACK);
        this.setReady(true);
    }

    @RequiredArgsConstructor
    class StartButtonListener implements ActionListener {
        private final App app;
        private final VersionManager versionManager;

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!e.getSource().equals(ControlButton.this)) {
                return;
            }

            if (!ControlButton.this.isReady()) {
                return;
            }

            final Version version = (Version) this.app.getVersionBox().getSelectedItem();
            if (version == null) {
                this.app.updateStatusBox("INVALID VERSION!");
                this.app.getToolkit().beep();
                Executors.newSingleThreadScheduledExecutor()
                        .schedule(ControlButton.this::reset, 1, java.util.concurrent.TimeUnit.SECONDS);
                return;
            }

            final SourceType type = this.app.getServer().isSelected() ? SourceType.SERVER : SourceType.CLIENT;
            final ProcessorOptions options = this.app.getProcessorOptionsGroup().getProcessorOptions();

            final ReleaseManifest releaseManifest;
            try {
                releaseManifest = this.versionManager.getReleaseManifest(version);
            } catch (final IOException ex) {
                this.app.updateStatusBox("FAILED TO FETCH RELEASE MANIFEST");
                this.app.getToolkit().beep();
                Executors.newSingleThreadScheduledExecutor()
                        .schedule(ControlButton.this::reset, 1, java.util.concurrent.TimeUnit.SECONDS);
                return;
            }

            final ResourceRequest resourceRequest = new ResourceRequest(releaseManifest, type);
            ControlButton.this.setText("Running...");
            ControlButton.this.setForeground(Color.BLUE);
            this.app.start(resourceRequest, options);
        }
    }
}
