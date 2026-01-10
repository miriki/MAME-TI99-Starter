package com.miriki.ti99.mame.ui.menu;

import javax.swing.JMenu;

/**
 * Creates dynamic (non‑I18n) submenus for runtime-generated entries.
 */
public final class MenuFactory {

    private MenuFactory() {
        // utility class
    }

    /**
     * Creates a submenu with the given label and adds it to the parent menu.
     */
    public static JMenu createMenu(JMenu parent, String label) {
        JMenu menu = new JMenu(label);
        parent.add(menu);
        return menu;
    }
}
