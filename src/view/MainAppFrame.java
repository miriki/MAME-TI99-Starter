package view;

import java.awt.Component;
import java.awt.EventQueue;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FileTools.FileTools;
import MameTools.EmulatorOptionsDTO;
import MameTools.MameTools;
import persistence.SettingsManager;
import persistence.SettingsPathRegistry;
import view.util.ControlCollector;

// ############################################################################

public class MainAppFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(MainAppFrame.class);

    private final MainAppFrameUI ui;
    
    private MirikiPebDevicesController ctlPebDevices;
    public MirikiPebDevicesController getPebDevicesController() { return this.ctlPebDevices; }
    public void setPebDevicesController( MirikiPebDevicesController ctl ) { this.ctlPebDevices = ctl; }

    private final Map<JComboBox<?>,String> pendingSelections = new HashMap<>();
    public String getPendingSelection( JComboBox<?> cbx ) { return pendingSelections.get( cbx ); }
    public void setPendingSelection( JComboBox<?> cbx, String value ) { pendingSelections.put( cbx, value ); }
 
    private boolean initializing = true;
    public boolean isInitializing() { return initializing; }
    public void setInitializing(boolean initializing) { this.initializing = initializing; }
    
	// --------------------------------------------------
    
    public MainAppFrame() {
    	
        log.debug("[constructor] MainAppFrame()" );

    	setInitializing( true );

    	setTitle( "MAME TI99 Starter" );
        setBounds( 100, 100, 1200, 675 );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        ui = new MainAppFrameUI();
        setContentPane( ui.getContentPane() );
        
    	// log.warn( "initialize gui - start" );
        MainAppFrameBuilder builder = new MainAppFrameBuilder( this, ui );
    	// log.warn( "  initGUI()" );
        builder.initGUI();
    	// log.warn( "  postGUI()" );
        builder.postGUI();
    	// log.warn( "  collectEmulatorOptions()" );
        collectEmulatorOptions();
    	// log.warn( "initialize gui - end" );

    	// setInitializing( false );

    } // [constructor] MainAppFrame
    
	// --------------------------------------------------
    
    public void updateFrameTitle() {
        String baseTitle = "MAME TI99 Starter";

        // Aktuellen Settings-Pfad holen
        Path current = SettingsPathRegistry.getCurrent();

        String configName = FileTools.prettyName(current);

        // Titel zusammensetzen
        setTitle(baseTitle + "   -   " + configName);
    }
    
	// --------------------------------------------------
    
    public void restoreSettings() {
        
    	Map<String, JComponent> controls = ControlCollector.collectControls( this );

    	// log.warn( "restoreSettings() - start" );

        try {
        	setInitializing( true);
        	Properties props = SettingsManager.restoreFromCurrent( controls );
        	setPendingSelection( ui.cbxCartridge, props.getProperty( "cbxCartridge" ));
        	setPendingSelection( ui.cbxFlop1, props.getProperty( "cbxFlop1" ));
        	setPendingSelection( ui.cbxFlop2, props.getProperty( "cbxFlop2" ));
        	setPendingSelection( ui.cbxFlop3, props.getProperty( "cbxFlop3" ));
        	setPendingSelection( ui.cbxFlop4, props.getProperty( "cbxFlop4" ));
        	setPendingSelection( ui.cbxHard1, props.getProperty( "cbxHard1" ));
        	setPendingSelection( ui.cbxHard2, props.getProperty( "cbxHard2" ));
        	setPendingSelection( ui.cbxHard3, props.getProperty( "cbxHard3" ));
        	setPendingSelection( ui.cbxCass1, props.getProperty( "cbxCass1" ));
        	setPendingSelection( ui.cbxCass2, props.getProperty( "cbxCass2" ));
        	setInitializing( false );
        	collectEmulatorOptions();
        	updateFrameTitle();
        } catch ( IOException ex ) {
            // log.warn( "Keine gespeicherten Einstellungen gefunden.", ex );
            log.warn("Keine gespeicherten Einstellungen gefunden für '{}'.", SettingsPathRegistry.getCurrent(), ex);
        }
        
    	// log.warn( "restoreSettings() - end" );
    	
    } // restoreSettings

	// --------------------------------------------------
    
    public void saveSettings() {

    	Map<String, JComponent> controls = ControlCollector.collectControls( this );
        Map<String, Component> tabMap = ctlPebDevices != null ? ctlPebDevices.getTabMap() : Map.of();

        // log.warn( "saveSettings() - start" );
        
        try {
            SettingsManager.saveToCurrent( controls, tabMap );
            log.info("Einstellungen gespeichert in '{}'.", SettingsPathRegistry.getCurrent());
        } catch ( IOException ex ) {
            // log.error( "Fehler beim Speichern der Einstellungen", ex );
            log.error("Fehler beim Speichern der Einstellungen in '{}'.", SettingsPathRegistry.getCurrent(), ex);
        }

        // log.warn( "saveSettings() - end" );
        
    }

	// --------------------------------------------------
	
    private void updateComboBoxPreserve( JComboBox<String> cbx, List<String> cbxItems, String defaultSelection ) {
	    
        // log.warn( "updateComboBoxPreserve() - start" );
    	setInitializing( true );
    	
    	String previousSelection = (String) cbx.getSelectedItem();
	    
	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>( cbxItems.toArray( new String[0] ));
	    cbx.setModel( model );
	    
	    String pendingSelection = getPendingSelection( cbx );
	    String newSelection;
	    
	    if ( pendingSelection != null) {
	        if ( cbxItems.contains( pendingSelection )) {
	            // cbx.setSelectedItem( pendingSelection );
	            newSelection = pendingSelection;
	        } else {
	            // cbx.setSelectedItem( defaultSelection );
	            newSelection = defaultSelection;
	        }
	        // setPendingCartridgeSelection( null ); // einmalig verbrauchen
	        setPendingSelection( cbx, null );
	    }
	    else if ( previousSelection != null && cbxItems.contains( previousSelection )) {
	        // cbx.setSelectedItem( previousSelection );
	        newSelection = previousSelection;
	    } else {
	        // cbx.setSelectedItem( defaultSelection );
            newSelection = defaultSelection;
	    }

	    // Nur setzen, wenn sich der Wert wirklich geändert hat
	    String current = (String) cbx.getSelectedItem();
	    log.debug("Restoring {} -> '{}'", cbx.getName(), current);
	    if (! Objects.equals( current, newSelection )) {
	        cbx.setSelectedItem( newSelection );
	    }
	    log.debug("After setSelectedItem: {}", cbx.getSelectedItem());
	    
    	setInitializing( false );
	    // log.warn( "updateComboBoxPreserve() - end" );
    }
    
    public EmulatorOptionsDTO collectEmulatorOptions() {

        // log.warn( "collectEmulatorOptions() - start" );
        
	    // long t0 = System.currentTimeMillis();
	    // long t1 = System.currentTimeMillis();

	    log.debug( "collectEmulatorOptions()" );
		
		final String cbxSelNone = "------";

	    String workingDir = ui.txtWorkingDir.getText().trim();
	    String cartPath = ui.txtCartPath.getText().trim();
	    String fddPath = ui.txtFddPath.getText().trim();
	    String hddPath = ui.txtHddPath.getText().trim();
	    String cassPath = ui.txtCassPath.getText().trim();
	    
	    EmulatorOptionsDTO dto = new EmulatorOptionsDTO();
		
	    if ( ! isInitializing() ) {

		    // t0 = System.currentTimeMillis();

		    // t1 = System.currentTimeMillis();
		    List<String> model = FileTools.scanDirectories( workingDir, cartPath, "zip" );
		    // log.warn( "    scanDirectories cart {} ms", System.currentTimeMillis() - t1 );
		    updateComboBoxPreserve( ui.cbxCartridge, model, cbxSelNone );
		    
		    // t1 = System.currentTimeMillis();
		    model = FileTools.scanDirectories( workingDir, fddPath, "dsk" );
		    // log.warn( "    scanDirectories dsk {} ms", System.currentTimeMillis() - t1 );
		    updateComboBoxPreserve( ui.cbxFlop1, model, cbxSelNone );
		    updateComboBoxPreserve( ui.cbxFlop2, model, cbxSelNone );
		    updateComboBoxPreserve( ui.cbxFlop3, model, cbxSelNone );
		    updateComboBoxPreserve( ui.cbxFlop4, model, cbxSelNone );

		    // t1 = System.currentTimeMillis();
		    model = FileTools.scanDirectories( workingDir, hddPath, "chd" );
		    // log.warn( "    scanDirectories chd {} ms", System.currentTimeMillis() - t1 );
		    updateComboBoxPreserve( ui.cbxHard1, model, cbxSelNone );
		    updateComboBoxPreserve( ui.cbxHard2, model, cbxSelNone );
		    updateComboBoxPreserve( ui.cbxHard3, model, cbxSelNone );

		    model = FileTools.scanDirectories( workingDir, cassPath, "wav" );
		    updateComboBoxPreserve( ui.cbxCass1, model, cbxSelNone );
		    updateComboBoxPreserve( ui.cbxCass2, model, cbxSelNone );

			dto.mame_Executable = ui.txtEmulator.getText().trim();
			dto.mame_WorkingPath = ui.txtWorkingDir.getText().trim();
			
			dto.mame_AddOpt = ui.txtAddOpt.getText().trim();
	
			dto.mame_RomPath = ui.txtRomPath.getText().trim();
			dto.mame_CartPath = ui.txtCartPath.getText().trim();
			
			dto.mame_DskPath = ui.txtFddPath.getText().trim();
			dto.mame_WdsPath = ui.txtHddPath.getText().trim();
			dto.mame_CsPath = ui.txtCassPath.getText().trim();
	
			dto.mame_Machine = ui.cbxMachine.getSelectedItem().toString();
	
			dto.mame_GromPort = ui.cbxGromPort.getSelectedItem().toString();
			dto.mame_Cartridge = ui.cbxCartridge.getSelectedItem().toString();
			
			dto.mame_JoyPort = ui.cbxJoystick.getSelectedItem().toString();
			
			dto.mame_IoPort = ui.cbxIoPort.getSelectedItem().toString();
	
			// media
			
			// dto.mame_CS1 = ui.txtCass1.getText().trim();
			// dto.mame_CS2 = ui.txtCass2.getText().trim();
			dto.mame_CS1 = ui.cbxCass1.getSelectedItem().toString();
			dto.mame_CS2 = ui.cbxCass2.getSelectedItem().toString();
	
			// dto.mame_DSK1 = ui.txtFlop1.getText().trim();
			// dto.mame_DSK2 = ui.txtFlop2.getText().trim();
			// dto.mame_DSK3 = ui.txtFlop3.getText().trim();
			// dto.mame_DSK4 = ui.txtFlop4.getText().trim();
			dto.mame_DSK1 = ui.cbxFlop1.getSelectedItem().toString();
			dto.mame_DSK2 = ui.cbxFlop2.getSelectedItem().toString();
			dto.mame_DSK3 = ui.cbxFlop3.getSelectedItem().toString();
			dto.mame_DSK4 = ui.cbxFlop4.getSelectedItem().toString();
	
			// dto.mame_WDS1 = ui.txtHard1.getText().trim();
			// dto.mame_WDS2 = ui.txtHard2.getText().trim();
			// dto.mame_WDS3 = ui.txtHard3.getText().trim();
			dto.mame_WDS1 = ui.cbxHard1.getSelectedItem().toString();
			dto.mame_WDS2 = ui.cbxHard2.getSelectedItem().toString();
			dto.mame_WDS3 = ui.cbxHard3.getSelectedItem().toString();
	
			// System.out.println( "    getting 'peb slots' fields" );
	
			// dto.PebDevices[1] = "peb";

			JComboBox<?>[] slots = { ui.cbxSlot2, ui.cbxSlot3, ui.cbxSlot4, ui.cbxSlot5, ui.cbxSlot6, ui.cbxSlot7, ui.cbxSlot8 };
			
			if ( "TI99_4ev".equals( dto.mame_Machine )) {
	        	ui.cbxIoPort.setSelectedIndex( 1 ); // peb
				ui.cbxIoPort.setEnabled(false);
	        	ui.cbxSlot1.setSelectedIndex( 2 ); // ti99
				ui.cbxSlot2.setSelectedIndex( 6 ); // evpc
				ui.cbxSlot2.setEnabled(false);
	        } else {
				ui.cbxIoPort.setEnabled(true);
	        	ui.cbxSlot2.setEnabled(true);
				for ( int i = 0; i < slots.length; i++ ) {
					JComboBox<?> slot = slots[i];
				    if ( "evpc".equals(slot.getSelectedItem().toString())) {
						slot.setSelectedIndex( 0 ); // not
				    }
				}
	        }
	        
	        if (( "geneve".equals( dto.mame_Machine )) || ( "genmod".equals( dto.mame_Machine ))) { 
	        	ui.cbxIoPort.setSelectedIndex( 1 ); // peb
				ui.cbxIoPort.setEnabled(false);
				ui.cbxSlot1.setSelectedIndex( 3 ); // geneve
				for ( int i = 0; i < slots.length; i++ ) {
					JComboBox<?> slot = slots[i];
				    if ( "evpc".equals(slot.getSelectedItem().toString())) {
						slot.setSelectedIndex( 0 ); // no evpc
				    }
				}
			} else {
	        	ui.cbxSlot1.setSelectedIndex( 2 ); // ti99
			}
	        
	        dto.PebDevices[1] = ui.cbxSlot1.getSelectedItem().toString();
			dto.PebDevices[2] = ui.cbxSlot2.getSelectedItem().toString();
			dto.PebDevices[3] = ui.cbxSlot3.getSelectedItem().toString();
			dto.PebDevices[4] = ui.cbxSlot4.getSelectedItem().toString();
			dto.PebDevices[5] = ui.cbxSlot5.getSelectedItem().toString();
			dto.PebDevices[6] = ui.cbxSlot6.getSelectedItem().toString();
			dto.PebDevices[7] = ui.cbxSlot7.getSelectedItem().toString();
			dto.PebDevices[8] = ui.cbxSlot8.getSelectedItem().toString();
			
			List<String> selectedCards = new ArrayList<>();
			Set<String> seenDevices = new HashSet<>();
			selectedCards.add( "peb" );
	
			for ( int i = 0; i < slots.length; i++ ) {
				JComboBox<?> slot = slots[i];
			    String sel = slot.getSelectedItem().toString();
			    
			    if ( ! "------".equals( sel )) {
			    
			        if ( ! seenDevices.add( sel ) ) {
			            JOptionPane.showMessageDialog(null,
			                "Gerät '" + sel + "' wurde mehrfach ausgewählt!",
			                "Fehler", JOptionPane.ERROR_MESSAGE);
			            slot.setSelectedIndex( 0 );
			            return null;
			        }
				    
			        switch ( sel ) {
	
			        	case "bwg":
				            dto.bwg_0 = ui.cbxBwg0.getSelectedItem().toString();
				            dto.bwg_1 = ui.cbxBwg1.getSelectedItem().toString();
				            dto.bwg_2 = ui.cbxBwg2.getSelectedItem().toString();
				            dto.bwg_3 = ui.cbxBwg3.getSelectedItem().toString();
			        		break;
	
			        	case "ccdcc":
				            dto.ccdcc_0 = ui.cbxCcdcc0.getSelectedItem().toString();
				            dto.ccdcc_1 = ui.cbxCcdcc1.getSelectedItem().toString();
				            dto.ccdcc_2 = ui.cbxCcdcc2.getSelectedItem().toString();
				            dto.ccdcc_3 = ui.cbxCcdcc3.getSelectedItem().toString();
			        		break;
	
			        	case "ccfdc":
				            dto.ccfdc_0 = ui.cbxCcfdc0.getSelectedItem().toString();
				            dto.ccfdc_1 = ui.cbxCcfdc1.getSelectedItem().toString();
				            dto.ccfdc_2 = ui.cbxCcfdc2.getSelectedItem().toString();
				            dto.ccfdc_3 = ui.cbxCcfdc3.getSelectedItem().toString();
			        		break;
	
			        	case "ddcc1":
				            dto.ddcc1_0 = ui.cbxDdcc0.getSelectedItem().toString();
				            dto.ddcc1_1 = ui.cbxDdcc0.getSelectedItem().toString();
				            dto.ddcc1_2 = ui.cbxDdcc0.getSelectedItem().toString();
				            dto.ddcc1_3 = ui.cbxDdcc0.getSelectedItem().toString();
			        		break;
	
			        	case "evpc":
			        		dto.evpc_colorbus = cbxSelNone;
			        		break;
	
				        case "hfdc":
				            dto.hfdc_f1 = ui.cbxHfdcF1.getSelectedItem().toString();
				            dto.hfdc_f2 = ui.cbxHfdcF2.getSelectedItem().toString();
				            dto.hfdc_f3 = ui.cbxHfdcF3.getSelectedItem().toString();
				            dto.hfdc_f4 = ui.cbxHfdcF4.getSelectedItem().toString();
				            dto.hfdc_h1 = ui.cbxHfdcH1.getSelectedItem().toString();
				            dto.hfdc_h2 = ui.cbxHfdcH2.getSelectedItem().toString();
				            dto.hfdc_h3 = ui.cbxHfdcH3.getSelectedItem().toString();
				            break;
				        
			        	case "ide":
				            dto.ide_0 = ui.cbxAta0.getSelectedItem().toString();
				            dto.ide_1 = ui.cbxAta1.getSelectedItem().toString();
			        		break;
	
			        	case "speechadapter":
			        		break;
	
			        	case "tifdc":
				            dto.tifdc_0 = ui.cbxTifdc0.getSelectedItem().toString();
				            dto.tifdc_1 = ui.cbxTifdc0.getSelectedItem().toString();
				            dto.tifdc_2 = ui.cbxTifdc0.getSelectedItem().toString();
			        		break;
	
			        	case "whtscsi":
				            dto.whtscsi_0 = ui.cbxScsibus0.getSelectedItem().toString();
				            dto.whtscsi_1 = ui.cbxScsibus0.getSelectedItem().toString();
				            dto.whtscsi_2 = ui.cbxScsibus0.getSelectedItem().toString();
				            dto.whtscsi_3 = ui.cbxScsibus0.getSelectedItem().toString();
				            dto.whtscsi_4 = ui.cbxScsibus0.getSelectedItem().toString();
				            dto.whtscsi_5 = ui.cbxScsibus0.getSelectedItem().toString();
				            dto.whtscsi_6 = ui.cbxScsibus0.getSelectedItem().toString();
			        		break;
	
				        default:
				        	break;
			        
			        }
		        
			        selectedCards.add( sel );
		        
			    }
		        
			}
			
			for (int i = 0; i < selectedCards.size(); i++) {
			    log.trace("selectedCard[{}] = {}", i, selectedCards.get(i));
			}
			// ctlPebDevices.updateTabs( selectedCards );
			getPebDevicesController().updateTabs(selectedCards);
	
			// -----
			
			ui.dbgEmulatorOptions.setText( MameTools.emulatorOptionsConcatenate( dto ));
			
	        // log.warn( "collectEmulatorOptions() - end dto" );
		    // log.warn( "    collectEmulatorOptions {} ms", System.currentTimeMillis() - t0 );
	        
			return dto;
		
	    } else {
	    	
	        // log.warn( "collectEmulatorOptions() - end null" );
		    // log.warn( "    collectEmulatorOptions {} ms", System.currentTimeMillis() - t0 );
	        
	    	return null;
	    	
	    }
		
	} // collectEmulatorOptions()

	// --------------------------------------------------
	
    public static void main(String[] args) {
    	
        EventQueue.invokeLater(() -> {
        	
            try {
                MainAppFrame frame = new MainAppFrame();
                frame.restoreSettings();
                frame.collectEmulatorOptions();
                frame.setVisible( true );
            } catch ( Exception e ) {
                log.error( "Fehler beim Starten der Anwendung", e );
            }
            
        });
        
    } // main

	// --------------------------------------------------
	
} // class MainAppFrame

//############################################################################
