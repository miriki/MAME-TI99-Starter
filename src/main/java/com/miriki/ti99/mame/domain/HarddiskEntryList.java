package com.miriki.ti99.mame.domain;

import com.miriki.ti99.mame.dto.HarddiskEntry;

/**
 * A typed list for {@link HarddiskEntry} objects.
 * <p>
 * This class provides semantic clarity when working with hard‑disk
 * media entries. It does not introduce additional behavior beyond
 * {@link MediaEntryList}, but improves readability and intent throughout
 * the codebase.
 */
public class HarddiskEntryList extends MediaEntryList<HarddiskEntry> {
    // No additional implementation required at this time.
}
