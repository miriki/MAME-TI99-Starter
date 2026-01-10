package com.miriki.ti99.mame.service;

import java.util.List;

import com.miriki.ti99.mame.dto.CartridgeEntry;

/**
 * Scanner for cartridge media files.
 * <p>
 * Supports ZIP and RPK cartridge formats and produces {@link CartridgeEntry}
 * instances for each discovered file.
 */
public class CartridgeScanner extends MediaScanner<CartridgeEntry> {

    /**
     * Returns the supported cartridge file extensions.
     */
    @Override
    protected List<String> getExtensions() {
        return List.of("zip", "rpk");
    }

    /**
     * Creates a {@link CartridgeEntry} for a discovered file.
     */
    @Override
    protected CartridgeEntry createEntry(String path, String name, String ext) {
        return new CartridgeEntry(path, name, ext);
    }

    /**
     * Creates the special "none" placeholder entry.
     */
    @Override
    protected CartridgeEntry createNoneEntry() {
        return CartridgeEntry.none();
    }
}
