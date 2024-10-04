package com.shanebeestudios.mcdeob.app.listener;

import com.shanebeestudios.mcdeob.app.App;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SnapshotButtonListener implements ActionListener {

    private final App app;

    public SnapshotButtonListener(App app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JToggleButton snapshotToggleButton = this.app.getSnapshotToggleButton();
        if (snapshotToggleButton.isSelected()) {
            snapshotToggleButton.setSelected(true);
            snapshotToggleButton.setText("Toggle Releases");
            this.app.setupVersions(true);
        } else {
            snapshotToggleButton.setSelected(false);
            snapshotToggleButton.setText("Toggle Snapshots");
            this.app.setupVersions(false);
        }
    }

}
