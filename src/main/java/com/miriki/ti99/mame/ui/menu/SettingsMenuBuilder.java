package com.miriki.ti99.mame.ui.menu;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.nio.file.*;
import java.awt.Component;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.tools.FileTools;
import com.miriki.ti99.mame.persistence.SettingsPathRegistry;
import com.miriki.ti99.mame.persistence.SettingsUsageRegistry;
import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;

/**
 * Populates the dynamic "Settings" menu.
 */
public class SettingsMenuBuilder {

    private final MainAppFrameComponents ui;
    private final MainAppFrame frame;

    private static final Logger log = LoggerFactory.getLogger(SettingsMenuBuilder.class);

    public SettingsMenuBuilder(MainAppFrame frame, MainAppFrameComponents ui) {
        this.frame = frame;
        this.ui = ui;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Populates the given Settings menu.
     */
    public void build(JMenu menu) {

        ui.menuSettings = menu;

        // Rebuild menu dynamically whenever opened
        menu.addMenuListener(new MenuListener() {
            @Override public void menuSelected(MenuEvent e) { rebuildSettingsMenu(menu); }
            @Override public void menuDeselected(MenuEvent e) {}
            @Override public void menuCanceled(MenuEvent e) {}
        });

        // Initial build
        rebuildSettingsMenu(menu);
    }

    // -------------------------------------------------------------------------
    // File normalization
    // -------------------------------------------------------------------------

    private void normalizeDirectory(Path dir) {
        try (Stream<Path> stream = Files.list(dir)) {
            stream.filter(p -> p.getFileName().toString().endsWith(".settings"))
                  .forEach(this::normalizeSingleFile);
        } catch (IOException ex) {
            log.warn("Could not normalize directory '{}': {}", dir, ex);
        }
    }

    private Path normalizeSingleFile(Path result) {

        String fileName = result.getFileName().toString();
        String safe = FileTools.safeFileName(fileName);

        if (!safe.equals(fileName)) {
            Path newPath = result.resolveSibling(safe);
            try {
                Files.move(result, newPath);
                SettingsUsageRegistry.renameEntry(result, newPath);
                result = newPath;
            } catch (IOException ex) {
                log.warn("Could not normalize filename {} → {}", result, newPath, ex);
                result = null;
            }
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // File chooser
    // -------------------------------------------------------------------------

    private Path chooseSettingsFile(boolean forSave) {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(forSave ? "Save Settings As …" : "Load Settings");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Settings files (*.settings)", "settings"));

        Path current = SettingsPathRegistry.getCurrent();
        if (current != null && Files.exists(current)) {
            chooser.setSelectedFile(current.toFile());
        } else {
            SettingsPathRegistry.setCurrent(null);
        }

        int fs = forSave
                ? chooser.showSaveDialog(frame)
                : chooser.showOpenDialog(frame);

        if (fs != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File file = chooser.getSelectedFile();

        if (forSave && !file.getName().toLowerCase().endsWith(".settings")) {
            file = new File(file.getParentFile(), file.getName() + ".settings");
        }

        return file.toPath();
    }

    // -------------------------------------------------------------------------
    // Save / Load
    // -------------------------------------------------------------------------

    private void doSaveAs() {

        Path chosen = chooseSettingsFile(true);
        if (chosen == null) return;

        String rawName = chosen.getFileName().toString();
        String safeName = FileTools.safeFileName(rawName);
        Path safePath = chosen.resolveSibling(safeName);

        SettingsPathRegistry.setCurrent(safePath);
        frame.saveSettings();
    }

    private void doLoad() {

        Path chosen = chooseSettingsFile(false);
        if (chosen == null) return;

        chosen = normalizeSingleFile(chosen);
        normalizeDirectory(chosen.getParent());

        SettingsPathRegistry.setCurrent(chosen);
        frame.restoreSettings();
        frame.collectEmulatorOptions();
    }

    // -------------------------------------------------------------------------
    // Menu rebuild
    // -------------------------------------------------------------------------

    private void rebuildSettingsMenu(JMenu menu) {

        menu.removeAll();

        // --- Save ---
        ui.menuSettingsSave = I18nMenuFactory.createMenuItem(menu, "menu.settings.save");
        ui.menuSettingsSave.addActionListener(e -> {
            Path current = SettingsPathRegistry.getCurrent();
            if (!Files.exists(current)) {
                doSaveAs();
            } else {
                frame.saveSettings();
            }
        });

        // --- Save As ---
        ui.menuSettingsSaveAs = I18nMenuFactory.createMenuItem(menu, "menu.settings.saveas");
        ui.menuSettingsSaveAs.addActionListener(e -> doSaveAs());

        menu.addSeparator();

        // --- Load ---
        ui.menuSettingsLoad = I18nMenuFactory.createMenuItem(menu, "menu.settings.load");
        ui.menuSettingsLoad.addActionListener(e -> doLoad());

        menu.addSeparator();

        // Cleanup registries
        SettingsUsageRegistry.cleanupInvalidEntries();
        SettingsPathRegistry.cleanupCurrentIfInvalid();

        List<Path> top = SettingsUsageRegistry.getTop(10);

        if (top.isEmpty()) {
            ui.menuSettingsNoSel = I18nMenuFactory.createMenuItem(menu, "menu.settings.nosel");
            ui.menuSettingsNoSel.setEnabled(false);
            return;
        }

        // --- Pick submenu ---
        ui.menuSettingsPick = I18nMenuFactory.createSubMenu(menu, "menu.settings.pick");

        ui.menuSettingsPickByCount = I18nMenuFactory.createMenuItem(ui.menuSettingsPick, "menu.settings.pick.bycount");
        ui.menuSettingsPickByCount.addActionListener(e -> {
            SettingsUsageRegistry.setSortMode(SettingsUsageRegistry.SortMode.BY_COUNT);
            rebuildSettingsMenu(menu);
        });

        ui.menuSettingsPickByDate = I18nMenuFactory.createMenuItem(ui.menuSettingsPick, "menu.settings.pick.bydate");
        ui.menuSettingsPickByDate.addActionListener(e -> {
            SettingsUsageRegistry.setSortMode(SettingsUsageRegistry.SortMode.BY_DATE);
            rebuildSettingsMenu(menu);
        });

     // --- Recent entries ---
        for (Path p : top) {
            if (!Files.exists(p)) continue;

            String label = FileTools.prettyName(p);
            Path current = SettingsPathRegistry.getCurrent();

            JMenu configMenu = MenuFactory.createMenu(menu, label);

            // Bullet icon for current config
            if (p.equals(current)) {
                configMenu.setIcon(new Icon() {
                    @Override public int getIconWidth() { return 8; }
                    @Override public int getIconHeight() { return 8; }
                    @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                        g.fillOval(x, y, 7, 7);
                    }
                });
            }

            // Load
            ui.menuSettingsPickLoad =
                I18nMenuFactory.createMenuItem(configMenu, "menu.settings.pick.load");
            ui.menuSettingsPickLoad.addActionListener(e -> {
                frame.saveSettings();
                SettingsPathRegistry.setCurrent(p);
                frame.restoreSettings();
                frame.updateFrameTitle();
                log.info("Switched settings to '{}'", p);
            });

            configMenu.addSeparator();

            // Delete
            ui.menuSettingsPickDelete =
                I18nMenuFactory.createMenuItem(configMenu, "menu.settings.pick.delete");
            ui.menuSettingsPickDelete.addActionListener(e -> {
                try {
                    Files.deleteIfExists(p);
                    SettingsUsageRegistry.removeEntry(p);
                    rebuildSettingsMenu(menu);
                } catch (IOException ex) {
                    log.warn("Error deleting '{}'", p, ex);
                }
            });

            // Rename
            ui.menuSettingsPickRename =
                I18nMenuFactory.createMenuItem(configMenu, "menu.settings.pick.rename");
            ui.menuSettingsPickRename.addActionListener(e -> {
                String oldName = FileTools.editName(p);
                String newName = JOptionPane.showInputDialog(frame, "Rename config:", oldName);

                if (newName != null && !newName.isBlank()) {
                    String safe = FileTools.safeFileName(newName);
                    Path newPath = p.resolveSibling(safe);

                    try {
                        Files.move(p, newPath);
                        SettingsUsageRegistry.renameEntry(p, newPath);
                        SettingsPathRegistry.setCurrent(newPath);
                        frame.restoreSettings();
                        frame.updateFrameTitle();
                        rebuildSettingsMenu(menu);
                    } catch (IOException ex) {
                        log.warn("Rename failed: {} → {}", p, newPath, ex);
                    }
                }
            });
        }

        menu.revalidate();
        menu.repaint();
    }
}
