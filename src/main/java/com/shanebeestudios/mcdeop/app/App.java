package com.shanebeestudios.mcdeop.app;

import com.shanebeestudios.mcdeop.McDeob;
import com.shanebeestudios.mcdeop.app.components.ControlButton;
import com.shanebeestudios.mcdeop.app.components.ProcessorOptionsGroup;
import com.shanebeestudios.mcdeop.app.components.VersionBox;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import com.shanebeestudios.mcdeop.processor.Processor;
import com.shanebeestudios.mcdeop.processor.ProcessorOptions;
import com.shanebeestudios.mcdeop.processor.ResourceRequest;
import lombok.Getter;
import mx.kenzie.mirror.Mirror;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"SameParameterValue", "unchecked", "rawtypes", "FieldCanBeLocal"})
@Getter
public class App extends JFrame {
    public static java.util.List<Component> getAllComponents(final Container c) {
        final java.util.List<Component> containers = new ArrayList<>();
        final Component[] comps = c.getComponents();
        for (final Component comp : comps) {
            containers.add(comp);
            if (comp instanceof final Container container) {
                containers.addAll(getAllComponents(container));
            }
        }
        return containers;
    }

    private final JLabel titleLabel;
    private final ControlButton controlButton;
    private final JRadioButton server;
    private final JRadioButton client;
    private final ProcessorOptionsGroup processorOptionsGroup;
    private final JComboBox<Version> versionBox;
    private final JTextField statusBox;

    public App() {
        try {
            // If we're running on Mac, set the logo
            final Taskbar taskbar = Taskbar.getTaskbar();
            taskbar.setIconImage(Icon.DOCK_LOGO_1024.getImage());
        } catch (final Exception ignored) {
            // Else we set it this way
            this.setIconImages(Icon.LOGO_IMAGES);
        }

        this.titleLabel = new JLabel("Let's start de-obfuscating some Minecraft");

        this.server = new JRadioButton("Server");
        this.client = new JRadioButton("Client");
        this.client.setSelected(true);

        final ButtonGroup versionTypeGroup = new ButtonGroup();
        versionTypeGroup.add(this.server);
        versionTypeGroup.add(this.client);

        this.versionBox = new VersionBox();
        this.processorOptionsGroup = new ProcessorOptionsGroup();

        this.statusBox = new JTextField("Status!");
        this.statusBox.setEditable(false);

        this.controlButton = new ControlButton(this);

        this.createLayout();
        this.pack();
        this.setupWindow();
        this.setVisible(true);
    }

    private List<Component> getAllComponents() {
        return getAllComponents(this);
    }

    private void createLayout() {
        final Container contentPane = this.getContentPane();
        final GroupLayout layout = new GroupLayout(contentPane);
        layout.setLayoutStyle(new AppLayoutStyle());
        contentPane.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(this.titleLabel)
                .addGroup(
                        layout.createSequentialGroup().addComponent(this.client).addComponent(this.server))
                .addGroup(layout.createSequentialGroup().addComponent(this.versionBox, 150, 150, 150))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(this.processorOptionsGroup.getRemap())
                        .addComponent(this.processorOptionsGroup.getDecompile())
                        .addComponent(this.processorOptionsGroup.getZipDecompileOutput()))
                .addGroup(layout.createSequentialGroup().addComponent(this.statusBox, 150, 450, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup().addComponent(this.controlButton, 150, 150, 150)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(this.titleLabel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                        .addComponent(this.client)
                        .addComponent(this.server))
                .addGroup(layout.createSequentialGroup().addComponent(this.versionBox, 25, 25, 25))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                        .addComponent(this.processorOptionsGroup.getRemap())
                        .addComponent(this.processorOptionsGroup.getDecompile())
                        .addComponent(this.processorOptionsGroup.getZipDecompileOutput()))
                .addGroup(layout.createSequentialGroup().addComponent(this.statusBox, 50, 50, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup().addComponent(this.controlButton, 15, 50, 50)));
    }

    private void setupWindow() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("McDeob - " + McDeob.getVersion());
        this.setResizable(true);
        this.setMinimumSize(this.getSize());
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    public void updateStatusBox(final String string) {
        this.statusBox.setText(string);
    }

    public void toggleControls() {
        for (final Component component : this.getAllComponents()) {
            if (component instanceof final JTextField field && !field.isEditable()) {
                continue;
            }

            if (component instanceof JLabel) {
                continue;
            }

            component.setEnabled(!component.isEnabled());
        }
    }

    public void start(final ResourceRequest request, final ProcessorOptions options) {
        this.getControlButton().setReady(false);

        final App app = this;
        final Thread thread = new Thread("Processor") {
            @Override
            public void run() {
                Processor.runProcessor(request, options, app);
            }
        };
        thread.start();
    }

    @Override
    public void setTitle(final String title) {
        super.setTitle(title);

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
    }

    private static class AppLayoutStyle extends LayoutStyle {
        @Override
        public int getPreferredGap(
                final JComponent component1,
                final JComponent component2,
                final ComponentPlacement type,
                final int position,
                final Container parent) {
            return 15;
        }

        @Override
        public int getContainerGap(final JComponent component, final int position, final Container parent) {
            return 15;
        }
    }
}
