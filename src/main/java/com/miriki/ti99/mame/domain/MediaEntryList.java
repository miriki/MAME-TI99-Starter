package com.miriki.ti99.mame.domain;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.miriki.ti99.mame.dto.MediaEntry;
import com.miriki.ti99.mame.ui.UiConstants;

//##################################################

public abstract class MediaEntryList<T extends MediaEntry> {

    // --------------------------------------------------
    
    protected final List<T> entries = new ArrayList<>();

    // --------------------------------------------------
    
    public void set( List<T> newEntries ) {
    	
        entries.clear();
        entries.addAll( newEntries );
        
    } // set

    // --------------------------------------------------
    
    public List<T> getAll() {
    	
        return new ArrayList<>( entries );
        
    } // getAll

    // --------------------------------------------------
    
    public List<String> getDisplayNames() {

        return entries.stream()
                .map(MediaEntry::getDisplayName)
                .distinct()
                .toList();
        
    } // getDisplayNames

    /*
    public Path resolveMediaPath(String displayName) {

        if (displayName == null || displayName.isBlank() || UiConstants.CBX_SEL_NONE.equals(displayName)) {
            return null;
        }

        T entry = findByDisplayName(displayName);
        if (entry == null) {
            return null;
        }

        // ZIP → kein Pfad
        if ("zip".equalsIgnoreCase(entry.getMediaExt())) {
            return null;
        }

        // alle anderen → voller Pfad
        return entry.getFullPath();
        
    } // resolveMediaPath
    */
    
    public Path resolveMediaPath( String displayName ) {

        if ( displayName == null || displayName.isBlank() || UiConstants.CBX_SEL_NONE.equals( displayName )) {
            return null;
        }

        T entry = findByDisplayName( displayName );
        if ( entry == null ) {
            return null;
        }

        return entry.getFullPath();
    } // resolveMediaPath

    // --------------------------------------------------
    
    public Path resolveMediaRelativePath( String displayName, Path basePath ) {
    	
        MediaEntry entry = findByDisplayName( displayName );
        
        if ( entry == null ) return null;

        return entry.getRelativePath( basePath );
        
    } // resolveMediaRelativePath

    // --------------------------------------------------
    
    public T findByDisplayName(String displayName) {
    	
        if (displayName == null) {
        	
            return null;
            
        }

        for (T entry : entries) {
        	
            if (displayName.equals(entry.getDisplayName())) {
            	
                return entry;
                
            }
            
        }

        return null;
        
    } // findByDisplayName

    // --------------------------------------------------
    
    protected void deduplicate() {
    	
        Map<String, T> unique = new LinkedHashMap<>();
        
        for ( T e : entries ) {
            unique.putIfAbsent( e.getMediaName().toLowerCase(), e );
        }
        
        entries.clear();
        entries.addAll(unique.values());
        
    } // deduplicate

    // --------------------------------------------------
    
    protected void sort() {
    	
        entries.sort( Comparator.comparing(T::getMediaName, String.CASE_INSENSITIVE_ORDER ));
        
    } // sort

    // --------------------------------------------------
    
} // class MediaEntryList

//##################################################
