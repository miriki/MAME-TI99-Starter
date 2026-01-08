package com.miriki.ti99.mame.ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.domain.CartridgeEntryList;
import com.miriki.ti99.mame.domain.CassetteEntryList;
import com.miriki.ti99.mame.domain.FloppyEntryList;
import com.miriki.ti99.mame.domain.HarddiskEntryList;
import com.miriki.ti99.mame.domain.MediaEntryList;
import com.miriki.ti99.mame.dto.CartridgeEntry;
import com.miriki.ti99.mame.dto.CassetteEntry;
import com.miriki.ti99.mame.dto.EmulatorOptionsDTO;
import com.miriki.ti99.mame.dto.FloppyEntry;
import com.miriki.ti99.mame.dto.HarddiskEntry;
import com.miriki.ti99.mame.dto.MediaEntry;
import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.persistence.SettingsManager;
import com.miriki.ti99.mame.persistence.SettingsPathRegistry;
import com.miriki.ti99.mame.service.CartridgeScanner;
import com.miriki.ti99.mame.service.CassetteScanner;
import com.miriki.ti99.mame.service.FloppyScanner;
import com.miriki.ti99.mame.service.HarddiskScanner;
import com.miriki.ti99.mame.tools.EmulatorStart;
import com.miriki.ti99.mame.tools.FileTools;
import com.miriki.ti99.mame.ui.mamedevices.PebDevicesController;
import com.miriki.ti99.mame.ui.util.ControlCollector;

// ############################################################################

public class MainAppFrame extends JFrame {

	// --------------------------------------------------

	private MainAppFrameBuilder builder;
	
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(MainAppFrame.class);

    private final MainAppFrameComponents ui;
    
    private PebDevicesController ctlPebDevices;
    public PebDevicesController getPebDevicesController() { return this.ctlPebDevices; }
    public void setPebDevicesController( PebDevicesController ctl ) { this.ctlPebDevices = ctl; }

    private final Map<JComboBox<?>,String> pendingSelections = new HashMap<>();
    public String getPendingSelection( JComboBox<?> cbx ) { return pendingSelections.get( cbx ); }
    public void setPendingSelection( JComboBox<?> cbx, String value ) { pendingSelections.put( cbx, value ); }
 
    private boolean eventsSuspended = false;
    public boolean getEventsSuspended() { return eventsSuspended; }
    public void setEventsSuspended( boolean evtSusp ) { this.eventsSuspended = evtSusp; }
    
    private final CartridgeEntryList cartridgeList = new CartridgeEntryList();
    private final CartridgeScanner cartridgeScanner = new CartridgeScanner();
    
    private final FloppyEntryList floppyList = new FloppyEntryList();
    private final FloppyScanner floppyScanner = new FloppyScanner();
    
    private final HarddiskEntryList harddiskList = new HarddiskEntryList();
    private final HarddiskScanner harddiskScanner = new HarddiskScanner();
    
    private final CassetteEntryList cassetteList = new CassetteEntryList();
    private final CassetteScanner cassetteScanner = new CassetteScanner();
    
	// --------------------------------------------------
    
    public MainAppFrameBuilder getBuilder() {
    	
        return builder;
        
    } // getBuilder

	// --------------------------------------------------
    
    public MainAppFrame() {
    	
        log.debug( "----- start: [constructor] MainAppFrame()" );

    	setEventsSuspended( true );

    	setTitle( "MAME TI99 Starter" );
        setBounds( 100, 100, 1200, 675 );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        ui = new MainAppFrameComponents();
        setContentPane( ui.getContentPane() );
        
        this.builder = new MainAppFrameBuilder( this, ui );
        builder.initGUI();
        builder.postGUI();

        log.debug( "----- end: [constructor] MainAppFrame()" );

    } // [constructor] MainAppFrame
    
	// --------------------------------------------------
    
    public void updateFrameTitle() {
    	
        log.debug( "----- start: updateFrameTitle()" );
    	
        String baseTitle = "MAME TI99 Starter";

        // Aktuellen Settings-Pfad holen
        Path current = SettingsPathRegistry.getCurrent();

        String configName = FileTools.prettyName( current );

        // Titel zusammensetzen
        setTitle( baseTitle + "   -   " + configName );

        log.debug( "----- end: updateFrameTitle()" );
    	
    } // updateFrameTitle
    
