package com.shanebeestudios.mcdeop.app;

import com.shanebeestudios.mcdeop.McDeob;
import com.shanebeestudios.mcdeop.VersionManager;
import com.shanebeestudios.mcdeop.launchermeta.data.release.ReleaseManifest;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import com.shanebeestudios.mcdeop.processor.Processor;
import com.shanebeestudios.mcdeop.processor.ProcessorOptions;
import com.shanebeestudios.mcdeop.processor.ResourceRequest;
import com.shanebeestudios.mcdeop.processor.SourceType;
import com.shanebeestudios.mcdeop.util.Util;
import mx.kenzie.mirror.Mirror;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

@SuppressWarnings({"SameParameterValue", "unchecked", "rawtypes", "FieldCanBeLocal"})
public class App extends JFrame {
    private JButton startButton;
    private JRadioButton server;
    private JRadioButton client;
    private JCheckBox decompile;
    private JComboBox<Version> versionBox;
    private JTextField statusBox;
    private JTextField currentVerBox;

    public App() {
        this.init();
        this.setVisible(true);
    }

    private void init() {
        try {
            // If we're running on Mac, set the logo
            final Taskbar taskbar = Taskbar.getTaskbar();
            assert Icon.DOCK_LOGO_1024 != null;
            taskbar.setIconImage(Icon.DOCK_LOGO_1024.getImage());
        } catch (final Exception ignored) {
            // Else we set it this way
            this.setIconImages(Icon.LOGO_IMAGES);
        }

        this.setupWindow(500, 335);
        this.createTitle();
        this.createTypeButton();
        this.createVersionPopup();
        this.createDeobOption();
        this.createStatusBox();
        this.createStartButton();
    }

    private void setupWindow(final int width, final int height) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(width, height);

        final String title = "McDeob - " + McDeob.getVersion();
        this.setTitle(title);

        try { // Window title hack for GTK
            Mirror.of(Toolkit.getDefaultToolkit())
                    .unsafe()
                    .field("awtAppClassName")
                    .set(title);
        } catch (final Exception ignored) {
            // We're probably just not on XToolkit
        }

        // Window title hack for macOS
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", title);

