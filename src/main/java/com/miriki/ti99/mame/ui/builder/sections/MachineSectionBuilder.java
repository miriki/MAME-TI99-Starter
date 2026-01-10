package com.miriki.ti99.mame.ui.builder.sections;

import java.awt.Container;

import javax.swing.DefaultComboBoxModel;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;
import com.miriki.ti99.mame.ui.builder.UiFactory;

/**
 * Builds the machine‑related configuration section of the main application UI.
 * <p>
 * This includes the machine selector, GROM port selector, joystick port selector
 * and side‑port (I/O) selector.
 */
public class MachineSectionBuilder {

    private final MainAppFrame frame;
    private final MainAppFrameComponents ui;
    private final UiFactory factory;

    public MachineSectionBuilder(MainAppFrame frame,
                                 MainAppFrameComponents ui,
                                 UiFactory factory) {
        this.frame = frame;
        this.ui = ui;
        this.factory = factory;
    }

    /**
     * Adds and configures all machine‑related combo boxes.
     */
    public void build() {

        Container parent = frame.getContentPane();

        // ---------------------------------------------------------------------
        // Machine
        // ---------------------------------------------------------------------

        ui.cbxMachine = factory.createComboBoxWithLabel(
                parent,
                "cbxMachine",
                new DefaultComboBoxModel<>(ui.MACHINE_NAMES),
                112,
                80,
                144,
                22,
                0,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.machine"),
                88
        );

        // ---------------------------------------------------------------------
        // GROM Port
        // ---------------------------------------------------------------------

        ui.cbxGromPort = factory.createComboBoxWithLabel(
                parent,
                "cbxGromPort",
                new DefaultComboBoxModel<>(ui.GROM_PORT_NAMES),
                360,
                80,
                144,
                22,
                0,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.gromport"),
                88
        );
        // Cartridge selector is built in MediaSectionBuilder

        // ---------------------------------------------------------------------
        // Joystick Port
        // ---------------------------------------------------------------------

        ui.cbxJoyPort = factory.createComboBoxWithLabel(
                parent,
                "cbxJoyPort",
                new DefaultComboBoxModel<>(ui.JOYSTICK_PORT_NAMES),
                768,
                80,
                144,
                22,
                0,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.joyport"),
                88
        );

        // ---------------------------------------------------------------------
        // I/O Port (Sideport)
        // ---------------------------------------------------------------------

        ui.cbxIoPort = factory.createComboBoxWithLabel(
                parent,
                "cbxIoPort",
                new DefaultComboBoxModel<>(ui.SIDEPORT_DEVICE_NAMES),
                1024,
                80,
                144,
                22,
                0,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.ioport"),
                88
        );
    }
}
