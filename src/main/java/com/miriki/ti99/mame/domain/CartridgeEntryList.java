package com.miriki.ti99.mame.domain;

import com.miriki.ti99.mame.dto.CartridgeEntry;

/**
 * A typed list for {@link CartridgeEntry} objects.
 * <p>
 * This class exists primarily to provide type safety and semantic clarity
 * when working with cartridge-related media entries. It does not add
 * additional behavior beyond {@link MediaEntryList}, but improves
 * readability and intent throughout the codebase.
 */
public class CartridgeEntryList extends MediaEntryList<CartridgeEntry> {
    // No additional implementation required at this time.
}
