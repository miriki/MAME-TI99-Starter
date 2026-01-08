package com.miriki.ti99.mame.dto;

import java.nio.file.Path;

import com.miriki.ti99.mame.domain.MediaExtensions;

//##################################################

public class CartridgeEntry extends MediaEntry {

    // --------------------------------------------------
    
    public CartridgeEntry( String bPath, String bName, String bExt ) {
    	
        super( bPath, bName, bExt );
        
    } // [constructor] CartridgeEntry

    // --------------------------------------------------
    
    /*
    @Override
    public Path getFullPath() {
    	
        // ZIP-Cartridges bekommen KEINEN Pfad
        if ( "zip".equalsIgnoreCase( getMediaExt() )) {
            return null;
        }

        // RPK-Cartridges bekommen den vollständigen Pfad
        return super.getFullPath();
        
    }
    */
    
    @Override
    
    public Path getFullPath() {

        if ( MediaExtensions.isPathless( getMediaExt() )) {
            return null;
        }

        return super.getFullPath();
        
    } // getFullPath

    // --------------------------------------------------
    
    public boolean isNone() {
    	
        // return mediaFileName != null && mediaFileName.equals("------") && (mediaFileExt == null || mediaFileExt.isEmpty());
        return "------".equals( mediaName );
        
    } // isNone

    // --------------------------------------------------
    
    public static CartridgeEntry none() {
    	
        return new CartridgeEntry( "", "------", "" );
        
    } // none

} // class CartridgeEntry

//##################################################
