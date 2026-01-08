package com.miriki.ti99.mame.dto;

//##################################################

public class FloppyEntry extends MediaEntry {

    // --------------------------------------------------
    
    public FloppyEntry( String fPath, String fName, String fExt ) {
    	
        super( fPath, fName, fExt );
        
    } // [constructor] FloppyEntry

    // --------------------------------------------------
    
    public static FloppyEntry none() { 
    	
    	return new FloppyEntry( "", "------", "" ); 
    	
    } // none
    
} // class FloppyEntry

//##################################################
