package com.miriki.ti99.mame.persistence;

import java.nio.file.*;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//############################################################################

public final class SettingsPathRegistry {
	
	// --------------------------------------------------
	
    private static final String PREF_KEY = "lastSettingsFile";
    private static final String DEFAULT_FILE = "MAME_TI99_Starter.settings";
    private static final Preferences PREFS = Preferences.userNodeForPackage( SettingsPathRegistry.class );

    private static final Logger log = LoggerFactory.getLogger( SettingsPathRegistry.class );
	
	// --------------------------------------------------
	
    private SettingsPathRegistry() {}

	// --------------------------------------------------
	
    public static Path getCurrent() {
    	
        log.debug("----- start: getCurrent()" );

		Path result;
		
        String stored = PREFS.get( PREF_KEY, "" );
        
        if ( stored == null || stored.isBlank() ) {
            result = Paths.get( DEFAULT_FILE );
        } else {
            result = Paths.get( stored );
        }
        
		log.debug( "----- end: getCurrent() ---> '{}'", result );

        return result;
        
    } // getCurrent

	// --------------------------------------------------
	
    public static void setCurrent(Path path) {
    	
		log.debug( "----- start: setCurrent( '{}' )", path );
		
        if ( path != null ) {
            PREFS.put( PREF_KEY, path.toAbsolutePath().toString() );
        }
        
		log.debug( "----- end: setCurrent()" );

    } // setCurrent

	// --------------------------------------------------
	
    public static void cleanupCurrentIfInvalid() {
    	
        Path current = getCurrent();
        
        if (current != null && !Files.exists(current)) {
            setCurrent(null); // oder clearCurrent()
        }
        
		log.debug( "----- end: cleanupCurrentIfInvalid()" );

    } // cleanupCurrentIfInvalid

	// --------------------------------------------------
	
}

//############################################################################
