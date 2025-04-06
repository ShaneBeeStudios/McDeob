package com.shanebeestudios.mcdeob.app;

import com.shanebeestudios.mcdeob.Processor;
import com.shanebeestudios.mcdeob.app.listener.SnapshotButtonListener;
import com.shanebeestudios.mcdeob.app.listener.StartButtonListener;
import com.shanebeestudios.mcdeob.util.TimeSpan;
import com.shanebeestudios.mcdeob.util.Util;
import com.shanebeestudios.mcdeob.version.Version;
import com.shanebeestudios.mcdeob.version.Versions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.Field;

public class App extends JFrame {

    private JLabel infoLineLabel;
    private JButton startButton;
    private JRadioButton serverRadioButton;
    private JRadioButton clientRadioButton;
    private JToggleButton snapshotToggleButton;
    private JCheckBox decompileCheckbox;
    private JComboBox<Version> versionBox;
    private JTextField statusBox;

    public App() {
        // Create window and components
        startSetup();

        // Initialize versions
        if (!Versions.initVersions()) {
            updateInfoLine("Failed to load versions. Are you connected to the internet?", Util.TITLE_FAIL_COLOR);
            return;
        }

        // Update window after versions initialized
        finishSetup();
    }

    private void startSetup() {
        if (Util.isRunningMacOS()) {
            // If we're running on Mac, set the logo
            Taskbar.getTaskbar().setIconImage(Icon.getLogoForMacOs());
        } else {
            // Else we set it this way
            setIconImages(Icon.getLogoImages());
        }

        setupWindow();
        createInfoLine();
        createTypeButton();
        createVersionPopup();
        createDecompileButton();
        createStatusBox();
        createStartButton();
        setVisible(true);
        toggleControls();
    }

