package com.miriki.ti99.mame.ui.mamedevices;

import java.util.*;
import java.util.stream.Collectors;

import com.miriki.ti99.mame.ui.UiConstants;
	
public class SideportDevices {

    private static final List<SideportDevice> DEVICES = List.of(
        new SideportDevice("peb",       "PEB",      "Peripheral Expansion Box"),
        new SideportDevice("speechsyn", "speech",   "TI-99 Speech Synthesizer"),
        new SideportDevice("splitter",  "Splitter", "TI-99 I/O Port Splitter"),
        new SideportDevice("arcturus",  "Arcturus", "Arcturus sidecar cartridge")
    );

    private static final Map<String, SideportDevice> BY_COMBO =
        DEVICES.stream().collect(Collectors.toMap(
            d -> d.getComboName().toLowerCase(), d -> d )
        );

    public static List<SideportDevice> all() {
    	
        return DEVICES;
        
    }

    public static SideportDevice byComboName( String comboName ) {
    	
        if ( comboName == null ) return null;
        return BY_COMBO.get( comboName.toLowerCase() );
        
    }

    public static String[] comboModel() {
    	
        String[] arr = new String[DEVICES.size() + 1];
        arr[0] = UiConstants.CBX_SEL_NONE;
        for ( int i = 0; i < DEVICES.size(); i++ ) {
            arr[i + 1] = DEVICES.get( i ).getComboName();
        }
        return arr;
        
    }

    public static String toPrettyTitle( String comboName ) {
    	
        SideportDevice d = byComboName( comboName );
        return d != null ? d.getPrettyTitle() : comboName;
        
    }

    public static String toParamName( String comboName ) {
    	
        SideportDevice d = byComboName( comboName );
        return d != null ? d.getParamName() : comboName.toLowerCase();
        
    }

    public static String toInternalName( String comboName ) {
    	
        SideportDevice d = byComboName( comboName );
        return d != null ? d.getInternalName() : ("Panel_PEB_" + comboName);
        
    }
    
}

class SideportDevice {

    private final String comboName;
    private final String prettyTitle;
    private final String paramName;
    private final String internalName;
    private final String deviceComment;

    public SideportDevice( String comboName, String prettyTitle, String cardComment ) {
    	
        this.comboName = comboName;
        this.prettyTitle = prettyTitle;
        this.paramName = comboName.toLowerCase();
        this.internalName = "Panel_PEB_" + comboName;
        this.deviceComment = cardComment;
        
    }

    public String getComboName()      { return comboName; }
    public String getPrettyTitle()    { return prettyTitle; }
    public String getParamName()      { return paramName; }
    public String getInternalName()   { return internalName; }
    public String getDeviceComment()  { return deviceComment; }

    @Override
    public String toString() {
    	
        return comboName;
        
    }
	
}
