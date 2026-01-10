package com.miriki.ti99.mame.ui.menu;

import javax.swing.JMenu;

import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;

/**
 * Builds the File menu.
 */
public class FileMenuBuilder {

    private final MainAppFrameComponents ui;
    private final MainAppFrame frame;

    public FileMenuBuilder(MainAppFrameComponents ui, MainAppFrame frame) {
        this.ui = ui;
        this.frame = frame;
    }

    /**
     * Populates the given File menu.
     */
    public void build(JMenu menu) {

        ui.menuFile = menu;

        ui.menuFileExit = I18nMenuFactory.createMenuItem(menu, "menu.file.exit");

        // Clean shutdown via MainAppFrame
        ui.menuFileExit.addActionListener(e -> frame.exitApplication());
    }
}
