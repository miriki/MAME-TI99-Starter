package com.miriki.ti99.mame.ui;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.persistence.SettingsManager;
import com.miriki.ti99.mame.persistence.SettingsPathRegistry;
import com.miriki.ti99.mame.tools.FileTools;
import com.miriki.ti99.mame.ui.util.ControlCollector;

/**
 * Handles loading, saving and restoring settings.
 * Extracted from MainAppFrame for clarity and modularity.
 */
public class MainAppFrameSettings {

    private static final Logger log = LoggerFactory.getLogger(MainAppFrameSettings.class);

    private final MainAppFrame frame;
    private final MainAppFrameComponents ui;
    private final MainAppFrameState state;
    private final MainAppFrameMediaUpdater media;

    public MainAppFrameSettings(MainAppFrame frame,
                                MainAppFrameComponents ui,
                                MainAppFrameState state,
                                MainAppFrameMediaUpdater media) {
        this.frame = frame;
        this.ui = ui;
        this.state = state;
        this.media = media;
    }

    // -------------------------------------------------------------------------
    // Save
    // -------------------------------------------------------------------------

    public void saveSettings() {

        Map<String, JComponent> controls = ControlCollector.collectControls(frame);

        Map<String, java.awt.Component> tabMap =
                frame.getPebDevicesController() != null
                        ? frame.getPebDevicesController().getTabMap()
                        : Map.of();

        SettingsManager.saveToCurrent(controls, tabMap);
    }

    // -------------------------------------------------------------------------
    // Restore
    // -------------------------------------------------------------------------

    public void restoreSettings() {

        frame.setEventsSuspended(true);

        Map<String, JComponent> controls = ControlCollector.collectControls(frame);
        Properties props = null;

        try {
            props = SettingsManager.restoreFromCurrent(controls);

            restoreLocale(props);

            frame.refreshUI();

        } catch (IOException ex) {
            log.warn("No saved settings found for '{}'.",
                    SettingsPathRegistry.getCurrent(), ex);
        }

        if (props == null) {
            frame.setEventsSuspended(false);
            return;
        }

        // Update media lists once
        media.updateUIFromFilesystem(
                ui.txtWorkingDir.getText().trim(),
                ui.txtCartPath.getText().trim(),
                ui.txtFddPath.getText().trim(),
                ui.txtHddPath.getText().trim(),
                ui.txtCassPath.getText().trim()
        );

        restorePendingSelections(props);

        updateFrameTitle();

        frame.setEventsSuspended(false);
    }

    private void restoreLocale(Properties props) {

        String savedLocale = props.getProperty("locale");

        if (savedLocale != null && !savedLocale.isBlank()) {
            I18n.setLocale(Locale.forLanguageTag(savedLocale));
            return;
        }

        Locale sys = Locale.getDefault();

        if (sys.getLanguage().equals("de")) {
            I18n.setLocale(I18n.LOCALE_DE_DE);
        } else if (sys.getLanguage().equals("en")) {
            I18n.setLocale(I18n.LOCALE_EN_GB);
        } else {
            I18n.setLocale(I18n.LOCALE_DE_DE);
        }
    }

    private void restorePendingSelections(Properties props) {

        state.setPendingSelection(ui.cbxCartridge, props.getProperty("cbxCartridge"));
        state.setPendingSelection(ui.cbxFlop1, props.getProperty("cbxFlop1"));
        state.setPendingSelection(ui.cbxFlop2, props.getProperty("cbxFlop2"));
        state.setPendingSelection(ui.cbxFlop3, props.getProperty("cbxFlop3"));
        state.setPendingSelection(ui.cbxFlop4, props.getProperty("cbxFlop4"));
        state.setPendingSelection(ui.cbxHard1, props.getProperty("cbxHard1"));
        state.setPendingSelection(ui.cbxHard2, props.getProperty("cbxHard2"));
        state.setPendingSelection(ui.cbxHard3, props.getProperty("cbxHard3"));
        state.setPendingSelection(ui.cbxCass1, props.getProperty("cbxCass1"));
        state.setPendingSelection(ui.cbxCass2, props.getProperty("cbxCass2"));
    }

    // -------------------------------------------------------------------------
    // Title
    // -------------------------------------------------------------------------

    public void updateFrameTitle() {

        String baseTitle = "MAME TI99 Starter";

        Path current = SettingsPathRegistry.getCurrent();
        String configName = FileTools.prettyName(current);

        frame.setTitle(baseTitle + "   -   " + configName);
    }
}