	// --------------------------------------------------
    
    public void restoreSettings() {
        
        log.debug( "----- start: restoreSettings()" );
    	
    	Map<String, JComponent> controls = ControlCollector.collectControls( this );

        try {
        	
        	Properties props = SettingsManager.restoreFromCurrent( controls );
        	
        	String savedLocale = props.getProperty("locale");
        	if (savedLocale != null && !savedLocale.isBlank()) {
        	    I18n.setLocale(Locale.forLanguageTag(savedLocale));
        	} else {
        	    // automatische Erkennung
        	    Locale sys = Locale.getDefault();
        	    if (sys.getLanguage().equals("de")) {
        	        I18n.setLocale(I18n.LOCALE_DE_DE);
        	    } else if (sys.getLanguage().equals("en")) {
        	        I18n.setLocale(I18n.LOCALE_EN_GB);
        	    } else {
        	        I18n.setLocale(I18n.LOCALE_DE_DE); // fallback
        	    }
        	}
        	builder.refreshUI();
        	
	    	// TODO: Asmusr reported: "As soon as you open the Settings menu your selection is reset for Flop1, f2, f3, and cartridge."
        	// fixed: ActionEvents fired while ActionEvents are already running - this deadloop should have produced this behaviour
	    	
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
        	
        	updateFrameTitle();
        	
        } catch ( IOException ex ) {
        	
            log.warn("no saved setting found for '{}'.", SettingsPathRegistry.getCurrent(), ex);
            
        }
        
        log.debug( "----- end: restoreSettings()" );
    	
    } // restoreSettings

	// --------------------------------------------------
    
    public void saveSettings() {

        log.debug( "----- start: saveSettings()" );
    	
    	Map<String, JComponent> controls = ControlCollector.collectControls( this );
        Map<String, Component> tabMap = ctlPebDevices != null ? ctlPebDevices.getTabMap() : Map.of();

        SettingsManager.saveToCurrent( controls, tabMap );

        log.debug( "----- end: saveSettings()" );
    	
    } // saveSettings

	// --------------------------------------------------
	
    private void updateComboBoxPreserve( JComboBox<String> cbx, List<String> cbxItems, String defaultSelection ) {
	    
        log.debug( "----- start: updateComboBoxPreserve( '{}', List:String[{}]', '{}' )", cbx.getName(), cbxItems.size(), defaultSelection );
    	
    	String previousSelection = (String) cbx.getSelectedItem();
	    
	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // NONE ganz oben
	    model.addElement(defaultSelection);

	    // dann alle echten Items
	    for (String s : cbxItems) {
	        model.addElement(s);
	    }

	    cbx.setModel(model);
	    
	    String pendingSelection = getPendingSelection( cbx );
	    String newSelection;
	    
	    if ( pendingSelection != null) {
	        if ( cbxItems.contains( pendingSelection )) {
	            newSelection = pendingSelection;
	        } else {
	            newSelection = defaultSelection;
	        }
	        setPendingSelection( cbx, null );
	    }
	    else if ( previousSelection != null && cbxItems.contains( previousSelection )) {
	        newSelection = previousSelection;
	    } else {
            newSelection = defaultSelection;
	    }

	    // Nur setzen, wenn sich der Wert wirklich geändert hat
	    String current = (String) cbx.getSelectedItem();
	    // log.trace( "Restoring {} ---> '{}'", cbx.getName(), current);
	    if ( ! Objects.equals( current, newSelection )) {
	        cbx.setSelectedItem( newSelection );
	    }
	    // log.trace("After setSelectedItem: {}", cbx.getSelectedItem());
	    
        log.debug( "----- end: updateComboBoxPreserve()" );
    	
    } // updateComboBoxPreserve
    
	// --------------------------------------------------

    private void updateCartridgeModel( String basePath, String cartPath, String cbxSelNone ) {
    	
        log.debug( "----- start: updateCartridgeModel()" );
        
        List<CartridgeEntry> entries = cartridgeScanner.scan(basePath, cartPath);
        cartridgeList.set(entries);
        // updateComboBoxPreserveCartridge( ui.cbxCartridge, cartridgeList.getAll(), CartridgeEntry.none() );
        updateComboBoxPreserve( ui.cbxCartridge, cartridgeList.getDisplayNames(), "------" );

        log.debug( "----- end: updateCartridgeModel()" );
        
    } // updateCartridgeModel
    
