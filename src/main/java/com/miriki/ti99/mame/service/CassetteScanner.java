package com.miriki.ti99.mame.service;

import java.util.List;

import com.miriki.ti99.mame.dto.CassetteEntry;

//##################################################

public class CassetteScanner extends MediaScanner<CassetteEntry> {

    // --------------------------------------------------
    
	@Override
	protected List<String> getExtensions() {
		
	    return List.of( "wav", "flac" );
	    
    } // getExtensions

    // --------------------------------------------------
    
    @Override
    protected CassetteEntry createEntry( String cPath, String cName, String cExt ) {
    	
        return new CassetteEntry( cPath, cName, cExt );
        
    } // createEntry

    // --------------------------------------------------
    
	@Override 
	protected CassetteEntry createNoneEntry() { 
		
		return CassetteEntry.none(); 
		
	} // createNoneEntry
		 
    // --------------------------------------------------
    
} // class CassetteScanner

//##################################################
