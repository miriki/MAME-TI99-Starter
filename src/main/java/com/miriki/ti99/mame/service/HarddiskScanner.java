package com.miriki.ti99.mame.service;

import java.util.List;

import com.miriki.ti99.mame.dto.HarddiskEntry;

//##################################################

public class HarddiskScanner extends MediaScanner<HarddiskEntry> {

    // --------------------------------------------------
    
	@Override
	protected List<String> getExtensions() {
		
	    return List.of( "chd", "hd" );
	    
	} // getExtensions

    // --------------------------------------------------
    
    @Override
    protected HarddiskEntry createEntry( String hPath, String hName, String hExt ) {
    	
        return new HarddiskEntry( hPath, hName, hExt );
        
    } // createEntry

    // --------------------------------------------------
    
	@Override 
	protected HarddiskEntry createNoneEntry() { 
		
		return HarddiskEntry.none(); 
		
	} // createNoneEntry

    // --------------------------------------------------
    
}

//##################################################
