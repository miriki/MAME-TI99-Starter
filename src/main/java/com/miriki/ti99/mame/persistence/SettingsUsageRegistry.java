package com.miriki.ti99.mame.persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//############################################################################

public class SettingsUsageRegistry {
	
    private static final String PREF_KEY = "settingsUsage";
    private static final Preferences PREFS = Preferences.userNodeForPackage( SettingsUsageRegistry.class );

    // Map: Pfad -> Count
    private static Map<Path, Integer> usageMap = new HashMap<>();
    private static Map<Path, LocalDateTime> lastUsedMap = new HashMap<>();

    public enum SortMode { BY_COUNT, BY_DATE }

    private static SortMode sortMode = SortMode.BY_COUNT;

    private static final Logger log = LoggerFactory.getLogger( SettingsUsageRegistry.class );
	
	// --------------------------------------------------
	
    static {
        load();
    } // class-init

	// --------------------------------------------------
	
    public static void setSortMode( SortMode mode ) {
    	
		log.debug( "----- start: setSortMode( '{}' )", mode );
		
        sortMode = mode;
        
        save(); // optional, wenn persistent
        
		log.debug( "----- end: setSortMode()" );

    } // setSortMode

	// --------------------------------------------------
	
    public static SortMode getSortMode() {
    	
		log.debug( "----- start: getSortMode()" );
		
        SortMode result = sortMode;
        
		log.debug( "----- end: getSortMode() ---> {}", result );
		
		return result;

    } // getSortMode
    
	// --------------------------------------------------
	
    public static void cleanupInvalidEntries() {
    	
		log.debug( "----- start: cleanupInvalidEntries()" );
		
        // Kopie der Keys, um ConcurrentModification zu vermeiden
        List<Path> toRemove = new ArrayList<>();
        
        for ( Path p : usageMap.keySet() ) {
        	
            if ( ! Files.exists( p )) {
                toRemove.add( p );
            }
            
        }
        toRemove.forEach( usageMap::remove );
        
        save(); // Änderungen persistieren
        
		log.debug( "----- end: cleanupInvalidEntries()" );

    } // cleanupInvalidEntries
    
	// --------------------------------------------------
	
    public static void removeEntry( Path p ) {
    	
		log.debug( "----- start: removeEntry( '{}' )", p );
		
        usageMap.remove( p );
        
        save();
        
		log.debug( "----- end: removeEntry()" );

    } // removeEntry
    
	// --------------------------------------------------
	
    private static void load() {
    	
		log.debug( "----- start: load()" );
		
        usageMap.clear();
        
        String raw = PREFS.get(PREF_KEY, "");
        
        if (!raw.isBlank()) {
        	
            for (String entry : raw.split(";")) {
            	
                String[] parts = entry.split("\\|");
                
                if (parts.length == 2) {
                	
                    Path p = Paths.get(parts[0]);
                    int count = Integer.parseInt(parts[1]);
                    usageMap.put(p, count);
                    
                }
                
            }
            
        }
        
		log.debug( "----- end: load()" );

    } // load

	// --------------------------------------------------
	
    private static void save() {
    	
		log.debug( "----- start: save()" );
		
        StringBuilder sb = new StringBuilder();
        
        for (Map.Entry<Path,Integer> e : usageMap.entrySet()) {
            sb.append(e.getKey().toAbsolutePath()).append("|").append(e.getValue()).append(";");
        }
        
        PREFS.put(PREF_KEY, sb.toString());
        
		log.debug( "----- end: save()" );

    } // save

	// --------------------------------------------------
	
    public static void renameEntry( Path oldPath, Path newPath ) {
    	
		log.debug( "----- start: renameEntry( '{}', '{}' )", oldPath, newPath );
		
        // Usage-Zähler übernehmen
        Integer count = usageMap.remove(oldPath);
        
        if (count != null) {
            usageMap.put(newPath, count);
        }
        
        save(); // Änderungen persistieren
        
		log.debug( "----- end: renameEntry()" );

    } // renameEntry

	// --------------------------------------------------
	
    public static void increment( Path file ) {
    	
		log.debug( "----- start: increment( '{}' )", file );
		
        usageMap.merge(file, 1, Integer::sum);
        
        save();
        
		log.debug( "----- end: increment()" );

    } // increment

	// --------------------------------------------------
	
    public static void updateLastUsed( Path p ) {
    	
		log.debug( "----- start: updateLastUsed( '{}' )", p );
		
        lastUsedMap.put(p, LocalDateTime.now());
        
        save();
        
		log.debug( "----- end: updateLastUsed()" );

    } // updateLastUsed
    
	// --------------------------------------------------
	
    private static List<Path> getTopByCount( int x ) {
    	
		log.debug( "----- start: getTopByCount( {} )", x );
		
		List<Path> result = usageMap.entrySet().stream()
            .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
            .limit(x)
            .map(Map.Entry::getKey)
            .toList();
        
		log.debug( "----- end: getTopByCount() ---> List:Path[{}]", result.size() );

		return result;
		
    } // getTopByCount
    
	// --------------------------------------------------
	
    private static List<Path> getTopByDate( int x ) {
    	
		log.debug( "----- start: getTopByDate( {} )", x );
		
		List<Path> result = lastUsedMap.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(x)
            .map(Map.Entry::getKey)
            .toList();
        
		log.debug( "----- end: getTopByDate() ---> List:Path[{}]", result.size() );
		
		return result;

    } // getTopByDate
    
	// --------------------------------------------------
	
    public static List<Path> getTop( int x ) {
    	
		log.debug( "----- start: getTop( {} )", x );
		
		List<Path> result = switch (sortMode) {
            case BY_COUNT -> getTopByCount(x);
            case BY_DATE  -> getTopByDate(x);
        };
        
		log.debug( "----- end: getTop() ---> List:Path[{}]", result.size() );
		
		return result;

    } // getTop
    
	// --------------------------------------------------
	
}

//############################################################################
