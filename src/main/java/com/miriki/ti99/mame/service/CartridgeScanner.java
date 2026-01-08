package com.miriki.ti99.mame.service;

import java.util.List;

import com.miriki.ti99.mame.dto.CartridgeEntry;

//##################################################

public class CartridgeScanner extends MediaScanner<CartridgeEntry> {

    // --------------------------------------------------
    
    @Override
    protected List<String> getExtensions() {
    	
        return List.of( "zip", "rpk" );
        
    } // getExtensions

    // --------------------------------------------------
    
    @Override
    protected CartridgeEntry createEntry( String path, String name, String ext ) {
    	
        return new CartridgeEntry( path, name, ext );
        
    } // createEntry

    // --------------------------------------------------
    
    @Override
    protected CartridgeEntry createNoneEntry() {
    	
        return CartridgeEntry.none();
        
    } // createNoneEntry
    
    // --------------------------------------------------
    
} // class CartridgeScanner

//##################################################
