package com.shanebeestudios.mcdeop.app;

import com.shanebeestudios.mcdeop.*;
import com.shanebeestudios.mcdeop.util.Util;
import mx.kenzie.mirror.Mirror;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings({"SameParameterValue", "unchecked", "rawtypes", "FieldCanBeLocal"})
public class App extends JFrame {

    private JButton startButton;
    private JRadioButton server;
    private JRadioButton client;
    private JCheckBox decompile;
    private JComboBox<String> versionBox;
    private JTextField statusBox;
    private JTextField currentVerBox;

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
        createDeobOption();
        createStatusBox();
        createStartButton();
    }

    private void setupWindow(int width, int height) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width, height);
        setTitle("McDeob");

        try { // Window title hack for GTK
            Mirror.of(Toolkit.getDefaultToolkit()).unsafe().field("awtAppClassName").set("McDeob");
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

    private ComponentListener hookSize(final Runnable sizeTask) {
        final ComponentListener listener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                sizeTask.run();
            }
        };

        this.addComponentListener(listener);
        sizeTask.run();
        return listener;
    }

    private void createTitle() {
        JLabel label = new JLabel("Let's start de-obfuscating some Minecraft", SwingConstants.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        hookSize(() -> label.setBounds(0, 10, getSize().width, 50));
        add(label);
    }

    private void createTypeButton() {
        server = new JRadioButton("Server");
        client = new JRadioButton("Client");
        hookSize(() -> server.setBounds(getSize().width / 2 + 10, 60, 100, 20));
        hookSize(() -> client.setBounds(getSize().width / 2 - (100 - 10), 60, 100, 20));
        client.setSelected(true);
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(server);
        typeGroup.add(client);
        add(server);
        add(client);
    }

    private void createVersionPopup() {
        versionBox = new JComboBox();
        for (Version version : Version.values()) {
            if (version.getType() == Version.Type.SERVER) {
                versionBox.addItem(version.getVersion().replace("_", " "));
            }
        }
        versionBox.setSelectedIndex(0);
        hookSize(() -> versionBox.setBounds((getSize().width / 2) - 110, 95, versionBox.getPreferredSize().width, 20));
        add(versionBox);
    }

    private void createDeobOption() {
        decompile = new JCheckBox("Decompile?");
        int decompileHeight;
        if (Util.isRunningMacOS()) {
            decompileHeight = 40;
        } else {
            // fixes some weird overlap with the status box
            decompileHeight = 30;
        }
        hookSize(() -> decompile.setBounds((getSize().width / 2) - 60, 115, 120, decompileHeight));
        decompile.setSelected(false);
        add(decompile);
    }

    private transient ComponentListener statusBoxListener;
    private void createStatusBox() {
        statusBox = new JTextField("Status!");
        statusBox.setEditable(false);
        statusBoxListener = hookSize(() -> {
            int width = (int) (getSize().width * 0.90);
            statusBox.setBounds((getSize().width / 2) - (width / 2), 150, width, 30);
        });
        add(statusBox);
    }

    public void updateStatusBox(String string) {
        statusBox.setText(string);
    }

    public void addVersionBox(String version) {
        if (currentVerBox == null) {
            currentVerBox = new JTextField();
            currentVerBox.setEditable(false);
            currentVerBox.setHorizontalAlignment(SwingConstants.CENTER);
            hookSize(() -> currentVerBox.setBounds((getSize().width / 2) - 110, 150, 220, 30));
            add(currentVerBox);
        }
        // Shift status box down
        int width = (int) (getSize().width * 0.90);
        this.removeComponentListener(statusBoxListener);
        statusBoxListener = hookSize(() ->
            statusBox.setBounds((getSize().width / 2) - (width / 2), 190, width, 30));

        currentVerBox.setText("Version: " + version);
        currentVerBox.setForeground(new Color(13, 193, 47));
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
        startButton.addActionListener(new ButtonListener());
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
        startButton.setEnabled(!startButton.isEnabled());
        decompile.setEnabled(!decompile.isEnabled());
        versionBox.setEnabled(!versionBox.isEnabled());
        server.setEnabled(!server.isEnabled());
        client.setEnabled(!client.isEnabled());
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

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == startButton) {
                Version.Type type = server.isSelected() ? Version.Type.SERVER : Version.Type.CLIENT;
                Version version = Version.getByVersion(((String) versionBox.getSelectedItem()).replace(" ", "_"), type);
                if (!startButton.getText().equalsIgnoreCase("Start!")) return;
                if (version == null) {
                    updateButton("INVALID VERSION!", Color.RED);
                    getToolkit().beep();
                    Timer timer = new Timer(1000, e1 -> updateButton("Start!"));
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    boolean decomp = decompile.isSelected();
                    updateButton("Starting...", Color.BLUE);
                    start(version, decomp);
                }
            }
        }
    }

}
