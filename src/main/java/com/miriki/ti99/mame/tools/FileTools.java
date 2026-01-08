package com.miriki.ti99.mame.tools;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.ui.UiConstants;

//############################################################################

public class FileTools {

	// --------------------------------------------------
	
    private static final Logger log = LoggerFactory.getLogger( FileTools.class );
	
	// --------------------------------------------------
	
    public static boolean fileExists( String dpf ) {
	
		log.debug( "----- start: fileExists( '{}' )", dpf );
		
    	boolean result;
		File chk;
		
		result = true;
		
		chk = new File( dpf );
		
        if (( ! chk.exists() ) || ( ! chk.isFile() )) {
        	result = false;
        }
		
		log.debug( "----- end: fileExists() ---> {}", result );

		return result;

    } // fileExists()

	// --------------------------------------------------
	
    public static boolean dirExists( String dpf ) {

		log.debug( "----- start: dirExists( '{}' )", dpf );
		
		boolean result;
		
		result = true;
		
		result = dirExists( dpf, "" );
		
		log.debug( "----- end: fileExists() ---> {}", result );

		return result;

    } // dirExists()

	// ----------
	
    public static boolean dirExists( String subDirList, String baseDir ) {

		log.debug( "----- start: dirExists( '{}', '{}' )", subDirList, baseDir );
		
		boolean result;
		
		result = true;
		
	    Path basePath = Paths.get( baseDir.trim() ).normalize();
	    String[] subDirArray = subDirList.split( ";" );
	    
	    for ( String subDir : subDirArray ) {
	    	subDir = subDir.trim();
	        if ( ! subDir.isEmpty() ) {

		        Path subPath = Paths.get( subDir ).normalize();
	
		        // Absolut oder relativ?
		        Path fullPath = subPath.isAbsolute() ? subPath : basePath.resolve( subPath );
	
		        if (( ! Files.exists( fullPath )) || ( ! Files.isDirectory( fullPath ))) {
		            result = false;
		        }

	        }
	    }

		log.debug( "----- end: dirExists() ---> {}", result );

		return result;

    } // dirExists()

	// --------------------------------------------------

    public static List<String> scanDirectories( String basePath, String subPath, String... fileExt ) {
    	
		log.debug( "----- start: scanDirectories( '{}', '{}', '{}' )", basePath, subPath, fileExt );
		
        // key = lowercased name (zum Duplikatfilter), value = originaler Name (für Anzeige)
        Map<String, String> unique = new LinkedHashMap<>();

        String[] dirs = subPath.split( ";" );
        for ( String dir : dirs ) {
            File folder = new File( basePath, dir.trim() );
            if ( ! folder.isDirectory()) continue;

            // File[] files = folder.listFiles(( d, name ) -> name.toLowerCase( Locale.ROOT ).endsWith( "." + fileExtension ));
            File[] files = folder.listFiles(( d, name ) -> {
                String lower = name.toLowerCase( Locale.ROOT );
                for ( String ext : fileExt ) {
                    if (lower.endsWith("." + ext)) return true;
                }
                return false;
            });
            if ( files == null ) continue;

            for ( File file : files ) {
                // String original = file.getName().replaceFirst( "(?i)\\.zip$", "" ).trim();
                // if ( original.isEmpty() ) continue;
                String original = file.getName().trim();
                String lower    = original.toLowerCase(Locale.ROOT);
                for (String ext : fileExt) {
                    String dotExt = "." + ext.toLowerCase(Locale.ROOT);
                    if (lower.endsWith(dotExt)) {
                        original = original.substring(0, original.length() - dotExt.length()).trim();
                        break;
                    }
                }

                if (original.isEmpty()) continue;

                String key = original.toLowerCase( Locale.ROOT );
                // Nur den ersten Treffer behalten, um Original-Schreibweise konsistent zu halten
                unique.putIfAbsent( key, original );
            }
        }

        // Werte extrahieren und locale-bewusst, case-insensitiv sortieren
        List<String> sorted = new ArrayList<>( unique.values() );
        Collator collator = Collator.getInstance( Locale.getDefault() );
        collator.setStrength( Collator.PRIMARY ); // ignoriert Groß/Kleinschreibung, Akzente
        sorted.sort(( a, b ) -> collator.compare( a, b ));

        // Platzhalter oben fix
        sorted.add( 0, UiConstants.CBX_SEL_NONE );

		log.debug( "----- end: scanDirectories() ---> List:String[{}]", sorted.size() );
        
        return sorted;
        
    } // scanDirectories
    
