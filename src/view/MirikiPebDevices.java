package view;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	
//############################################################################

// -----------------------------------------------------------------------------
// ⚔️ Registry is the single source of truth.
// Tabs are NOT identified by their designer-given titles.
// Instead, they are mapped through the comboName → PebDevice → prettyTitle chain.
// -----------------------------------------------------------------------------
//
// ComboBox selection (comboName) → Registry lookup (PebDevice) → Controller sets tab title (prettyTitle)
//
// This ensures:
// - Internal consistency: logic uses stable keys (paramName).
// - UI flexibility: visible titles can be changed without breaking identification.
// - Designer independence: initial tab titles are irrelevant, as they are overwritten.
//
// Reminder to future maintainers:
// Do not rely on the tab's visible title for identification.
// Always consult the Registry (MirikiPebDevices) for the canonical mapping.
//-----------------------------------------------------------------------------

public class MirikiPebDevices {

	// --------------------------------------------------
	
    private static final Logger log = LoggerFactory.getLogger( MirikiPebDevices.class );
	
    // special case "no device" in the combobox
    public static final String NONE = "------";

    // database of all devices
    private static final List<PebDevice> DEVICES = List.of(
        new PebDevice("32kmem",        "32K Memory",     "TI-99 32KiB memory expansion card"),
        new PebDevice("bwg",           "BWG Fdc",        "SNUG BwG Floppy Controller"),
        new PebDevice("ccdcc",         "Cc Dcc",         "CorComp Disk Controller Card"),
        new PebDevice("ccfdc",         "Cc Fdc",         "CorComp Floppy Disk Controller Card Rev A"),
        new PebDevice("ddcc1",         "D Dcc 1",        "Myarc Disk Controller Card"),
        new PebDevice("evpc",          "EV PC",          "SNUG Enhanced Video Processor Card"),
        new PebDevice("forti",         "FORTi",          "FORTi Sound Card"),
        new PebDevice("hfdc",          "H Fdc",          "Myarc Hard and Floppy Disk Controller"),
        new PebDevice("horizon",       "Horizon",        "Horizon 4000B RAMdisk"),
        new PebDevice("hsgpl",         "Hs GPL",         "SNUG High-speed GPL card"),
        new PebDevice("ide",           "IDE",            "Nouspikel IDE interface card"),
        new PebDevice("myarcmem",      "Myarc Mem",      "Myarc Memory expansion card MEXP-1"),
        new PebDevice("pcode",         "P-Code",         "TI-99 P-Code Card"),
        new PebDevice("pgram",         "PGRAM",          "PGRAM(+) memory card"),
        new PebDevice("samsmem",       "SAMS Mem",       "SuperAMS memory expansion card"),
        new PebDevice("sidmaster",     "Sid Master",     "SID Master 99"),
        new PebDevice("speechadapter", "Speech Adapter", "TI-99 Speech synthesizer adapter card"),
        new PebDevice("tifdc",         "TI Fdc",         "TI-99 Standard DSSD Floppy Controller"),
        new PebDevice("tipi",          "TI Pi",          "TiPi card"),
        new PebDevice("tirs232",       "TI RS232",       "TI-99 RS232/PIO interface"),
        new PebDevice("usbsm",         "USB Sm",         "Nouspikel USB/Smartmedia card"),
        new PebDevice("whtscsi",       "WHT SCSI",       "Western Horizon Technologies SCSI host adapter")
    ); // DEVICES

    // BY_COMBO is the canonical lookup.
    // Do not rely on tab titles – always resolve devices via comboName → PebDevice.
    private static final Map<String, PebDevice> BY_COMBO =
        DEVICES.stream().collect(Collectors.toMap(
            d -> d.getComboName().toLowerCase(), d -> d )
        ); // BY_COMBO

	// --------------------------------------------------
	
    // public access

    public static List<PebDevice> all() {
    	
        return DEVICES;
        
    } // all()

    // --------------------------------------------------
    
    // returns the item as displayed in the combobox text and its list items
    public static PebDevice byComboName( String comboName ) {
    	
        if ( comboName == null ) return null;
        return BY_COMBO.get( comboName.toLowerCase());
        
    } // byComboName()

    // --------------------------------------------------
    
    // returns a list to be used as the combobox model
    public static String[] comboModel() {
    	
		log.debug( "comboModel()" );
		
        // for the models: NONE + all comboNames
        String[] arr = new String[DEVICES.size() + 1];
        arr[0] = NONE;
        for ( int i = 0; i < DEVICES.size(); i++ ) {
            arr[i + 1] = DEVICES.get( i ).getComboName();
        }
        return arr;
        
    } // comboModel()

    // --------------------------------------------------
    
    // returns a "pretty" title, more human readable
    public static String toPrettyTitle( String comboName ) {
    	
    	PebDevice d = byComboName( comboName );
        return d != null ? d.getPrettyTitle() : comboName;
        
    } // toPrettyTitle()

    // --------------------------------------------------
    
    // returns an all-lowercase of the device name
    public static String toParamName( String comboName ) {
    	
    	PebDevice d = byComboName( comboName );
        return d != null ? d.getParamName() : comboName.toLowerCase();
        
    } // toParamName()

    // --------------------------------------------------
    
    // returns the tab name corresponding to the device (to show / hide it)
    public static String toInternalName( String comboName ) {
    	
    	PebDevice d = byComboName( comboName );
        return d != null ? d.getInternalName() : ( "Panel_PEB_" + comboName );
        
    } // toInternalName()
    
    // --------------------------------------------------
    
} // class MirikiPebDevices

//############################################################################

/*
 * an (internal) helping data class for the peb devices class
 */

class PebDevice {

    // --------------------------------------------------
    
    private static final Logger log = LoggerFactory.getLogger( PebDevice.class );
	
    private final String comboName;     // display in text / list of the combobox, e.g. camelcase like "TiRs232"
    private final String prettyTitle;   // "pretty" title, more human readable, e.g. "TI RS232"
    private final String paramName;     // parameter for external program, all lowercase title, e.g. "tirs232"
    private final String internalName;  // name of the corresponding panel to show / hide, e.g. "Panel_PEB_TiRs232"
    private final String deviceComment; // verbose Name and / or brief description of the device as additional info

    // --------------------------------------------------
    
    // constructor - creates the entry of a device
    public PebDevice( String comboName, String prettyTitle, String cardComment ) {
    	
		log.debug( "[constructor] PebDevice()" );
		
        this.comboName = comboName;
        this.prettyTitle = prettyTitle;
        this.paramName = comboName.toLowerCase();
        this.internalName = "Panel_PEB_" + comboName;
        this.deviceComment = "Panel_PEB_" + cardComment;
        
    } // [constructor] PebDevice()

    // --------------------------------------------------
    
    public String getComboName()      { return comboName; }
    public String getPrettyTitle()    { return prettyTitle; }
    public String getParamName()      { return paramName; }
    public String getInternalName()   { return internalName; }
    public String getDeviceComment()  { return deviceComment; }

    // --------------------------------------------------
    
    @Override
    public String toString() {
    	
        // useful for debugging oder logging
        return comboName;
        
    } // toString()
	
    // --------------------------------------------------
    
} // class PebDevice

//############################################################################
