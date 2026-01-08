package com.miriki.ti99.mame.dto;

//##################################################

public class HarddiskEntry extends MediaEntry {

    // --------------------------------------------------
    
    public HarddiskEntry( String hPath, String hName, String hExt ) {
    	
        super( hPath, hName, hExt );
        
    } // [constructor] HarddiskEntry

    // --------------------------------------------------
    
    public static HarddiskEntry none() { 
    	
    	return new HarddiskEntry("", "------", ""); 
    	
    } // none

} // class HarddiskEntry

//##################################################
