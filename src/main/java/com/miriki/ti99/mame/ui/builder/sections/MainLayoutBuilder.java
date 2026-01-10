package com.miriki.ti99.mame.ui.builder.sections;

import java.awt.Container;

import javax.swing.JMenuBar;

import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;
import com.miriki.ti99.mame.ui.builder.UiFactory;
import com.miriki.ti99.mame.ui.menu.MenuBarBuilder;

/**
 * Builds the main layout structure of the application window.
 */
public class MainLayoutBuilder {

    private final MainAppFrame frame;
    private final MainAppFrameComponents ui;
    @SuppressWarnings("unused")
    private final UiFactory factory;

    public MainLayoutBuilder(MainAppFrame frame,
                             MainAppFrameComponents ui,
                             UiFactory factory) {
        this.frame = frame;
        this.ui = ui;
        this.factory = factory;
    }

    /**
     * Builds the main layout structure.
     */
    public void build() {

        // Root content pane
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(null);

        // Menu bar
        JMenuBar bar = new MenuBarBuilder(frame, ui).build();
        frame.setJMenuBar(bar);

        // Sideport devices tabbed pane
        ui.contentPane.add(ui.tabSideportDevices);
        ui.tabSideportDevices.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        ui.tabSideportDevices.setBounds(8, 272, 464 + 96 + 32, 352 - 24);

        // PEB container (CardLayout)
        ui.tabSideportDevices.addTab("peb", null, ui.Panel_PEB, null);
        ui.Panel_PEB.setLayout(new java.awt.CardLayout(0, 0));

        ui.Panel_PEB.add(ui.tabPebDevices, "tabPebDevices");
        ui.tabPebDevices.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
    }
}