        this.setResizable(true);
        this.setMinimumSize(new Dimension(500, 335));
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setLayout(null);
    }

    private ComponentListener hookSize(final Runnable sizeTask) {
        final ComponentListener listener = new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                sizeTask.run();
            }
        };

        this.addComponentListener(listener);
        sizeTask.run();
        return listener;
    }

    private void createTitle() {
        final JLabel label = new JLabel("Let's start de-obfuscating some Minecraft", SwingConstants.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        this.hookSize(() -> label.setBounds(0, 10, this.getSize().width, 50));
        this.add(label);
    }

    private void createTypeButton() {
        this.server = new JRadioButton("Server");
        this.client = new JRadioButton("Client");
        this.hookSize(() -> this.server.setBounds(this.getSize().width / 2 + 10, 60, 100, 20));
        this.hookSize(() -> this.client.setBounds(this.getSize().width / 2 - (100 - 10), 60, 100, 20));
        this.client.setSelected(true);
        final ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(this.server);
        typeGroup.add(this.client);
        this.add(this.server);
        this.add(this.client);
    }

    private void createVersionPopup() {
        this.versionBox = new JComboBox();
        this.versionBox.setRenderer(new DefaultListCellRenderer() {
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
        });

        for (final Version version : VersionManager.getInstance().getVersions()) {
            this.versionBox.addItem(version);
        }
        this.versionBox.setSelectedIndex(0);
        this.versionBox.setBackground(Color.lightGray);
        this.hookSize(() -> this.versionBox.setBounds(
                (this.getSize().width / 2) - this.versionBox.getWidth() / 2,
                95,
                this.versionBox.getPreferredSize().width,
                25));
        this.add(this.versionBox);
    }

    private void createDeobOption() {
        this.decompile = new JCheckBox("Decompile?");
        final int decompileHeight;
        if (Util.isRunningMacOS()) {
            decompileHeight = 30;
        } else {
            // fixes some weird overlap with the status box
            decompileHeight = 30;
        }
        this.hookSize(() -> this.decompile.setBounds((this.getSize().width / 2) - 60, 125, 120, decompileHeight));
        this.decompile.setSelected(false);
        this.add(this.decompile);
    }

    private transient ComponentListener statusBoxListener;

    private void createStatusBox() {
        this.statusBox = new JTextField("Status!");
        this.statusBox.setEditable(false);
        this.statusBoxListener = this.hookSize(() -> {
            final int width = (int) (this.getSize().width * 0.90);
            this.statusBox.setBounds((this.getSize().width / 2) - (width / 2), 160, width, 30);
        });
        this.add(this.statusBox);
    }

    public void updateStatusBox(final String string) {
        this.statusBox.setText(string);
    }

    public void addVersionBox(final String version) {
        if (this.currentVerBox == null) {
            this.currentVerBox = new JTextField();
            this.currentVerBox.setEditable(false);
            this.currentVerBox.setHorizontalAlignment(SwingConstants.CENTER);
            this.hookSize(() -> this.currentVerBox.setBounds((this.getSize().width / 2) - 110, 150, 220, 30));
            this.add(this.currentVerBox);
        }
        // Shift status box down
        final int width = (int) (this.getSize().width * 0.90);
        this.removeComponentListener(this.statusBoxListener);
        this.statusBoxListener =
                this.hookSize(() -> this.statusBox.setBounds((this.getSize().width / 2) - (width / 2), 190, width, 30));

        this.currentVerBox.setText("Version: " + version);
        this.currentVerBox.setForeground(new Color(13, 193, 47));
    }

    private void createStartButton() {
        this.startButton = new JButton("Start!");
        this.startButton.setToolTipText("test");
        final int w = 200;
        final int h = 50;
        final int hDivided;
        if (Util.isRunningMacOS()) {
            hDivided = h / 2;
        } else {
            // makes the spacing of the button look better on windows
            hDivided = Math.round(h / 1.25F);
        }
        this.hookSize(() -> this.startButton.setBounds(
                (this.getSize().width / 2) - (w / 2), ((this.getSize().height / 5) * 4) - hDivided, w, h));
        this.startButton.addActionListener(new ButtonListener());
        this.add(this.startButton);
    }

    public void updateButton(final String string) {
        this.updateButton(string, Color.BLACK);
    }

    public void updateButton(final String string, final Color color) {
        this.startButton.setText(string);
        this.startButton.setForeground(color);
    }

    public void toggleControls() {
        this.startButton.setEnabled(!this.startButton.isEnabled());
        this.decompile.setEnabled(!this.decompile.isEnabled());
        this.versionBox.setEnabled(!this.versionBox.isEnabled());
        this.server.setEnabled(!this.server.isEnabled());
        this.client.setEnabled(!this.client.isEnabled());
    }

    private void start(final ResourceRequest request, final boolean shouldDecompile) {
        final App app = this;
        final Thread thread = new Thread("Processor") {
            @Override
            public void run() {
                final ProcessorOptions options =
                        ProcessorOptions.builder().decompile(shouldDecompile).build();

                Processor.runProcessor(request, options, app);
            }
        };
        thread.start();
    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getSource() != App.this.startButton) {
                return;
            }

            if (!App.this.startButton.getText().equalsIgnoreCase("Start!")) {
                return;
            }

            final Version version = (Version) App.this.versionBox.getSelectedItem();
            if (version == null) {
                App.this.updateButton("INVALID VERSION!", Color.RED);
                App.this.getToolkit().beep();
                final Timer timer = new Timer(1000, e1 -> App.this.updateButton("Start!"));
                timer.setRepeats(false);
                timer.start();
                return;
            }

            final SourceType type = App.this.server.isSelected() ? SourceType.SERVER : SourceType.CLIENT;
            final boolean decomp = App.this.decompile.isSelected();

            final ReleaseManifest releaseManifest;
            try {
                releaseManifest = VersionManager.getInstance().getReleaseManifest(version);
            } catch (final IOException ex) {
                App.this.updateButton("FAILED TO FETCH RELEASE MANIFEST", Color.RED);
                App.this.getToolkit().beep();
                final Timer timer = new Timer(1000, e1 -> App.this.updateButton("Start!"));
                timer.setRepeats(false);
                timer.start();
                return;
            }

            final ResourceRequest resourceRequest = new ResourceRequest(releaseManifest, type);
            App.this.updateButton("Starting...", Color.BLUE);
            App.this.start(resourceRequest, decomp);
        }
    }
}
