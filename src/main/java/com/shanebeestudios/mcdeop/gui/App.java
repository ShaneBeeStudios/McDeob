package com.shanebeestudios.mcdeop.gui;

import com.apple.eawt.Application;
import com.shanebeestudios.mcdeop.Version;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings({"SameParameterValue", "unchecked", "rawtypes", "FieldCanBeLocal", "ConstantConditions"})
public class App extends JFrame {

    private JButton startButton;
    private JRadioButton server;
    private JRadioButton client;
    private JComboBox<String> versionBox;

    public App() {
        init();
        setVisible(true);
    }

    private void init() {
        try {
            // If we're running on mac, set the logo
            Application application = Application.getApplication();
            application.setDockIconImage(Icon.DOCK_LOGO.getImage());
        } catch (Throwable ignore) {
            // Else we set it this way
            setIconImage(Icon.DOCK_LOGO.getImage());
        }

        setupWindow(500, 300);
        createTitle();
        createTypeButton();
        createVersionPopup();
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
        server.setBounds(center + 10, 100, 100, 20);
        client.setBounds(center - (100 - 10), 100, 100, 20);
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
        versionBox.setBounds((getSize().width / 2) - 110, 140, 220, 20);
        add(versionBox);
    }

    private void createStartButton() {
        startButton = new JButton("Start!");
        int w = 150;
        int h = 50;
        startButton.setBounds((getSize().width / 2) - (w / 2), ((getSize().height / 5) * 4) - (h / 2), w, h);
        startButton.addActionListener(new ButtonListener());
        add(startButton);
    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == startButton) {
                Version.Type type = server.isSelected() ? Version.Type.SERVER : Version.Type.CLIENT;
                Version version = Version.getByVersion((String) versionBox.getSelectedItem(), type);
                if (version == null) {
                    startButton.setText("INVALID VERSION!");
                    startButton.setForeground(Color.RED);
                    Timer timer = new Timer(1000, e1 -> {
                        startButton.setText("Start!");
                        startButton.setForeground(Color.BLACK);
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    startButton.setText("Starting...");
                    startButton.setForeground(Color.BLUE);
                    // TODO process jar
                }
            }
        }
    }

}
