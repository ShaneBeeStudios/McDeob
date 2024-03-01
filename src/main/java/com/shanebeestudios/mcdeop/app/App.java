package com.shanebeestudios.mcdeop.app;

import com.shanebeestudios.mcdeop.Processor;
import com.shanebeestudios.mcdeop.Version;
import com.shanebeestudios.mcdeop.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.Field;

@SuppressWarnings({"SameParameterValue", "unchecked", "rawtypes", "FieldCanBeLocal"})
public class App extends JFrame {

    private JButton startButton;
    private JRadioButton serverRadioButton;
    private JRadioButton clientRadioButton;
    private JToggleButton snapshotToggleButton;
    private JCheckBox decompile;
    private JComboBox<String> versionBox;
    private JTextField statusBox;

    public App() {
        init();
        setVisible(true);
    }

    private void init() {
        try {
            // If we're running on Mac, set the logo
            Taskbar taskbar = Taskbar.getTaskbar();
            assert Icon.DOCK_LOGO_1024 != null;
            taskbar.setIconImage(Icon.DOCK_LOGO_1024.getImage());
        } catch (Exception ignored) {
            // Else we set it this way
            setIconImages(Icon.LOGO_IMAGES);
        }

        setupWindow(500, 335);
        createTitle();
        createTypeButton();
        createVersionPopup();
        createDecompileButton();
        createStatusBox();
        createStartButton();
    }

    private void setupWindow(int width, int height) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width, height);
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

    private void createTitle() {
        JLabel label = new JLabel("Let's start de-obfuscating some Minecraft", SwingConstants.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        hookSize(() -> label.setBounds(0, 10, getSize().width, 50));
        add(label);
    }

    private void createTypeButton() {
        this.serverRadioButton = new JRadioButton("Server");
        this.clientRadioButton = new JRadioButton("Client");
        this.snapshotToggleButton = new JToggleButton("Toggle Snapshots");
        this.snapshotToggleButton.setSelected(false);
        this.snapshotToggleButton.addActionListener(new SnapshotButtonListener());

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

    private void setupVersions(boolean showSnapshots) {
        this.versionBox.removeAllItems();
        for (Version version : Version.getVersions()) {
            if (version.getReleaseType().equalsIgnoreCase("snapshot") && !showSnapshots) continue;
            else if (version.getReleaseType().equalsIgnoreCase("release") && showSnapshots) continue;
            this.versionBox.addItem(version.getVersion().replace("_", " "));
        }
    }

    private void createVersionPopup() {
        this.versionBox = new JComboBox();
        setupVersions(false);
        this.versionBox.setSelectedIndex(0);
        this.versionBox.setBackground(Color.lightGray);
        hookSize(() -> versionBox.setBounds((getSize().width / 2) - 100, 125, 200, 30));
        add(versionBox);
    }

    private void createDecompileButton() {
        decompile = new JCheckBox("Decompile?");
        hookSize(() -> decompile.setBounds((getSize().width / 2) - 60, 165, 120, 30));
        this.decompile.setSelected(false);
        add(decompile);
    }

    private void createStatusBox() {
        statusBox = new JTextField("Status!");
        statusBox.setEditable(false);
        hookSize(() -> {
            int width = (int) (getSize().width * 0.90);
            statusBox.setBounds((getSize().width / 2) - (width / 2), 200, width, 30);
        });
        add(statusBox);
    }

    public void updateStatusBox(String string) {
        statusBox.setText(string);
    }

    private void createStartButton() {
        startButton = new JButton("Start!");
        startButton.setToolTipText("test");
        int w = 200;
        int h = 50;
        int hDivided;
        if (Util.isRunningMacOS()) {
            hDivided = h / 2;
        } else {
            // makes the spacing of the button look better on windows
            hDivided = Math.round(h / 1.25F);
        }
        hookSize(() -> startButton.setBounds((getSize().width / 2) - (w / 2), ((getSize().height / 5) * 4) - hDivided, w, h));
        startButton.addActionListener(new StartButtonListener());
        add(startButton);
    }

    public void updateButton(String string) {
        updateButton(string, Color.BLACK);
    }

    public void updateButton(String string, Color color) {
        startButton.setText(string);
        startButton.setForeground(color);
    }

    public void toggleControls() {
        this.startButton.setEnabled(!this.startButton.isEnabled());
        this.decompile.setEnabled(!this.decompile.isEnabled());
        this.versionBox.setEnabled(!this.versionBox.isEnabled());
        this.serverRadioButton.setEnabled(!this.serverRadioButton.isEnabled());
        this.clientRadioButton.setEnabled(!this.clientRadioButton.isEnabled());
        this.snapshotToggleButton.setEnabled(!this.snapshotToggleButton.isEnabled());
    }

    private void start(Version version, boolean shouldDecompile) {
        App app = this;
        Thread thread = new Thread("Processor") {
            @Override
            public void run() {
                Processor processor = new Processor(version, shouldDecompile, app);
                processor.init();
            }
        };
        thread.start();
    }

    public void fail() {
        toggleControls();
        updateButton("INVALID VERSION!", Color.RED);
        getToolkit().beep();
        Timer timer = new Timer(1000, e1 -> {
            updateButton("Start!");
            toggleControls();
        });
        timer.setRepeats(false);
        timer.start();
    }

    class StartButtonListener implements ActionListener {

        @SuppressWarnings("DataFlowIssue")
        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == startButton) {
                Version version = Version.getByVersion(((String) versionBox.getSelectedItem()).replace(" ", "_"));
                if (!startButton.getText().equalsIgnoreCase("Start!")) return;
                if (version == null) {
                    fail();
                } else {
                    version.setType(serverRadioButton.isEnabled() ? Version.Type.SERVER : Version.Type.CLIENT);
                    updateButton("Starting...", Color.BLUE);
                    start(version, decompile.isSelected());
                }
            }
        }
    }

    class SnapshotButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (snapshotToggleButton.isSelected()) {
                snapshotToggleButton.setSelected(true);
                snapshotToggleButton.setText("Toggle Releases");
                setupVersions(true);
            } else {
                snapshotToggleButton.setSelected(false);
                snapshotToggleButton.setText("Toggle Snapshots");
                setupVersions(false);
            }
        }
    }

}
