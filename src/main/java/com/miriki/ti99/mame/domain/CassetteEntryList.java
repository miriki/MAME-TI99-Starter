package com.miriki.ti99.mame.domain;

import com.miriki.ti99.mame.dto.CassetteEntry;

/**
 * A typed list for {@link CassetteEntry} objects.
 * <p>
 * This class provides semantic clarity when working with cassette-based
 * media entries. It does not introduce additional behavior beyond
 * {@link MediaEntryList}, but improves readability and intent throughout
 * the codebase.
 */
public class CassetteEntryList extends MediaEntryList<CassetteEntry> {
    // No additional implementation required at this time.
}
