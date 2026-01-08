package com.miriki.ti99.mame.dto;

//##################################################

public class CassetteEntry extends MediaEntry {

    // --------------------------------------------------
    
    public CassetteEntry( String cPath, String cName, String cExt ) {
    	
        super( cPath, cName, cExt );
        
    } // [constructor] CassetteEntry

    // --------------------------------------------------
    
    public static CassetteEntry none() { 
    	
    	return new CassetteEntry( "", "------", "" ); 
    	
    } // none
    
} // class CassetteEntry

//##################################################
