package com.miriki.ti99.mame.dto;

import java.nio.file.Path;

//##################################################

public abstract class MediaEntry {

	protected final String mediaName;
    protected final String mediaExt;
    protected final Path   mediaPath;

    public String getMediaName() { return mediaName; }
    public String getMediaExt()  { return mediaExt; }
    public Path   getMediaPath() { return mediaPath; }

    // --------------------------------------------------
    
    public MediaEntry( String mPath, String mName, String mExt ) {
    	
    	mediaName = mName;
    	mediaExt  = mExt;
    	mediaPath = Path.of( mPath );
    	
    } // [constructor] MediaEntry
    
    // --------------------------------------------------
    
    public Path getFullPath() {
    	
    	return mediaPath.resolve( mediaName + "." + mediaExt );
    	
    } // getFullPath

    // --------------------------------------------------
    
    public Path getRelativePath(Path basePath) {
    	
    	Path full = getFullPath();
    	
        if ( full == null ) {
            return null;
        }

        try {
            return basePath.relativize( full );
            
        } catch ( IllegalArgumentException ex ) {
            // Pfade liegen auf unterschiedlichen Laufwerken → absolut verwenden
            return full;
            
        }
        
    } // getRelativePath

    // --------------------------------------------------
    
    public String getDisplayName() {
    	
        return mediaName + "." + mediaExt;
        
    } // getDisplayName

    // --------------------------------------------------
    
    @Override
    public String toString() {
    	
        return mediaName;
        
    } // toString
	
} // class MediaEntry

//##################################################