    private void setupWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 335);
        setTitle("McDeob");

        try { // Window title hack for GTK
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            Field nameField = defaultToolkit.getClass().getDeclaredField("awtAppClassName");
            nameField.setAccessible(true);
            nameField.set(defaultToolkit, "McDeob");
        } catch (Exception ignored) {
            // We're probably just not on XToolkit
        }

        // Window title hack for macOS
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "McDeob");

        setResizable(true);
        setMinimumSize(new Dimension(500, 335));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setLayout(null);
    }

    private void createInfoLine() {
        this.infoLineLabel = new JLabel("Initializing versions, please wait...", SwingConstants.CENTER);
        this.infoLineLabel.setForeground(Util.TITLE_LOADING_COLOR);
        this.infoLineLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        hookSize(() -> this.infoLineLabel.setBounds(0, 10, getSize().width, 50));
        add(this.infoLineLabel);
    }

    public void updateInfoLine(String text, Color color) {
        this.infoLineLabel.setText(text);
        this.infoLineLabel.setForeground(color);
    }

    private void createTypeButton() {
        this.serverRadioButton = new JRadioButton("Server");
        this.clientRadioButton = new JRadioButton("Client");
        this.snapshotToggleButton = new JToggleButton("Toggle Snapshots");
        this.snapshotToggleButton.setSelected(false);
        this.snapshotToggleButton.addActionListener(new SnapshotButtonListener(this));

        hookSize(() -> this.serverRadioButton.setBounds(getSize().width / 2 + 10, 60, 100, 20));
        hookSize(() -> this.clientRadioButton.setBounds(getSize().width / 2 - (100 - 10), 60, 100, 20));
        hookSize(() -> this.snapshotToggleButton.setBounds((getSize().width / 2) - 100, 90, 200, 30));
        this.clientRadioButton.setSelected(true);
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(this.serverRadioButton);
        typeGroup.add(this.clientRadioButton);
        add(this.serverRadioButton);
        add(this.clientRadioButton);
        add(this.snapshotToggleButton);
    }

    public void setupVersions(boolean showSnapshots) {
        this.versionBox.removeAllItems();
        for (Version version : showSnapshots ? Versions.getSnapshotVersions() : Versions.getReleaseVersions()) {
            this.versionBox.addItem(version);
        }
    }

    private void createVersionPopup() {
        this.versionBox = new JComboBox<>();
        this.versionBox.setBackground(Color.lightGray);
        hookSize(() -> this.versionBox.setBounds((getSize().width / 2) - 100, 125, 200, 30));
        add(this.versionBox);
    }

    private void createDecompileButton() {
        this.decompileCheckbox = new JCheckBox("Decompile?");
        hookSize(() -> this.decompileCheckbox.setBounds((getSize().width / 2) - 60, 165, 120, 30));
        this.decompileCheckbox.setSelected(false);
        add(this.decompileCheckbox);
    }

    private void createStatusBox() {
        this.statusBox = new JTextField("Status!");
        this.statusBox.setEditable(false);
        hookSize(() -> {
            int width = (int) (getSize().width * 0.90);
            this.statusBox.setBounds((getSize().width / 2) - (width / 2), 200, width, 30);
        });
        add(this.statusBox);
    }

    public void updateStatusBox(String string) {
        this.statusBox.setText(string);
    }

    private void createStartButton() {
        this.startButton = new JButton("Start!");
        this.startButton.setToolTipText("test");
        int w = 200;
        int h = 50;
        int hDivided;
        if (Util.isRunningMacOS()) {
            hDivided = h / 2;
        } else {
            // makes the spacing of the button look better on windows
            hDivided = Math.round(h / 1.25F);
        }
        hookSize(() -> this.startButton.setBounds((getSize().width / 2) - (w / 2), ((getSize().height / 5) * 4) - hDivided, w, h));
        this.startButton.addActionListener(new StartButtonListener(this));
        add(this.startButton);
    }

    public void updateButton(String string) {
        updateButton(string, Color.BLACK);
    }

    public void updateButton(String string, Color color) {
        this.startButton.setText(string);
        this.startButton.setForeground(color);
    }

    public void toggleControls() {
        this.startButton.setEnabled(!this.startButton.isEnabled());
        this.decompileCheckbox.setEnabled(!this.decompileCheckbox.isEnabled());
        this.versionBox.setEnabled(!this.versionBox.isEnabled());
        this.serverRadioButton.setEnabled(!this.serverRadioButton.isEnabled());
        this.clientRadioButton.setEnabled(!this.clientRadioButton.isEnabled());
        this.snapshotToggleButton.setEnabled(!this.snapshotToggleButton.isEnabled());
    }

    public void start(Version version, boolean shouldDecompile) {
        Thread thread = new Thread("Processor") {
            @Override
            public void run() {
                Processor processor = new Processor(version, null, App.this, shouldDecompile, false);
                processor.init();
            }
        };
        thread.start();
    }

    public void fail(String failMessage) {
        if (this.startButton.isEnabled()) toggleControls();
        updateButton(failMessage, Color.RED);
        getToolkit().beep();
        Timer timer = new Timer(1000, e1 -> {
            updateButton("Start!");
            toggleControls();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void finish(TimeSpan timeSpan) {
        this.updateStatusBox(String.format("Completed in %s!", timeSpan));
        updateButton("Completed!");
        getToolkit().beep();
        Timer timer = new Timer(1000, e1 -> {
            updateButton("Start!");
            toggleControls();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void finishSetup() {
        setupVersions(false);
        updateInfoLine("Let's start de-obfuscating some Minecraft", Util.TITLE_READY_COLOR);
        toggleControls();
    }

    public JButton getStartButton() {
        return this.startButton;
    }

    public JRadioButton getServerRadioButton() {
        return this.serverRadioButton;
    }

    public JToggleButton getSnapshotToggleButton() {
        return this.snapshotToggleButton;
    }

    public JCheckBox getDecompileCheckbox() {
        return this.decompileCheckbox;
    }

    public JComboBox<Version> getVersionBox() {
        return this.versionBox;
    }

    private void hookSize(final Runnable sizeTask) {
        final ComponentListener listener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                sizeTask.run();
            }
        };

        this.addComponentListener(listener);
        sizeTask.run();
    }

}
