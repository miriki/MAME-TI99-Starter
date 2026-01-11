package com.miriki.ti99.mame.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import com.miriki.ti99.mame.dto.FloppyEntry;

/**
 * Scanner for floppy‑disk media files.
 * <p>
 * Supports DSK, DTK and HFE floppy image formats and produces
 * {@link FloppyEntry} instances for each discovered file.
 */
public class FloppyScanner extends MediaScanner<FloppyEntry> {

    /**
     * Returns the supported floppy image file extensions.
     */
    @Override
    protected List<String> getExtensions() {
        return List.of("dsk", "dtk", "hfe");
    }

    /**
     * Creates a {@link FloppyEntry} for a discovered file.
     */
    @Override
    protected FloppyEntry createEntry(String path, String name, String ext) {
        return new FloppyEntry(path, name, ext);
    }

    /**
     * Creates the special "none" placeholder entry.
     */
    @Override
    protected FloppyEntry createNoneEntry() {
        return FloppyEntry.none();
    }
    
    @Override
    protected List<FloppyEntry> buildEntries(List<String> names, List<Path> paths) {
        List<FloppyEntry> result = super.buildEntries(names, paths);

        for (Path base : paths) {
            try (Stream<Path> stream = Files.list(base)) {
                stream
                    .filter(Files::isDirectory)
                    .forEach(dir -> {
                        String name = dir.getFileName().toString();
                        result.add(createEntry(base.toString(), name, "fiad"));
                    });
            } catch (IOException e) {
                // optional: logging
            }
        }

        return result;
    }
    
}
