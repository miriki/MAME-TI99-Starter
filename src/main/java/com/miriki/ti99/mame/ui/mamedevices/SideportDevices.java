package com.miriki.ti99.mame.ui.mamedevices;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.ui.UiConstants;
	
//############################################################################

// -----------------------------------------------------------------------------
// ⚔️ Registry is the single source of truth.
// Tabs are NOT identified by their designer-given titles.
// Instead, they are mapped through the comboName → SideportDevice → prettyTitle chain.
// -----------------------------------------------------------------------------
//
// ComboBox selection (comboName) → Registry lookup (SideportDevice) → Controller sets tab title (prettyTitle)
//
// This ensures:
// - Internal consistency: logic uses stable keys (paramName).
// - UI flexibility: visible titles can be changed without breaking identification.
// - Designer independence: initial tab titles are irrelevant, as they are overwritten.
//
// Reminder to future maintainers:
// Do not rely on the tab's visible title for identification.
// Always consult the Registry (MirikiSideportDevices) for the canonical mapping.
//-----------------------------------------------------------------------------

public class SideportDevices {

    // --------------------------------------------------
    
    private static final Logger log = LoggerFactory.getLogger(SideportDevices.class);
	
    // database of all devices
    private static final List<SideportDevice> DEVICES = List.of(
        new SideportDevice("peb",       "PEB",      "Peripheral Expansion Box"),
        new SideportDevice("speechsyn", "speech",   "TI-99 Speech Synthesizer"),
        new SideportDevice("splitter",  "Splitter", "TI-99 I/O Port Splitter"),
        new SideportDevice("arcturus",  "Arcturus", "Arcturus sidecar cartridge")
    ); // DEVICES

    // BY_COMBO is the canonical lookup.
    // Do not rely on tab titles – always resolve devices via comboName → SideportDevice.
    private static final Map<String, SideportDevice> BY_COMBO =
        DEVICES.stream().collect(Collectors.toMap(
            d -> d.getComboName().toLowerCase(), d -> d )
        ); // BY_COMBO

    // --------------------------------------------------
    
    // public access

    public static List<SideportDevice> all() {
    	
        return DEVICES;
        
    } // all

    // --------------------------------------------------
    
    // returns the item as displayed in the combobox text and its list items
    public static SideportDevice byComboName( String comboName ) {
    	
        if ( comboName == null ) return null;
        return BY_COMBO.get( comboName.toLowerCase() );
        
    } // byComboName

    // --------------------------------------------------
    
    // returns a list to be used as the combobox model
    public static String[] comboModel() {
    	
		log.debug( "comboModel()" );
		
        // for the models: NONE + all comboNames
        String[] arr = new String[DEVICES.size() + 1];
        arr[0] = UiConstants.CBX_SEL_NONE;
        for ( int i = 0; i < DEVICES.size(); i++ ) {
            arr[i + 1] = DEVICES.get( i ).getComboName();
        }
        return arr;
        
    } // comboModel

    // --------------------------------------------------
    
    // returns a "pretty" title, more human readable
    public static String toPrettyTitle( String comboName ) {
    	
        SideportDevice d = byComboName( comboName );
        return d != null ? d.getPrettyTitle() : comboName;
        
    } // toPrettyTitle

    // --------------------------------------------------
    
    // returns an all-lowercase of the device name
    public static String toParamName( String comboName ) {
    	
        SideportDevice d = byComboName( comboName );
        return d != null ? d.getParamName() : comboName.toLowerCase();
        
    } // toParamName

    // --------------------------------------------------
    
    // returns the tab name corresponding to the device (to show / hide it)
    public static String toInternalName( String comboName ) {
    	
        SideportDevice d = byComboName( comboName );
        return d != null ? d.getInternalName() : ("Panel_PEB_" + comboName);
        
    } // toInternalName
    
    // --------------------------------------------------
    
} // class MirikiSideportDevices

//############################################################################

/*
 * an (internal) helping data class for the sideport devices class
 */

class SideportDevice {

    // --------------------------------------------------
    
    private static final Logger log = LoggerFactory.getLogger(SideportDevice.class);
	
    private final String comboName;     // display in text / list of the combobox, e.g. camelcase like "TiRs232"
    private final String prettyTitle;   // "pretty" title, more human readable, e.g. "TI RS232"
    private final String paramName;     // parameter for external program, all lowercase title, e.g. "tirs232"
    private final String internalName;  // name of the corresponding panel to show / hide, e.g. "Panel_PEB_TiRs232"
    private final String deviceComment; // verbose Name and / or brief description of the device as additional info

    // --------------------------------------------------
    
    // constructor - creates the entry of a device
    public SideportDevice( String comboName, String prettyTitle, String cardComment ) {
    	
		log.debug( "----- start: [constructor] SideportDevice()" );
		
        this.comboName = comboName;
        this.prettyTitle = prettyTitle;
        this.paramName = comboName.toLowerCase();
        this.internalName = "Panel_PEB_" + comboName;
        this.deviceComment = cardComment;
        
		log.debug( "----- end: [constructor] SideportDevice()" );
		
    } // SideportDevice - constructor

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
        
    } // toString
	
    // --------------------------------------------------
    
} // class SideportDevice

//############################################################################