	// --------------------------------------------------

    private void updateFddModel( String basePath, String fddPath, String cbxSelNone ) {
    	
        log.debug( "----- start: updateFddModel( '{}', '{}', '{}' )", basePath, fddPath, cbxSelNone );

    	// List<String> modelS = FileTools.scanDirectories( basePath, fddPath, "dsk" );
	    // updateComboBoxPreserve( ui.cbxFlop1, modelS, cbxSelNone );
	    // updateComboBoxPreserve( ui.cbxFlop2, modelS, cbxSelNone );
	    // updateComboBoxPreserve( ui.cbxFlop3, modelS, cbxSelNone );
	    // updateComboBoxPreserve( ui.cbxFlop4, modelS, cbxSelNone );
    	List<FloppyEntry> entries = floppyScanner.scan(basePath, fddPath);
    	floppyList.set(entries);
    	updateComboBoxPreserve( ui.cbxFlop1, floppyList.getDisplayNames(), "------" );
    	updateComboBoxPreserve( ui.cbxFlop2, floppyList.getDisplayNames(), "------" );
    	updateComboBoxPreserve( ui.cbxFlop3, floppyList.getDisplayNames(), "------" );
    	updateComboBoxPreserve( ui.cbxFlop4, floppyList.getDisplayNames(), "------" );

        log.debug( "----- end: updateFddModel()" );
        
    } // updateFddModel
    
	// --------------------------------------------------

    private void updateHddModel( String basePath, String hddPath, String cbxSelNone ) {
    	
        log.debug( "----- start: updateHddModel( '{}', '{}', '{}' )", basePath, hddPath, cbxSelNone );
    	
	    // List<String> modelS = FileTools.scanDirectories( basePath, hddPath, "chd" );
	    // updateComboBoxPreserve( ui.cbxHard1, modelS, cbxSelNone );
	    // updateComboBoxPreserve( ui.cbxHard2, modelS, cbxSelNone );
	    // updateComboBoxPreserve( ui.cbxHard3, modelS, cbxSelNone );
    	List<HarddiskEntry> entries = harddiskScanner.scan(basePath, hddPath);
    	harddiskList.set(entries);
    	updateComboBoxPreserve( ui.cbxHard1, harddiskList.getDisplayNames(), "------" );
    	updateComboBoxPreserve( ui.cbxHard2, harddiskList.getDisplayNames(), "------" );
    	updateComboBoxPreserve( ui.cbxHard3, harddiskList.getDisplayNames(), "------" );

        log.debug( "----- end: updateHddModel()" );
        
    } // updateHddModel
    
	// --------------------------------------------------

    private void updateCassModel( String basePath, String cassPath, String cbxSelNone ) {
    	
        log.debug( "----- start: updateCassModel( '{}', '{}', '{}' )", basePath, cassPath, cbxSelNone );
    	
	    // List<String> modelS = FileTools.scanDirectories( basePath, cassPath, "wav" );
	    // updateComboBoxPreserve( ui.cbxCass1, modelS, cbxSelNone );
	    // updateComboBoxPreserve( ui.cbxCass2, modelS, cbxSelNone );
    	List<CassetteEntry> entries = cassetteScanner.scan(basePath, cassPath);
    	cassetteList.set(entries);
    	updateComboBoxPreserve( ui.cbxCass1, cassetteList.getDisplayNames(), "------" );
    	updateComboBoxPreserve( ui.cbxCass2, cassetteList.getDisplayNames(), "------" );

        log.debug( "----- end: updateCassModel()" );
        
    } // updateCassModel
    
	// --------------------------------------------------

    private void updateUIFromFilesystem( String basePath, String cartPath, String fddPath, String hddPath, String cassPath ) {
    	
    	// final CartridgeEntry cbxSelNoneCart = MainAppFrameComponents.CARTRIDGE_NONE;
    	// final String cbxSelNoneCart = UiConstants.CBX_SEL_NONE;
    	final String cbxSelNone = UiConstants.CBX_SEL_NONE; // "------";

        // ----- Cartridge -----
        updateCartridgeModel( basePath, cartPath, cbxSelNone );
        
        // ----- Floppy 1..4 -----
        updateFddModel( basePath, fddPath, cbxSelNone );

        // ----- Harddisk 1..3 -----
        updateHddModel( basePath, hddPath, cbxSelNone );

        // ----- Cassette 1..2 -----
        updateCassModel( basePath, cassPath, cbxSelNone );

    } // updateUIFromFilesystem
    
