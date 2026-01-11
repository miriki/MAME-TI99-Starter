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

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.dto.MediaEntry;

/**
 * Base class for all media scanners.
 * <p>
 * A {@code MediaScanner} resolves one or more search directories, scans them
 * for files matching specific extensions, and produces {@link MediaEntry}
 * instances for each discovered media file.
 *
 * @param <T> the concrete media entry type
 */
public abstract class MediaScanner<T extends MediaEntry> {

    // private static final Logger log = LoggerFactory.getLogger(MediaScanner.class);

    // -------------------------------------------------------------------------
    // Abstract factory methods
    // -------------------------------------------------------------------------

    /** Returns the supported file extensions (without dot). */
    protected abstract List<String> getExtensions();

    /** Creates a media entry for a discovered file. */
    protected abstract T createEntry(String path, String name, String ext);

    /** Creates the special "none" placeholder entry. */
    protected abstract T createNoneEntry();

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Scans the given media path(s) and returns a sorted, deduplicated list of
     * media entries.
     *
     * @param basePath  the base directory
     * @param mediaPath semicolon-separated list of subdirectories
     */
    public List<T> scan(String basePath, String mediaPath) {
        List<Path> paths = resolvePaths(basePath, mediaPath);
        List<String> names = scanDirectories(paths);
        List<T> entries = buildEntries(names, paths);
        return finalize(entries);
    }

    // -------------------------------------------------------------------------
    // Path resolution
    // -------------------------------------------------------------------------

    /**
     * Resolves all semicolon-separated directory names into absolute paths.
     */
    protected List<Path> resolvePaths(String basePath, String mediaPath) {
        List<Path> result = new ArrayList<>();

        for (String part : mediaPath.split(";")) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                continue;
            }

            try {
                result.add(Paths.get(basePath, trimmed));
            } catch (InvalidPathException ex) {
                // ignore invalid entries
            }
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Directory scanning
    // -------------------------------------------------------------------------

    /**
     * Scans all directories for files matching the supported extensions.
     *
     * @return list of file names without extension
     */
    protected List<String> scanDirectories(List<Path> paths) {
        List<String> names = new ArrayList<>();

        for (Path p : paths) {
            if (!Files.exists(p)) {
                continue;
            }

            try (var stream = Files.list(p)) {
                stream.forEach(file -> {
                    String fn = file.getFileName().toString();
                    String lower = fn.toLowerCase();

                    for (String ext : getExtensions()) {
                        String dotext = "." + ext;
                        if (lower.endsWith(dotext)) {
                            names.add(fn.substring(0, fn.length() - dotext.length()));
                        }
                    }
                });
            } catch (Exception ex) {
                // ignore unreadable directories
            }
        }

        return names;
    }

    // -------------------------------------------------------------------------
    // Entry construction
    // -------------------------------------------------------------------------

    /**
     * Builds media entries for all discovered names and paths.
     */
    protected List<T> buildEntries(List<String> names, List<Path> paths) {
    	// log.debug( "buildEntries( names={}, paths={} )", names, paths );
        List<T> result = new ArrayList<>();

        for (String name : names) {
            for (Path p : paths) {
                for (String ext : getExtensions()) {
                	// if ( ext == "dsk" ) {
                	// log.trace( "  scanning name='{}'", name );
                	// log.trace( "  scanning path='{}'", p );
                	// log.trace( "  scanning ext='{}'", ext );
                    Path file = p.resolve(name + "." + ext);
                	// log.trace( "  file='{}'", file );

                    if (Files.exists(file)) {
                        result.add(createEntry(p.toString(), name, ext));
                    	// log.trace( "  +++ file was found, added to list +++", file );
                    } else {
                    	// log.trace( "  --- file was NOT found ---", file );
                    }
                	// }
                }
            }
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Finalization: dedup + sort
    // -------------------------------------------------------------------------

    /**
     * Deduplicates entries by media name (case-insensitive) and sorts them
     * alphabetically.
     */
    protected List<T> finalize(List<T> entries) {
        List<T> withNone = new ArrayList<>();
        // withNone.add(createNoneEntry()); // optional placeholder
        withNone.addAll(entries);

        Map<String, T> unique = new LinkedHashMap<>();
        for (T e : withNone) {
            unique.putIfAbsent(e.getMediaName().toLowerCase(), e);
        }

        List<T> deduped = new ArrayList<>(unique.values());
        deduped.sort(Comparator.comparing(T::getMediaName, String.CASE_INSENSITIVE_ORDER));

        return deduped;
    }
}
