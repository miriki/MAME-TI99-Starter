package com.miriki.ti99.mame.service;

import java.util.List;

import com.miriki.ti99.mame.dto.FloppyEntry;

//##################################################

public class FloppyScanner extends MediaScanner<FloppyEntry> {

    // --------------------------------------------------
    
	@Override
	protected List<String> getExtensions() {
		
	    return List.of( "dsk", "dtk", "hfe" );
	    
	} // getExtensions

    // --------------------------------------------------
    
	@Override
	protected FloppyEntry createEntry( String fPath, String fName, String fExt ) { 
		
		return new FloppyEntry( fPath, fName, fExt ); 
		
	} // createEntry
	
    // --------------------------------------------------
    
	@Override 
	protected FloppyEntry createNoneEntry() { 
		
		return FloppyEntry.none(); 
		
	} // createNoneEntry
	     
    // --------------------------------------------------
    
} // class FloppyScanner

//##################################################
