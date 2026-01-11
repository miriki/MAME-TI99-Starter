package com.miriki.ti99.mame.ui;

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.miriki.ti99.mame.domain.*;
import com.miriki.ti99.mame.service.*;

/**
 * Handles all media scanning and ComboBox model updates.
 * Extracted from MainAppFrame for clarity and modularity.
 */
public class MainAppFrameMediaUpdater {

    private final MainAppFrameComponents ui;
    private final MainAppFrameState state;

    // Scanner bleiben intern
    private final CartridgeScanner cartridgeScanner = new CartridgeScanner();
    private final FloppyScanner floppyScanner = new FloppyScanner();
    private final HarddiskScanner harddiskScanner = new HarddiskScanner();
    private final CassetteScanner cassetteScanner = new CassetteScanner();

    // Gemeinsame Listen – vom MainAppFrame übergeben!
    private final FloppyEntryList floppyList;
    private final HarddiskEntryList harddiskList;
    private final CassetteEntryList cassetteList;
    private final CartridgeEntryList cartridgeList;

    // -------------------------------------------------------------------------
    // Konstruktor – bekommt die Listen vom MainAppFrame
    // -------------------------------------------------------------------------
    public MainAppFrameMediaUpdater(
            MainAppFrameComponents ui,
            MainAppFrameState state,
            FloppyEntryList floppyList,
            HarddiskEntryList harddiskList,
            CassetteEntryList cassetteList,
            CartridgeEntryList cartridgeList
    ) {
        this.ui = ui;
        this.state = state;

        this.floppyList = floppyList;
        this.harddiskList = harddiskList;
        this.cassetteList = cassetteList;
        this.cartridgeList = cartridgeList;
    }

    // -------------------------------------------------------------------------
    // ComboBox model update with selection preservation
    // -------------------------------------------------------------------------

    private void updateComboBoxPreserve(JComboBox<String> cbx,
                                        List<String> items,
                                        String defaultSelection) {

        String pending = state.getPendingSelection(cbx);
        String previous = (String) cbx.getSelectedItem();

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement(defaultSelection);

        for (String s : items) {
            model.addElement(s);
        }

        cbx.setModel(model);

        String target;

        if (pending != null) {
            target = pending;
        } else if (previous != null && state.modelContains(model, previous)) {
            target = previous;
        } else {
            target = defaultSelection;
        }

        if (state.modelContains(model, target)) {
            cbx.setSelectedItem(target);
            state.setPendingSelection(cbx, null);
        } else {
            cbx.setSelectedItem(defaultSelection);
        }
    }

    // -------------------------------------------------------------------------
    // Media model updates
    // -------------------------------------------------------------------------

    private void updateCartridgeModel(String basePath, String cartPath, String none) {
        var entries = cartridgeScanner.scan(basePath, cartPath);
        cartridgeList.set(entries);
        updateComboBoxPreserve(ui.cbxCartridge, cartridgeList.getDisplayNames(), none);
    }

    private void updateFddModel(String basePath, String fddPath, String none) {
        var entries = floppyScanner.scan(basePath, fddPath);
        floppyList.set(entries);

        updateComboBoxPreserve(ui.cbxFlop1, floppyList.getDisplayNames(), none);
        updateComboBoxPreserve(ui.cbxFlop2, floppyList.getDisplayNames(), none);
        updateComboBoxPreserve(ui.cbxFlop3, floppyList.getDisplayNames(), none);
        updateComboBoxPreserve(ui.cbxFlop4, floppyList.getDisplayNames(), none);
    }

    private void updateHddModel(String basePath, String hddPath, String none) {
        var entries = harddiskScanner.scan(basePath, hddPath);
        harddiskList.set(entries);

        updateComboBoxPreserve(ui.cbxHard1, harddiskList.getDisplayNames(), none);
        updateComboBoxPreserve(ui.cbxHard2, harddiskList.getDisplayNames(), none);
        updateComboBoxPreserve(ui.cbxHard3, harddiskList.getDisplayNames(), none);
    }

    private void updateCassModel(String basePath, String cassPath, String none) {
        var entries = cassetteScanner.scan(basePath, cassPath);
        cassetteList.set(entries);

        updateComboBoxPreserve(ui.cbxCass1, cassetteList.getDisplayNames(), none);
        updateComboBoxPreserve(ui.cbxCass2, cassetteList.getDisplayNames(), none);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Updates all media ComboBoxes based on the filesystem.
     */
    public void updateUIFromFilesystem(String basePath,
                                       String cartPath,
                                       String fddPath,
                                       String hddPath,
                                       String cassPath) {

        final String none = UiConstants.CBX_SEL_NONE;

        // Optional: Debug
        // log.debug("updateUIFromFilesystem( basePath='{}', cartPath='{}', fddPath='{}', hddPath='{}', cassPath='{}' )",
        //        basePath, cartPath, fddPath, hddPath, cassPath);

        updateCartridgeModel(basePath, cartPath, none);
        updateFddModel(basePath, fddPath, none);
        updateHddModel(basePath, hddPath, none);
        updateCassModel(basePath, cassPath, none);
    }

    // -------------------------------------------------------------------------
    // Accessors for state
    // -------------------------------------------------------------------------

    public FloppyEntryList getFloppyList()     { return floppyList; }
    public HarddiskEntryList getHarddiskList() { return harddiskList; }
    public CassetteEntryList getCassetteList() { return cassetteList; }
    public CartridgeEntryList getCartridgeList() { return cartridgeList; }
}