	// --------------------------------------------------

    private Path resolveMediaPath( MediaEntryList<?> list, String displayName ) {
    	
        if ( displayName == null || displayName.equals( UiConstants.CBX_SEL_NONE )) {
            return null;
        }

        MediaEntry entry = list.findByDisplayName( displayName );
        
        return ( entry != null ) ? entry.getFullPath() : null;
        
    } // resolveMediaPath

	// --------------------------------------------------

    private void setSlot( JComboBox<?> cbx, int index, boolean enabled ) {
    	
        cbx.setSelectedIndex( index );
        cbx.setEnabled( enabled );
        
    } // setSlot

	// --------------------------------------------------

    private void removeEvpcFromSlots( JComboBox<?>... slots ) {
    	
        for ( JComboBox<?> slot : slots ) {
        	
            if ( "evpc".equals( slot.getSelectedItem().toString() )) {
                slot.setSelectedIndex( 0 );
            }
            
        }
        
    } // removeEvpcFromSlots

	// --------------------------------------------------

    private void applyMachineRules( String machine, JComboBox<?> cbxIoPort, JComboBox<?> cbxSlot1, JComboBox<?> cbxSlot2, JComboBox<?>... otherSlots ) {

    	log.debug( "applyMachineRules( '{}', '{}', '{}', '{}' )", machine, cbxIoPort.getSelectedItem().toString(), cbxSlot1.getSelectedItem().toString(), cbxSlot2.getSelectedItem().toString() );
    	
        switch ( machine ) {

            case "TI99_4ev":
            	log.trace( "case 'EVPC' - cbxIoPort->'peb' locked, cbxSlot1->'TI99 console' locked, cbxSlot2->'evpc' locked" );
                setSlot( cbxIoPort, 1, false );   // peb
                setSlot( cbxSlot1, 2, false );    // TI99 console
                setSlot( cbxSlot2, 6, false );    // EVPC
                break;

            case "geneve":
            case "genmod":
            	log.trace( "case 'Geneve' - cbxIoPort->'peb' locked, cbxSlot1->'Geneve card' locked, cbxSlot2 enabled" );
                setSlot( cbxIoPort, 1, false );   // peb
                setSlot( cbxSlot1, 3, false );    // Geneve card
                cbxSlot2.setEnabled( true );
                removeEvpcFromSlots( otherSlots );
                break;

            default:
                // Standardfall: alles frei
            	log.trace( "case 'other' - cbxIoPort enabled, cbxSlot2 enabled" );
                cbxIoPort.setEnabled( true );
                setSlot( cbxSlot1, 2, false );    // TI99 console
                cbxSlot2.setEnabled( true );
                removeEvpcFromSlots( otherSlots );
                break;
                
        }
        
    } // applyMachineRules

	// --------------------------------------------------

    private void applyIoPortRules( JComboBox<?> cbxIoPort, JComboBox<?> cbxSlot1 ) {
    	
        if ( cbxIoPort.getSelectedIndex() == 0 ) { // "------"
        	
            setSlot( cbxSlot1, 1, false ); // "not connected"
            
        }
        
    } // applyIoPortRules

	// --------------------------------------------------

