package com.miriki.ti99.mame.persistence;

import java.awt.Component;
import java.awt.Container;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.tools.FileTools;
import com.miriki.ti99.mame.ui.util.ControlCollector;

/**
 * Handles saving and restoring UI control values to and from a settings file.
 * <p>
 * Only {@link JTextField} and {@link JComboBox} components are persisted.
 * Additional controls inside tab containers are collected recursively.
 */
public class SettingsManager {

    private static final Logger log = LoggerFactory.getLogger(SettingsManager.class);

    // -------------------------------------------------------------------------
    // Saving
    // -------------------------------------------------------------------------

    /**
     * Saves all control values into the given target file.
     *
     * @param controls mapping of control keys to UI components
     * @param tabMap   mapping of tab identifiers to tab root components
     * @param targetFile the file to write to
     */
    public static void save(Map<String, JComponent> controls,
                            Map<String, Component> tabMap,
                            Path targetFile) {

        Properties props = new Properties();

        // Save direct controls
        for (var entry : controls.entrySet()) {
            writeComponentValue(props, entry.getKey(), entry.getValue());
        }

        // Save controls inside tab containers
        for (var entry : tabMap.entrySet()) {
            Component comp = entry.getValue();

            if (comp instanceof Container container) {
                Map<String, JComponent> extraControls = new HashMap<>();
                ControlCollector.collectRecursive(container, extraControls);

                for (var e2 : extraControls.entrySet()) {
                    writeComponentValue(props, e2.getKey(), e2.getValue());
                }
            }
        }

        // Store locale
        props.setProperty("locale", I18n.getLocale().toLanguageTag());

        if (!FileTools.canWriteFile(targetFile)) {
            log.warn("Kann Einstellungen nicht speichern: Datei oder Verzeichnis nicht beschreibbar: {}", targetFile);
            return;
        }

        try (OutputStream out = Files.newOutputStream(targetFile)) {
            props.store(out, "Emulator GUI Settings");
            log.info("Einstellungen gespeichert in '{}'", targetFile);
        } catch (IOException e) {
            log.warn("Fehler beim Speichern der Einstellungen in '{}'", targetFile, e);
        }
    }

    /**
     * Writes a single component value into the properties map.
     */
    private static void writeComponentValue(Properties props, String key, JComponent comp) {
        if (comp instanceof JTextField tf) {
            props.setProperty(key, tf.getText());
        } else if (comp instanceof JComboBox<?> cbx) {
            Object sel = cbx.getSelectedItem();
            if (sel != null) {
                props.setProperty(key, sel.toString());
            }
        }
    }

    // -------------------------------------------------------------------------
    // Restoring
    // -------------------------------------------------------------------------

    /**
     * Restores control values from the given settings file.
     *
     * @param controls mapping of control keys to UI components
     * @param sourceFile the file to read from
     * @return the loaded properties
     */
    public static Properties restore(Map<String, JComponent> controls,
                                     Path sourceFile) throws IOException {

        Properties result = new Properties();

        if (!Files.exists(sourceFile)) {
            log.warn("No settings file found at '{}', skipping restore.", sourceFile);
            return result;
        }

        try (InputStream in = Files.newInputStream(sourceFile)) {
            result.load(in);
            log.info("Einstellungen geladen aus '{}'", sourceFile);
        }

        for (var entry : controls.entrySet()) {
            String key = entry.getKey();
            String value = result.getProperty(key);

            if (value != null) {
                JComponent comp = entry.getValue();

                if (comp instanceof JTextField tf) {
                    tf.setText(value);
                } else if (comp instanceof JComboBox<?> cbx) {
                    cbx.setSelectedItem(value);
                }
            }
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Convenience wrappers
    // -------------------------------------------------------------------------

    public static void saveToCurrent(Map<String, JComponent> controls,
                                     Map<String, Component> tabMap) {
        save(controls, tabMap, SettingsPathRegistry.getCurrent());
    }

    public static Properties restoreFromCurrent(Map<String, JComponent> controls)
            throws IOException {
        return restore(controls, SettingsPathRegistry.getCurrent());
    }
}
