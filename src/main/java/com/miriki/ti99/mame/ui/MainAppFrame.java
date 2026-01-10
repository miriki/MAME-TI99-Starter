package com.miriki.ti99.mame.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.dto.EmulatorOptionsDTO;
import com.miriki.ti99.mame.ui.builder.MainAppFrameBuilder;
import com.miriki.ti99.mame.ui.mamedevices.PebDevicesController;
import com.miriki.ti99.mame.ui.util.Listeners;
import com.miriki.ti99.mame.ui.util.UiDefaults;

/**
 * Main application window.
 * Now slim, modular and delegating all logic to helper classes.
 */
public class MainAppFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(MainAppFrame.class);

    // UI components
    private final MainAppFrameComponents ui;

    // Subsystems
    private final MainAppFrameBuilder builder;
    private final MainAppFrameState state;
    private final MainAppFrameMediaUpdater media;
    private final MainAppFrameSettings settings;
    private final MainAppFrameI18n i18n;
    private final MainAppFrameCollect collector;

    // PEB controller
    private PebDevicesController ctlPebDevices;
    public PebDevicesController getPebDevicesController() { return ctlPebDevices; }
    public void setPebDevicesController(PebDevicesController ctl) { this.ctlPebDevices = ctl; }

    // Event suspension flag
    private boolean eventsSuspended = true;
    public boolean getEventsSuspended() { return eventsSuspended; }
    public void setEventsSuspended(boolean evtSusp) { this.eventsSuspended = evtSusp; }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public MainAppFrame() {

        setEventsSuspended(true);

        setTitle("MAME TI99 Starter");
        setBounds(100, 100, 1200, 675);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        UiDefaults.apply();

        ui = new MainAppFrameComponents();
        setContentPane(ui.getContentPane());

        // Build subsystems
        builder   = new MainAppFrameBuilder(this, ui);
        state     = new MainAppFrameState(this, ui);
        media     = new MainAppFrameMediaUpdater(ui, state);
        settings  = new MainAppFrameSettings(this, ui, state, media);
        i18n      = new MainAppFrameI18n(this, ui);
        collector = new MainAppFrameCollect(this, ui, state, media);

        builder.initGUI();

        addWindowListener(Listeners.onCloseMainFrame(this));
    }

    // -------------------------------------------------------------------------
    // Application exit
    // -------------------------------------------------------------------------

    public void exitApplication() {
        settings.saveSettings();
        dispose();
        System.exit(0);
    }

    // -------------------------------------------------------------------------
    // Settings
    // -------------------------------------------------------------------------

    public void restoreSettings() {
        settings.restoreSettings();
    }

    public void saveSettings() {
        settings.saveSettings();
    }

    public void updateFrameTitle() {
        settings.updateFrameTitle();
    }

    // -------------------------------------------------------------------------
    // I18n
    // -------------------------------------------------------------------------

    public void refreshUI() {
        i18n.refreshUI();
    }

    // -------------------------------------------------------------------------
    // Collect emulator options
    // -------------------------------------------------------------------------

    public EmulatorOptionsDTO collectEmulatorOptions() {
        return collector.collectEmulatorOptions();
    }

    // -------------------------------------------------------------------------
    // Main entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            try {
                MainAppFrame frame = new MainAppFrame();
                frame.setVisible(true);

                SwingUtilities.invokeLater(() -> {
                    frame.builder.postGUI();
                    frame.setEventsSuspended(false);
                    frame.collectEmulatorOptions();
                });

            } catch (Exception e) {
                log.error("Error starting the application", e);
            }
        });
    }
}
