package FileTools;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

//############################################################################

public class FileTools {

    private static final Logger log = LoggerFactory.getLogger( FileTools.class );
	
	// --------------------------------------------------
	
    public static boolean fileExists( String dpf ) {
	
		log.debug( "fileExists()" );
		
    	boolean result;
		File chk;
		
		result = true;
		
		chk = new File( dpf );
        if (( ! chk.exists() ) || ( ! chk.isFile() )) {
        	System.out.println( "missing: " + dpf );
        	result = false;
        } else {
        	System.out.println( "found: " + dpf );
        }
		
		return result;

    } // fileExists()

	// --------------------------------------------------
	
    public static boolean dirExists( String dpf ) {

		log.debug( "dirExists()" );
		
		boolean result;
		
		result = true;
		
		result = dirExists( dpf, "" );
		
		return result;

    } // dirExists()

	// ----------
	
    public static boolean dirExists( String subDirList, String baseDir ) {

		log.debug( "dirExists()" );
		
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

		        	System.out.println( "missing: " + fullPath );
		            result = false;

		        } else {

		        	System.out.println( "found: " + fullPath );
		        	
		        }

	        }
	    }

		return result;

    } // dirExists()

	// --------------------------------------------------

    public static List<String> scanDirectories( String workingDir, String cartPath, String fileExtension ) {
    	
        // key = lowercased name (zum Duplikatfilter), value = originaler Name (für Anzeige)
        Map<String, String> unique = new LinkedHashMap<>();

        String[] dirs = cartPath.split( ";" );
        for ( String dir : dirs ) {
            File folder = new File( workingDir, dir.trim() );
            if ( ! folder.isDirectory()) continue;

            File[] files = folder.listFiles(( d, name ) -> name.toLowerCase( Locale.ROOT ).endsWith( "." + fileExtension ));
            if ( files == null ) continue;

            for ( File file : files ) {
                String original = file.getName().replaceFirst( "(?i)\\.zip$", "" ).trim();
                if ( original.isEmpty() ) continue;

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
        sorted.add( 0, "------" );
        return sorted;
        
    } // scanCartridgeDirectories
    
	// --------------------------------------------------

    public static String prettyName(Path p) {
        String name = p.getFileName().toString();
        if (name.endsWith(".settings")) {
            name = name.substring(0, name.length() - ".settings".length());
        }
        return name.replace('_', ' ');
    }
    
    public static String editName(Path p) {
        String name = p.getFileName().toString();

        if (name.endsWith(".settings")) {
            name = name.substring(0, name.length() - ".settings".length());
        }

        return name; // Unterstriche bleiben erhalten
    }
    
    public static String safeFileName(String userInput) {
        String base = userInput.trim();

        base = base.replace(" ", "_");

        if (!base.endsWith(".settings")) {
            base = base + ".settings";
        }

        return base;
    }

    private static String normalizeDir(String dir) {
        Path p = Paths.get(dir).normalize();
        String s = p.toString();

        // Wenn kein Trenner am Ende → hinzufügen
        if (!s.endsWith(File.separator)) {
            s = s + File.separator;
        }

        return s;
    }
    
    public static String normalizeMultiPath(String multi) {
        if (multi == null || multi.isBlank()) {
            return "";
        }

        return Arrays.stream(multi.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(FileTools::normalizeDir)
                .collect(Collectors.joining(";"));
    }
    
    // --------------------------------------------------

}

//############################################################################
