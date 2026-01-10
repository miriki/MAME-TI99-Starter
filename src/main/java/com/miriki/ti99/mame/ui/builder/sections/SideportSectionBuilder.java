package com.miriki.ti99.mame.ui.builder.sections;

import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;
import com.miriki.ti99.mame.ui.builder.UiFactory;

/**
 * Builds the sideport device section.
 */
public class SideportSectionBuilder {

    @SuppressWarnings("unused")
    private final MainAppFrame frame;
    private final MainAppFrameComponents ui;
    @SuppressWarnings("unused")
    private final UiFactory factory;

    public SideportSectionBuilder(MainAppFrame frame,
                                  MainAppFrameComponents ui,
                                  UiFactory factory) {
        this.frame = frame;
        this.ui = ui;
        this.factory = factory;
    }

    /**
     * Adds the sideport device tabs.
     */
    public void build() {

        // Speech Synthesizer
        ui.tabSideportDevices.addTab("speechsyn", null, ui.Panel_SpeechSyn, null);
        ui.Panel_SpeechSyn.putClientProperty("deviceKey", "speechsyn");
        ui.Panel_SpeechSyn.setLayout(null);

        // Splitter
        ui.tabSideportDevices.addTab("splitter", null, ui.Panel_Splitter, null);
        ui.Panel_Splitter.putClientProperty("deviceKey", "splitter");
        ui.Panel_Splitter.setLayout(null);

        // Arcturus
        ui.tabSideportDevices.addTab("arcturus", null, ui.Panel_Arcturus, null);
        ui.Panel_Arcturus.putClientProperty("deviceKey", "arcturus");
        ui.Panel_Arcturus.setLayout(null);
    }
}
