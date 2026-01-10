package com.miriki.ti99.mame.ui.builder.sections;

import java.awt.Container;
import java.awt.event.FocusListener;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;
import com.miriki.ti99.mame.ui.builder.UiFactory;

/**
 * Builds the debug section of the main application frame.
 * <p>
 * This includes the "Additional Options" text field and the debug output area
 * showing the generated emulator command line.
 */
public class DebugSectionBuilder {

    private final MainAppFrame frame;
    private final MainAppFrameComponents ui;
    private final UiFactory factory;

    public DebugSectionBuilder(MainAppFrame frame,
                               MainAppFrameComponents ui,
                               UiFactory factory) {
        this.frame = frame;
        this.ui = ui;
        this.factory = factory;
    }

    /**
     * Adds and configures all debug-related UI elements.
     */
    public void build() {

        Container parent = frame.getContentPane();

        // ---------------------------------------------------------------------
        // Additional Options Text Field (txtAddOpt)
        // ---------------------------------------------------------------------

        ui.txtAddOpt = factory.createTextBoxWithLabel(
                parent,
                "txtAddOpt",
                "",
                704,
                272,
                464,
                24,
                e -> frame.collectEmulatorOptions(),
                I18n.t("main.addopt"),
                88
        );

        // Remove automatic path-normalization listener for this field
        ui.txtAddOpt.removeFocusListener(
                (FocusListener) ui.txtAddOpt.getClientProperty("normalizeListener")
        );

        // ---------------------------------------------------------------------
        // Debug Text Area (dbgEmulatorOptions)
        // ---------------------------------------------------------------------

        parent.add(ui.dbgEmulatorOptions);
        ui.dbgEmulatorOptions.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        ui.dbgEmulatorOptions.setLineWrap(true);
        ui.dbgEmulatorOptions.setBounds(
                616,
                304,
                464 + 88,
                320 - 8 - 32 - 8 - 24
        );
    }
}
