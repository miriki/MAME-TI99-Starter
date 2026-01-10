package com.miriki.ti99.mame.ui;

import javax.swing.*;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.tools.IconLoader;

/**
 * Handles all I18n-related UI updates.
 * Extracted from MainAppFrame for clarity and modularity.
 */
public class MainAppFrameI18n {

    private final MainAppFrame frame;
    private final MainAppFrameComponents ui;

    public MainAppFrameI18n(MainAppFrame frame, MainAppFrameComponents ui) {
        this.frame = frame;
        this.ui = ui;
    }

    // -------------------------------------------------------------------------
    // Update all texts from I18n
    // -------------------------------------------------------------------------

    /**
     * Updates all UI texts based on the current locale.
     * Uses the "i18n" client property stored in each component.
     */
    public void updateTextsFromI18n() {

        for (var field : ui.getClass().getFields()) {
            try {
                Object value = field.get(ui);

                if (value instanceof JLabel lbl) {
                    String key = (String) lbl.getClientProperty("i18n");
                    if (key != null) lbl.setText(I18n.t(key));
                }

                if (value instanceof AbstractButton btn) {
                    String key = (String) btn.getClientProperty("i18n");
                    if (key != null) btn.setText(I18n.t(key));
                }

                if (value instanceof JMenu menu) {
                    String key = (String) menu.getClientProperty("i18n");
                    if (key != null) menu.setText(I18n.t(key));
                }

                if (value instanceof JMenuItem item) {
                    String key = (String) item.getClientProperty("i18n");
                    if (key != null) item.setText(I18n.t(key));
                }

            } catch (Exception ignored) {
                // Optional: log.warn("i18n update failed for field {}", field.getName());
            }
        }
    }

    // -------------------------------------------------------------------------
    // Refresh UI
    // -------------------------------------------------------------------------

    /**
     * Refreshes all I18n texts and updates the language flag icon.
     */
    public void refreshUI() {

        updateTextsFromI18n();

        // Update flag icon
        ui.menuLang.setIcon(
            IconLoader.load("flag_" + I18n.getCurrentLanguageCode() + ".png")
        );

        frame.revalidate();
        frame.repaint();
    }
}
