package com.miriki.ti99.mame.service;

import java.util.List;

import com.miriki.ti99.mame.dto.HarddiskEntry;

/**
 * Scanner for hard‑disk media files.
 * <p>
 * Supports CHD and HD disk image formats and produces {@link HarddiskEntry}
 * instances for each discovered file.
 */
public class HarddiskScanner extends MediaScanner<HarddiskEntry> {

    /**
     * Returns the supported hard‑disk image file extensions.
     */
    @Override
    protected List<String> getExtensions() {
        return List.of("chd", "hd");
    }

    /**
     * Creates a {@link HarddiskEntry} for a discovered file.
     */
    @Override
    protected HarddiskEntry createEntry(String path, String name, String ext) {
        return new HarddiskEntry(path, name, ext);
    }

    /**
     * Creates the special "none" placeholder entry.
     */
    @Override
    protected HarddiskEntry createNoneEntry() {
        return HarddiskEntry.none();
    }
}
