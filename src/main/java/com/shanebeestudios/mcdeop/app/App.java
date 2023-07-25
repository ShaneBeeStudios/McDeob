package com.shanebeestudios.mcdeop.app;

import com.shanebeestudios.mcdeop.McDeob;
import com.shanebeestudios.mcdeop.app.components.*;
import com.shanebeestudios.mcdeop.launchermeta.data.version.Version;
import com.shanebeestudios.mcdeop.processor.Processor;
import com.shanebeestudios.mcdeop.processor.ProcessorOptions;
import com.shanebeestudios.mcdeop.processor.ResourceRequest;
import lombok.Getter;
import mx.kenzie.mirror.Mirror;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"SameParameterValue", "unchecked", "rawtypes", "FieldCanBeLocal"})
@Getter
public class App extends JFrame {
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 335;

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

    private final JButton controlButton;
    private final JRadioButton server;
    private final JRadioButton client;
    private final JCheckBox decompile;
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

        this.setupWindow(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.addComponent(new TitleLabel());

        this.server = this.addComponent(new ServerVersionTypeButton());
        this.client = this.addComponent(new ClientVersionTypeButton());
        final ButtonGroup versionTypeGroup = new ButtonGroup();
        versionTypeGroup.add(this.server);
        versionTypeGroup.add(this.client);

        this.versionBox = this.addComponent(new VersionBox());
        this.decompile = this.addComponent(new DecompilerOptionBox());
        this.statusBox = this.addComponent(new StatusField());
        this.controlButton = this.addComponent(new ControlButton(this));

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                App.this.updateComponentDimensions();
            }
        });

        this.updateComponentDimensions();
        this.setVisible(true);
    }

    private <T extends Component> T addComponent(final T component) {
        super.add(component);
        return component;
    }

    private List<Component> getAllComponents() {
        return getAllComponents(this);
    }

    private void updateComponentDimensions() {
        for (final Component component : this.getAllComponents()) {
            if (component instanceof final DynamicDimension dynamicDimension) {
                dynamicDimension.updateDimension(this.getWidth(), this.getHeight());
            }
        }
    }

    private void setupWindow(final int width, final int height) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setTitle("McDeob - " + McDeob.getVersion());
        this.setResizable(true);
        this.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setLayout(null);
    }

    public void updateStatusBox(final String string) {
        this.statusBox.setText(string);
    }

    public void updateButton(final String string) {
        this.updateButton(string, Color.BLACK);
    }

    public void updateButton(final String string, final Color color) {
        this.controlButton.setText(string);
        this.controlButton.setForeground(color);
    }

    public void toggleControls() {
        for (final Component component : this.getAllComponents()) {
            component.setEnabled(!component.isEnabled());
        }
    }

    public void start(final ResourceRequest request, final boolean shouldDecompile) {
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
}