    private EmulatorOptionsDTO buildDTOFromUI() {
    	
	    final String cbxSelNone = UiConstants.CBX_SEL_NONE;

	    EmulatorOptionsDTO result = new EmulatorOptionsDTO();

		JComboBox<?>[] slots = { ui.cbxSlot2, ui.cbxSlot3, ui.cbxSlot4, ui.cbxSlot5, ui.cbxSlot6, ui.cbxSlot7, ui.cbxSlot8 };
		
	    // ---------- transfer ui controls into dto ----------

	    // Basisverzeichnisse (nur UI-Strings, keine Pfadlogik!)
	    result.mame_WorkingPath = ui.txtWorkingDir.getText().trim();
	    Path workingPath = Paths.get( result.mame_WorkingPath );
	    result.mame_Executable  = ui.txtEmulator.getText().trim();
	    result.mame_RomPath     = ui.txtRomPath.getText().trim();
	    result.mame_CartPath    = ui.txtCartPath.getText().trim();
	    result.mame_DskPath     = ui.txtFddPath.getText().trim();
	    result.mame_WdsPath     = ui.txtHddPath.getText().trim();
	    result.mame_CsPath      = ui.txtCassPath.getText().trim();

	    // ----- main -----

	    result.mame_Machine  = ui.cbxMachine.getSelectedItem().toString();
	    result.mame_GromPort = ui.cbxGromPort.getSelectedItem().toString();
	    result.mame_JoyPort  = ui.cbxJoystick.getSelectedItem().toString();

		this.setEventsSuspended( true );
		applyMachineRules(
		        result.mame_Machine,
		        ui.cbxIoPort,
		        ui.cbxSlot1,
		        ui.cbxSlot2,
		        slots
		);
		applyIoPortRules(ui.cbxIoPort, ui.cbxSlot1);
		this.setEventsSuspended( true );

	    result.mame_IoPort   = ui.cbxIoPort.getSelectedItem().toString();

	    // ----- cartridge -----

	    result.cartDisplayName = ui.cbxCartridge.getSelectedItem().toString();
	    result.cartEntry = cartridgeList.findByDisplayName( result.cartDisplayName );
	    result.cartPathP = resolveMediaPath( cartridgeList, result.cartDisplayName ); // null bei ZIP

	    // ----- floppy -----

	    String dsk1 = ui.cbxFlop1.getSelectedItem().toString();
	    String dsk2 = ui.cbxFlop2.getSelectedItem().toString();
	    String dsk3 = ui.cbxFlop3.getSelectedItem().toString();
	    String dsk4 = ui.cbxFlop4.getSelectedItem().toString();

	    result.mame_DSK1 = dsk1;
	    result.mame_DSK2 = dsk2;
	    result.mame_DSK3 = dsk3;
	    result.mame_DSK4 = dsk4;

	    result.fddPathP1 = resolveMediaPath( floppyList, dsk1 );
	    result.fddPathP2 = resolveMediaPath( floppyList, dsk2 );
	    result.fddPathP3 = resolveMediaPath( floppyList, dsk3 );
	    result.fddPathP4 = resolveMediaPath( floppyList, dsk4 );
	    result.fddPathRel1 = floppyList.resolveMediaRelativePath( dsk1, workingPath );
	    result.fddPathRel2 = floppyList.resolveMediaRelativePath( dsk2, workingPath );
	    result.fddPathRel3 = floppyList.resolveMediaRelativePath( dsk3, workingPath );
	    result.fddPathRel4 = floppyList.resolveMediaRelativePath( dsk4, workingPath );
	    
	    // ----- harddisk -----

	    String wds1 = ui.cbxHard1.getSelectedItem().toString();
	    String wds2 = ui.cbxHard2.getSelectedItem().toString();
	    String wds3 = ui.cbxHard3.getSelectedItem().toString();

	    result.mame_WDS1 = wds1;
	    result.mame_WDS2 = wds2;
	    result.mame_WDS3 = wds3;

	    result.hddPathP1 = resolveMediaPath( harddiskList, wds1 );
	    result.hddPathP2 = resolveMediaPath( harddiskList, wds2 );
	    result.hddPathP3 = resolveMediaPath( harddiskList, wds3 );
	    result.hddPathRel1 = harddiskList.resolveMediaRelativePath( wds1, workingPath );
	    result.hddPathRel2 = harddiskList.resolveMediaRelativePath( wds2, workingPath );
	    result.hddPathRel3 = harddiskList.resolveMediaRelativePath( wds3, workingPath );

	    // ----- cassette -----

	    String cass1 = ui.cbxCass1.getSelectedItem().toString();
	    String cass2 = ui.cbxCass2.getSelectedItem().toString();

	    result.mame_CS1 = cass1;
	    result.mame_CS2 = cass2;

	    result.cassPathP1 = resolveMediaPath( cassetteList, cass1 );
	    result.cassPathP2 = resolveMediaPath( cassetteList, cass2 );
	    result.cassPathRel1 = cassetteList.resolveMediaRelativePath( cass1, workingPath );
	    result.cassPathRel2 = cassetteList.resolveMediaRelativePath( cass2, workingPath );

	    // ----- peb slots -----
	    
		List<String> selectedCards = new ArrayList<>();
		Set<String> seenDevices = new HashSet<>();

		selectedCards.add( "peb" );

		// -----
		
        result.PebDevices[1] = ui.cbxSlot1.getSelectedItem().toString();
        result.PebDevices[2] = ui.cbxSlot2.getSelectedItem().toString();
        result.PebDevices[3] = ui.cbxSlot3.getSelectedItem().toString();
        result.PebDevices[4] = ui.cbxSlot4.getSelectedItem().toString();
        result.PebDevices[5] = ui.cbxSlot5.getSelectedItem().toString();
        result.PebDevices[6] = ui.cbxSlot6.getSelectedItem().toString();
        result.PebDevices[7] = ui.cbxSlot7.getSelectedItem().toString();
        result.PebDevices[8] = ui.cbxSlot8.getSelectedItem().toString();
		
		for ( int i = 0; i < slots.length; i++ ) {
			JComboBox<?> slot = slots[i];
		    String selS = slot.getSelectedItem().toString();
		    
		    if ( ! cbxSelNone.equals( selS )) {
		    
		        if ( ! seenDevices.add( selS ) ) {
		            // JOptionPane.showMessageDialog(null,
		            //     "Gerät '" + sel + "' wurde mehrfach ausgewählt!",
		            //     "Fehler", JOptionPane.ERROR_MESSAGE);
		        	log.warn( "Device '{}' was selected more than once, thus removed!", selS );
		            slot.setSelectedIndex( 0 );
		            // return null;
		        }
			    
		        switch ( selS ) {

		        	case "bwg":
		        		result.bwg_0 = ui.cbxBwg0.getSelectedItem().toString();
		        		result.bwg_1 = ui.cbxBwg1.getSelectedItem().toString();
		        		result.bwg_2 = ui.cbxBwg2.getSelectedItem().toString();
		        		result.bwg_3 = ui.cbxBwg3.getSelectedItem().toString();
		        		break;

		        	case "ccdcc":
		        		result.ccdcc_0 = ui.cbxCcdcc0.getSelectedItem().toString();
		        		result.ccdcc_1 = ui.cbxCcdcc1.getSelectedItem().toString();
		        		result.ccdcc_2 = ui.cbxCcdcc2.getSelectedItem().toString();
		        		result.ccdcc_3 = ui.cbxCcdcc3.getSelectedItem().toString();
		        		break;

		        	case "ccfdc":
		        		result.ccfdc_0 = ui.cbxCcfdc0.getSelectedItem().toString();
		        		result.ccfdc_1 = ui.cbxCcfdc1.getSelectedItem().toString();
		        		result.ccfdc_2 = ui.cbxCcfdc2.getSelectedItem().toString();
		        		result.ccfdc_3 = ui.cbxCcfdc3.getSelectedItem().toString();
		        		break;

		        	case "ddcc1":
		        		result.ddcc1_0 = ui.cbxDdcc0.getSelectedItem().toString();
		        		result.ddcc1_1 = ui.cbxDdcc0.getSelectedItem().toString();
		        		result.ddcc1_2 = ui.cbxDdcc0.getSelectedItem().toString();
		        		result.ddcc1_3 = ui.cbxDdcc0.getSelectedItem().toString();
		        		break;

		        	case "evpc":
		        		result.evpc_colorbus = cbxSelNone;
		        		break;

			        case "hfdc":
			        	result.hfdc_f1 = ui.cbxHfdcF1.getSelectedItem().toString();
			        	result.hfdc_f2 = ui.cbxHfdcF2.getSelectedItem().toString();
			        	result.hfdc_f3 = ui.cbxHfdcF3.getSelectedItem().toString();
			        	result.hfdc_f4 = ui.cbxHfdcF4.getSelectedItem().toString();
			        	result.hfdc_h1 = ui.cbxHfdcH1.getSelectedItem().toString();
			        	result.hfdc_h2 = ui.cbxHfdcH2.getSelectedItem().toString();
			        	result.hfdc_h3 = ui.cbxHfdcH3.getSelectedItem().toString();
			            break;
			        
		        	case "ide":
		        		result.ide_0 = ui.cbxAta0.getSelectedItem().toString();
		        		result.ide_1 = ui.cbxAta1.getSelectedItem().toString();
		        		break;

		        	case "speechadapter":
		        		break;

		        	case "tifdc":
		        		result.tifdc_0 = ui.cbxTifdc0.getSelectedItem().toString();
		        		result.tifdc_1 = ui.cbxTifdc0.getSelectedItem().toString();
		        		result.tifdc_2 = ui.cbxTifdc0.getSelectedItem().toString();
		        		break;

		        	case "whtscsi":
		        		result.whtscsi_0 = ui.cbxScsibus0.getSelectedItem().toString();
		        		result.whtscsi_1 = ui.cbxScsibus0.getSelectedItem().toString();
		        		result.whtscsi_2 = ui.cbxScsibus0.getSelectedItem().toString();
		        		result.whtscsi_3 = ui.cbxScsibus0.getSelectedItem().toString();
		        		result.whtscsi_4 = ui.cbxScsibus0.getSelectedItem().toString();
		        		result.whtscsi_5 = ui.cbxScsibus0.getSelectedItem().toString();
		        		result.whtscsi_6 = ui.cbxScsibus0.getSelectedItem().toString();
		        		break;

			        default:
			        	break;
		        
		        }
	        
		        selectedCards.add( selS );
	        
		    }
	        
		}

		for ( int i = 0; i < selectedCards.size(); i++ ) {
		    log.trace( "selectedCard[{}] = {}", i, selectedCards.get( i ));
		}
		
	    result.mame_AddOpt = ui.txtAddOpt.getText().trim();

		// TODO: Asmusr reported: "When you have selected a disk controller, e.g. hfdc, and go to the Hfdc tab to set up the drives, when you click inside the tab it jumps back to the Peb tab." --> save active tab and restore after updateTabs() finished
	    // fixed: rebuilt (after deleting all tabPages) lead to tabIndex 0 - event occurs on any change in the ui controls
		
		getPebDevicesController().updateTabs( selectedCards );

		ui.dbgEmulatorOptions.setText( EmulatorStart.emulatorOptionsConcatenate( result ));
        
        return result;
        
    } // buildDTOFromUI
    
