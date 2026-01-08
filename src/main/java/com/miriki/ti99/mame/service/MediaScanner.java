package com.miriki.ti99.mame.service;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.miriki.ti99.mame.dto.MediaEntry;

//##################################################

public abstract class MediaScanner<T extends MediaEntry> {

    // --------------------------------------------------
    
    protected abstract List<String> getExtensions();
    protected abstract T createEntry( String path, String name, String ext );
    protected abstract T createNoneEntry();

    // --------------------------------------------------
    
    public List<T> scan( String basePath, String mediaPath ) {
    	
        List<Path> paths =   resolvePaths   ( basePath, mediaPath );
        List<String> names = scanDirectories( paths );
        List<T> entries =    buildEntries   ( names, paths );
        
        return finalize( entries );
        
    } // scan

    // --------------------------------------------------
    
    protected List<Path> resolvePaths( String basePath, String mediaPath ) { 
    	
        List<Path> result = new ArrayList<>();
        for ( String part : mediaPath.split( ";" )) {
        	
            String trimmed = part.trim();
            if ( trimmed.isEmpty() ) continue;
            
            try {
                result.add( Paths.get( basePath, trimmed ));
            } catch ( InvalidPathException ex ) {
                // ignore
            }
            
        }
        
        return result;
        
    } // resolvePaths
    
    // --------------------------------------------------
    
    protected List<String> scanDirectories( List<Path> paths ) { 
    	
        List<String> names = new ArrayList<>();

        for ( Path p : paths ) {
            if ( ! Files.exists( p )) continue;

            try ( var stream = Files.list( p )) {
            	
                stream.forEach( file -> {
                    String fn = file.getFileName().toString();
                    String lower = fn.toLowerCase();
                    for ( String ext : getExtensions() ) {
	                    String dotext = "." + ext;
	                    if ( lower.endsWith( dotext )) {
	                        names.add( fn.substring( 0, fn.length() - dotext.length() ));
	                    }
                    }
                });
                
            } catch (Exception ex) {
                // ignore
            }
            
        }

        return names;
        
    } // scanDirectories 
    
    // --------------------------------------------------
    
    protected List<T> buildEntries( List<String> names, List<Path> paths ) {

        List<T> result = new ArrayList<>();

        for ( String name : names ) {
            for ( Path p : paths ) {

            	for (String ext : getExtensions()) {
            		
	                Path file = p.resolve( name + "." + ext );
	
	                if ( Files.exists( file )) {
	                    result.add( createEntry( p.toString(), name, ext ));
	                }
                
            	}
            }
        }

        return result;
        
    } // buildEntries
    
    // --------------------------------------------------
    
    protected List<T> finalize( List<T> entries ) {

        List<T> withNone = new ArrayList<>();
        // withNone.add( createNoneEntry() );
        withNone.addAll( entries );

        Map<String, T> unique = new LinkedHashMap<>();
        for ( T e : withNone ) {
            unique.putIfAbsent( e.getMediaName().toLowerCase(), e );
        }

        List<T> deduped = new ArrayList<>( unique.values() );
        deduped.sort( Comparator.comparing( T::getMediaName, String.CASE_INSENSITIVE_ORDER ));

        return deduped;
        
    } // finalize
    
    // --------------------------------------------------
    
}

//##################################################
