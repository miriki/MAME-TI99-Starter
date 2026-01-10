package com.miriki.ti99.mame.domain;

import com.miriki.ti99.mame.dto.FloppyEntry;

/**
 * A typed list for {@link FloppyEntry} objects.
 * <p>
 * This class provides semantic clarity when working with floppy‑disk
 * media entries. It does not introduce additional behavior beyond
 * {@link MediaEntryList}, but improves readability and intent throughout
 * the codebase.
 */
public class FloppyEntryList extends MediaEntryList<FloppyEntry> {
    // No additional implementation required at this time.
}
