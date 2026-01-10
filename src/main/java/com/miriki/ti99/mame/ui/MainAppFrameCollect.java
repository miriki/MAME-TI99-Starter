package com.miriki.ti99.mame.ui;

import com.miriki.ti99.mame.dto.EmulatorOptionsDTO;

/**
 * Handles the collection of emulator options from the UI.
 * Extracted from MainAppFrame for clarity and modularity.
 */
public class MainAppFrameCollect {

    private final MainAppFrame frame;
    private final MainAppFrameComponents ui;
    private final MainAppFrameState state;
    private final MainAppFrameMediaUpdater media;

    public MainAppFrameCollect(MainAppFrame frame,
                               MainAppFrameComponents ui,
                               MainAppFrameState state,
                               MainAppFrameMediaUpdater media) {
        this.frame = frame;
        this.ui = ui;
        this.state = state;
        this.media = media;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Collects all emulator options from the UI.
     * Suspends events during the process to avoid recursive updates.
     */
    public EmulatorOptionsDTO collectEmulatorOptions() {

        if (frame.getEventsSuspended()) {
            return null;
        }

        frame.setEventsSuspended(true);

        String basePath = ui.txtWorkingDir.getText().trim();
        String cartPath = ui.txtCartPath.getText().trim();
        String fddPath  = ui.txtFddPath.getText().trim();
        String hddPath  = ui.txtHddPath.getText().trim();
        String cassPath = ui.txtCassPath.getText().trim();

        // Update UI models from filesystem
        media.updateUIFromFilesystem(basePath, cartPath, fddPath, hddPath, cassPath);

        // Build DTO from UI state
        EmulatorOptionsDTO result = state.buildDTOFromUI();

        frame.setEventsSuspended(false);

        return result;
    }
}
