package com.miriki.ti99.mame.service;

import java.util.List;

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
}
