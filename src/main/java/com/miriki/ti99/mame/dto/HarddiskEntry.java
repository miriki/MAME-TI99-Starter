package com.miriki.ti99.mame.dto;

/**
 * Represents a hard‑disk media entry.
 * <p>
 * Hard‑disk images behave like regular file‑based media without special
 * path‑handling rules. This class also provides a {@code none()} placeholder
 * entry used for UI selection lists.
 */
public class HarddiskEntry extends MediaEntry {

    /**
     * Creates a new hard‑disk entry.
     *
     * @param basePath the base directory
     * @param baseName the media name
     * @param baseExt  the file extension
     */
    public HarddiskEntry(String basePath, String baseName, String baseExt) {
        super(basePath, baseName, baseExt);
    }

    /**
     * Creates the special placeholder entry used in UI selection lists.
     *
     * @return a new placeholder hard‑disk entry
     */
    public static HarddiskEntry none() {
        return new HarddiskEntry("", "------", "");
    }
}
