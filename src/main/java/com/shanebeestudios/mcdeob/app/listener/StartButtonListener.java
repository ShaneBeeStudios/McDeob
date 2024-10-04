package com.shanebeestudios.mcdeob.app.listener;

import com.shanebeestudios.mcdeob.version.Version;
import com.shanebeestudios.mcdeob.app.App;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartButtonListener implements ActionListener {

    private final App app;

    public StartButtonListener(App app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.app.getStartButton()) {
            Version version = (Version) this.app.getVersionBox().getSelectedItem();
            if (!this.app.getStartButton().getText().equalsIgnoreCase("Start!")) return;
            if (version == null) {
                this.app.fail();
            } else {
                version.setType(this.app.getServerRadioButton().isSelected() ? Version.Type.SERVER : Version.Type.CLIENT);
                this.app.updateButton("Starting...", Color.BLUE);
                this.app.start(version, this.app.getDecompileCheckbox().isSelected());
            }
        }
    }

}
