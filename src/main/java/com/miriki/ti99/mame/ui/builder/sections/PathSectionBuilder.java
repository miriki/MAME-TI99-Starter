package com.miriki.ti99.mame.ui.builder.sections;

import java.awt.Container;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;
import com.miriki.ti99.mame.ui.builder.UiFactory;

/**
 * Builds the path configuration section.
 */
public class PathSectionBuilder {

    private final MainAppFrame frame;
    private final MainAppFrameComponents ui;
    private final UiFactory factory;

    public PathSectionBuilder(MainAppFrame frame,
                              MainAppFrameComponents ui,
                              UiFactory factory) {
        this.frame = frame;
        this.ui = ui;
        this.factory = factory;
    }

    /**
     * Adds and configures all path‑related text fields.
     */
    public void build() {

        Container parent = frame.getContentPane();

        // Working directory
        ui.txtWorkingDir = factory.createTextBoxWithLabel(
                parent,
                "txtWorkingDir",
                "",
                112, 16, 464, 24,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.workingdir"),
                88
        );

        // Emulator executable
        ui.txtExecutable = factory.createTextBoxWithLabel(
                parent,
                "txtExecutable",
                "",
                704, 16, 464, 24,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.executable"),
                88
        );

        // Remove path-normalization listener (not a path field)
        ui.txtExecutable.removeFocusListener(
                (java.awt.event.FocusListener)
                        ui.txtExecutable.getClientProperty("normalizeListener")
        );

        // ROM path
        ui.txtRomPath = factory.createTextBoxWithLabel(
                parent,
                "txtRomPath",
                "",
                112, 48, 464, 24,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.rompath"),
                88
        );

        // Cartridge path
        ui.txtCartPath = factory.createTextBoxWithLabel(
                parent,
                "txtCartPath",
                "",
                704, 48, 464, 24,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.cartpath"),
                88
        );

        // Floppy path
        ui.txtFddPath = factory.createTextBoxWithLabel(
                parent,
                "txtFddPath",
                "",
                112, 112, 464, 24,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.fddpath"),
                88
        );

        // Harddisk path
        ui.txtHddPath = factory.createTextBoxWithLabel(
                parent,
                "txtHddPath",
                "",
                704, 112, 464, 24,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.hddpath"),
                88
        );

        // Cassette path
        ui.txtCassPath = factory.createTextBoxWithLabel(
                parent,
                "txtCassPath",
                "",
                112, 144, 464, 24,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.casspath"),
                88
        );

        // FIAD path
        ui.txtFiadPath = factory.createTextBoxWithLabel(
                parent,
                "txtFiadPath",
                "",
                704, 144, 464, 24,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.fiadpath"),
                88
        );
    }
}
