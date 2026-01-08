package com.miriki.ti99.mame.domain;

import java.util.Set;

// ##################################################

public final class MediaExtensions {

    // --------------------------------------------------
    
    private static final Set<String> PATHLESS_EXTENSIONS = Set.of(
        "zip"  // später einfach erweitern
    );

    // --------------------------------------------------
    
    private MediaExtensions() {}

    // --------------------------------------------------
    
    public static boolean isPathless(String ext) {
    	
        if (ext == null) return false;
        
        return PATHLESS_EXTENSIONS.contains(ext.toLowerCase());
        
    } // isPathless

    // --------------------------------------------------
    
} // class MediaExtensions

//##################################################
