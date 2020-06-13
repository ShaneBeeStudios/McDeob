package com.shanebeestudios.mcdeop.app;

import com.apple.eawt.Application;
import com.shanebeestudios.mcdeop.Processor;
import com.shanebeestudios.mcdeop.Version;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings({"SameParameterValue", "unchecked", "rawtypes", "FieldCanBeLocal"})
public class App extends JFrame {

    private JButton startButton;
    private JRadioButton server;
    private JRadioButton client;
    private JCheckBox decompile;
    private JComboBox<String> versionBox;
    private JTextField statusBox;

    public App() {
        init();
        setVisible(true);
    }

    private void init() {
        try {
            // If we're running on mac, set the logo
            Application application = Application.getApplication();
            application.setDockIconImage(Icon.DOCK_LOGO_1024.getImage());
        } catch (Throwable ignore) {
            // Else we set it this way
            setIconImages(Icon.LOGO_IMAGES);
        }

        setupWindow(500, 300);
        try {
            // makes the window prettier on other systems than macs
            // swing's look and feel is ew
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        createTitle();
        createTypeButton();
        createVersionPopup();
        createDeobOption();
        createStatusBox();
        createStartButton();
    }

    private void setupWindow(int width, int height) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setTitle("McDeob");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "AppName");
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setLayout(null);
    }

    private void createTitle() {
        JLabel label = new JLabel("Let's start deobfuscating some Minecraft", SwingConstants.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setBounds(0, 10, getSize().width, 50);
        add(label);
    }

    private void createTypeButton() {
        server = new JRadioButton("Server");
        client = new JRadioButton("Client");
        int center = getSize().width / 2;
        server.setBounds(center + 10, 60, 100, 20);
        client.setBounds(center - (100 - 10), 60, 100, 20);
        client.setSelected(true);
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(server);
        typeGroup.add(client);
        add(server);
        add(client);
    }

    private void createVersionPopup() {
        versionBox = new JComboBox();
        versionBox.addItem("Choose Minecraft Version");
        for (Version version : Version.values()) {
            if (version.getType() == Version.Type.SERVER) {
                versionBox.addItem(version.getVersion());
            }
        }
        versionBox.setBounds((getSize().width / 2) - 110, 95, 220, 20);
        add(versionBox);
    }

    private void createDeobOption() {
        decompile = new JCheckBox("Decompile?");
        int decompileHeight;
        if (isWindows()) {
            // fixes some weird overlap with the status box
            decompileHeight = 30;
        } else {
            decompileHeight = 40;
        }
        decompile.setBounds((getSize().width / 2) - 60, 115, 120, decompileHeight);
        decompile.setSelected(false);
        add(decompile);
    }

    private void createStatusBox() {
        statusBox = new JTextField("Status!");
        statusBox.setEditable(false);
        int width = (int) (getSize().width * 0.90);
        statusBox.setBounds((getSize().width / 2) - (width / 2), 150,width, 30);
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
        if (isWindows()) {
            // makes the spacing of the button look better on windows
            hDivided = Math.round(h / 1.25F);
        } else {
            hDivided = h / 2;
        }
        startButton.setBounds((getSize().width / 2) - (w / 2), ((getSize().height / 5) * 4) - hDivided, w, h);
        startButton.addActionListener(new ButtonListener());
        add(startButton);
    }

    private boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

    public void updateButton(String string) {
        updateButton(string, Color.BLACK);
    }

    public void updateButton(String string, Color color) {
        startButton.setText(string);
        startButton.setForeground(color);
    }

    private void start(Version version, boolean decomp) {
        App app = this;
        Thread thread = new Thread("Processor") {
            @Override
            public void run() {
                Processor processor = new Processor(version, decomp, app);
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
                Version version = Version.getByVersion((String) versionBox.getSelectedItem(), type);
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
