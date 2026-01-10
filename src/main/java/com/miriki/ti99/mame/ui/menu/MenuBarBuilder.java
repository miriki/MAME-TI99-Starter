package com.miriki.ti99.mame.ui.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;

/**
 * Builds the application's main menu bar.
 */
public class MenuBarBuilder {

    private final MainAppFrame frame;
    private final MainAppFrameComponents ui;

    /**
     * Creates a new menu bar builder.
     */
    public MenuBarBuilder(MainAppFrame frame, MainAppFrameComponents ui) {
        this.frame = frame;
        this.ui = ui;
    }

    /**
     * Builds and returns the complete menu bar.
     */
    public JMenuBar build() {

        JMenuBar menuBar = new JMenuBar();

        // Top-level menus
        JMenu fileMenu     = I18nMenuFactory.createMenu(menuBar, "menu.file");
        JMenu languageMenu = I18nMenuFactory.createMenu(menuBar, "menu.lang");
        JMenu settingsMenu = I18nMenuFactory.createMenu(menuBar, "menu.settings");
        JMenu helpMenu     = I18nMenuFactory.createMenu(menuBar, "menu.help");

        // Populate menus
        new FileMenuBuilder(ui, frame).build(fileMenu);
        new LanguageMenuBuilder(ui, frame).build(languageMenu);
        new SettingsMenuBuilder(frame, ui).build(settingsMenu);
        new HelpMenuBuilder(frame, ui).build(helpMenu);

        return menuBar;
    }
}
