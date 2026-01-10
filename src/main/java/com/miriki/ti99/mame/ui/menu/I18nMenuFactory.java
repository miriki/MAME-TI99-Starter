package com.miriki.ti99.mame.ui.menu;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.miriki.ti99.mame.localization.I18n;

/**
 * Factory for creating translated menu components.
 */
public final class I18nMenuFactory {

    private I18nMenuFactory() {}

    /**
     * Creates a translated top-level menu and adds it to the menu bar.
     */
    public static JMenu createMenu(JMenuBar bar, String key) {
        JMenu menu = new JMenu(I18n.t(key));
        menu.putClientProperty("i18n", key);
        bar.add(menu);
        return menu;
    }

    /**
     * Creates a translated submenu and adds it to the parent menu.
     */
    public static JMenu createSubMenu(JMenu parent, String key) {
        JMenu menu = new JMenu(I18n.t(key));
        menu.putClientProperty("i18n", key);
        parent.add(menu);
        return menu;
    }

    /**
     * Creates a translated menu item and adds it to the parent menu.
     */
    public static JMenuItem createMenuItem(JMenu parent, String key) {
        JMenuItem item = new JMenuItem(I18n.t(key));
        item.putClientProperty("i18n", key);
        parent.add(item);
        return item;
    }

    /**
     * Creates a translated checkbox menu item and adds it to the parent menu.
     */
    public static JCheckBoxMenuItem createCheckBoxItem(JMenu parent, String key) {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(I18n.t(key));
        item.putClientProperty("i18n", key);
        parent.add(item);
        return item;
    }
}
