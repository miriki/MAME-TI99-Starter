package com.miriki.ti99.mame.ui.mamedevices;

import javax.swing.*;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls the device tabs inside the PEB tabbed pane.
 */
public class PebDevicesController {

    private final JTabbedPane tabbedPane;

    /** Maps canonical device keys to tab components. */
    private final Map<String, Component> tabMap = new HashMap<>();

    public Map<String, Component> getTabMap() {
        return tabMap;
    }

    /**
     * Creates a controller for the given tabbed pane and initializes the registry.
     */
    public PebDevicesController(JTabbedPane extTabbedPane) {
        this.tabbedPane = extTabbedPane;
        init();
    }

    // -------------------------------------------------------------------------
    // Initialization
    // -------------------------------------------------------------------------

    /**
     * Scans existing tabs, extracts canonical keys and replaces titles.
     */
    private void init() {

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {

            String originalTitle = tabbedPane.getTitleAt(i);
            Component comp = tabbedPane.getComponentAt(i);

            String key = PebDevices.toParamName(originalTitle);
            tabMap.put(key, comp);

            String pretty = PebDevices.toPrettyTitle(originalTitle);
            tabbedPane.setTitleAt(i, pretty);
        }
    }

    // -------------------------------------------------------------------------
    // Tab update logic
    // -------------------------------------------------------------------------

    /**
     * Updates visible tabs to match the given canonical device keys.
     */
    public void updateTabs(List<String> selectedTitles) {

        // Remember previously selected tab
        int oldIndex = tabbedPane.getSelectedIndex();
        String oldKey = null;

        if (oldIndex >= 0) {
            Component oldComp = tabbedPane.getComponentAt(oldIndex);
            if (oldComp instanceof JComponent jc) {
                oldKey = (String) jc.getClientProperty("deviceKey");
            }
        }

        // Current order
        List<String> current = new ArrayList<>();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component c = tabbedPane.getComponentAt(i);
            if (c instanceof JComponent jc) {
                current.add((String) jc.getClientProperty("deviceKey"));
            }
        }

        // Desired order
        List<String> desired = new ArrayList<>(selectedTitles);

        if (current.equals(desired)) {
            return;
        }

        // Rebuild tabs
        tabbedPane.removeAll();

        for (String key : desired) {

            Component comp = tabMap.get(key);
            if (comp == null) continue;

            String pretty = PebDevices.toPrettyTitle(key);
            tabbedPane.addTab(pretty, comp);

            if (comp instanceof JComponent jc) {
                jc.putClientProperty("deviceKey", key);
            }
        }

        // Restore selection
        if (oldKey != null) {
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                Component c = tabbedPane.getComponentAt(i);
                if (c instanceof JComponent jc) {
                    if (oldKey.equals(jc.getClientProperty("deviceKey"))) {
                        tabbedPane.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }
}
