package com.miriki.ti99.mame.service;

import java.util.List;

import com.miriki.ti99.mame.dto.CassetteEntry;

/**
 * Scanner for cassette media files.
 * <p>
 * Supports WAV and FLAC cassette formats and produces {@link CassetteEntry}
 * instances for each discovered file.
 */
public class CassetteScanner extends MediaScanner<CassetteEntry> {

    /**
     * Returns the supported cassette file extensions.
     */
    @Override
    protected List<String> getExtensions() {
        return List.of("wav", "flac");
    }

    /**
     * Creates a {@link CassetteEntry} for a discovered file.
     */
    @Override
    protected CassetteEntry createEntry(String path, String name, String ext) {
        return new CassetteEntry(path, name, ext);
    }

    /**
     * Creates the special "none" placeholder entry.
     */
    @Override
    protected CassetteEntry createNoneEntry() {
        return CassetteEntry.none();
    }
}
