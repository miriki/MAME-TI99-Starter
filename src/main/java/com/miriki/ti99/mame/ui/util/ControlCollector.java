package com.miriki.ti99.mame.ui.util;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Collects named Swing controls from a component hierarchy.
 * <p>
 * Scans recursively and returns all {@link JComponent}s whose names start
 * with "cbx" or "txt".
 */
public class ControlCollector {

    /**
     * Collects all matching controls under the given root container.
     */
    public static Map<String, JComponent> collectControls(Container root) {
        Map<String, JComponent> map = new HashMap<>();
        collectRecursive(root, map);
        return map;
    }

    /**
     * Recursively scans a container and collects named components.
     */
    public static void collectRecursive(Container container, Map<String, JComponent> map) {

        for (Component comp : container.getComponents()) {

            // Named JComponents
            if (comp instanceof JComponent jc) {
                String name = jc.getName();
                if (name != null && (name.startsWith("cbx") || name.startsWith("txt"))) {
                    map.put(name, jc);
                }
            }

            // JTabbedPane → scan each tab
            if (comp instanceof JTabbedPane tabs) {
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    Component tabComp = tabs.getComponentAt(i);
                    if (tabComp instanceof Container child) {
                        collectRecursive(child, map);
                    }
                }
            }

            // Generic container recursion
            else if (comp instanceof Container child && child.getComponentCount() > 0) {
                collectRecursive(child, map);
            }
        }
    }
}