	// --------------------------------------------------

    public EmulatorOptionsDTO collectEmulatorOptions() {

        log.debug( "----- start: collectEmulatorOptions()" );
        
        if ( ! getEventsSuspended() ) {
        
	        setEventsSuspended(true);
	        
	        String basePath = ui.txtWorkingDir.getText().trim();
	        String cartPath = ui.txtCartPath.getText().trim();
	        String fddPath = ui.txtFddPath.getText().trim();
	        String hddPath = ui.txtHddPath.getText().trim();
	        String cassPath = ui.txtCassPath.getText().trim();
	
	    	EmulatorOptionsDTO result = null;
	    	
	        updateUIFromFilesystem( basePath, cartPath, fddPath, hddPath, cassPath );
	
	        result = buildDTOFromUI();
	        
			setEventsSuspended(false);
	        
	        log.debug( "----- end: collectEmulatorOptions() --> {dto}" );
        
	        return result;
	    	
        } else {
        	
        	log.trace( "##### collectEmulatorOptions skipped - Events are currently disabled" );
	        log.debug( "----- skipped: collectEmulatorOptions() --> null" );
	        
	        return null;
	        
        }
        
    } // collectEmulatorOptions
    
    // --------------------------------------------------
	
    public static void main(String[] args) {
    	
        log.debug( "----- start: main( String[{}] )", args.length );
        
        EventQueue.invokeLater(() -> {
        	
            try {
                MainAppFrame frame = new MainAppFrame();
                frame.restoreSettings();
                frame.setVisible( true );
                SwingUtilities.invokeLater(() -> { 
                	frame.setEventsSuspended(false); 
                	frame.collectEmulatorOptions(); 
                });
                
            } catch ( Exception e ) {
                log.error( "Error starting the application", e );
                
            }
            
        });
        
        log.debug( "----- end: main()" );
        
    } // main

	// --------------------------------------------------
	
} // class MainAppFrame

//############################################################################