	// --------------------------------------------------

    public static String prettyName(Path p) {

    	log.debug( "----- start: prettyName( '{}' )", p );
    	
    	String result = p.getFileName().toString();
    	
        if ( result.endsWith( ".settings" )) {
        	result = result.substring( 0, result.length() - ".settings".length() );
        }
        
        result = result.replace( '_', ' ' );

        log.debug( "----- end: prettyName() ---> '{}'", result );

        return result;
        
    } // prettyName
    
	// --------------------------------------------------

    public static String editName(Path p) {

    	log.debug( "----- start: editName( '{}' )", p );
    	
    	String result = p.getFileName().toString();

        if (result.endsWith(".settings")) {
        	result = result.substring(0, result.length() - ".settings".length());
        }

        log.debug( "----- end: editName() ---> '{}'", result );

        return result; // Unterstriche bleiben erhalten

    } // editName
    
	// --------------------------------------------------

    public static String safeFileName(String userInput) {
    	
    	log.debug( "----- start: safeFileName( '{}' )", userInput );
    	
        String result = userInput.trim();

        result = result.replace( " ", "_" );

        if ( ! result.endsWith( ".settings" )) {
        	result = result + ".settings";
        }

        log.debug( "----- end: safeFileName() ---> '{}'", result );

        return result;

    } // safeFileName

	// --------------------------------------------------

    private static String normalizeDir(String dir) {
    	
    	log.debug( "----- start: normalizeDir( '{}' )", dir );
    	    	
        Path p = Paths.get( dir ).normalize();
        String result = p.toString();

        // Wenn kein Trenner am Ende → hinzufügen
        if ( ! result.endsWith( File.separator )) {
        	result = result + File.separator;
        }

        log.debug( "----- end: normalizeDir() ---> '{}'", result );

        return result;

    } // normalizeDir
    
	// --------------------------------------------------

    public static String normalizeMultiPath(String multi) {
    	
    	log.debug( "----- start: normalizeDir( '{}' )", multi );
    	
    	// TODO: globeron reported: in add option it adds the \ after the last option then mame cannot start the geneve.
    	// fixed: The action Event Listener was active for "normal" textfields, too, not only for "path" ones.

    	String result = "";
    	
        if (( multi != null ) && ( ! multi.isBlank() )) {

	        result = Arrays.stream(multi.split(";"))
	                .map(String::trim)
	                .filter(s -> !s.isEmpty())
	                .map(FileTools::normalizeDir)
	                .collect(Collectors.joining(";"));

        }

        log.debug( "----- end: normalizeDir() ---> '{}'", result );
        
        return result;
        
    } // normalizeMultiPath
    
    // --------------------------------------------------

    public static boolean canWriteFile( Path file ) {
    	
    	log.debug( "----- start: canWriteFile( '{}' )", file );
    	
    	boolean result = false;
    	
        try {
            Path dir = file.getParent();
            if ( dir == null ) return false;

            // Verzeichnis existiert nicht → versuchen anzulegen
            if ( ! Files.exists( dir )) {
                Files.createDirectories( dir );
            }

            // Verzeichnis existiert, aber nicht beschreibbar
            if ( ! Files.isWritable( dir )) {
                return false;
            }

            // Datei existiert → prüfen ob beschreibbar
            if ( Files.exists( file )) {
                return Files.isWritable( file );
            }

            // Datei existiert nicht → testen ob sie angelegt werden kann
            try ( OutputStream out = Files.newOutputStream( file,
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE )) {
                // Datei erfolgreich angelegt → wieder löschen
            }
            Files.deleteIfExists( file );

            result = true;

        } catch ( IOException e ) {
        	
            result = false;
            
        }

        log.debug( "----- end: canWriteFile() ---> {}", result );

        return result;

    } // canWriteFile

    // --------------------------------------------------

}

//############################################################################
