package com.miriki.ti99.mame.dto;

/**
 * Represents a floppy‑disk media entry.
 * <p>
 * Floppy images behave like regular file‑based media without special
 * path‑handling rules. This class also provides a {@code none()} placeholder
 * entry used for UI selection lists.
 */
public class FloppyEntry extends MediaEntry {

    /**
     * Creates a new floppy entry.
     *
     * @param basePath the base directory
     * @param baseName the media name
     * @param baseExt  the file extension
     */
    public FloppyEntry(String basePath, String baseName, String baseExt) {
        super(basePath, baseName, baseExt);
    }

    /**
     * Creates the special placeholder entry used in UI selection lists.
     *
     * @return a new placeholder floppy entry
     */
    public static FloppyEntry none() {
        return new FloppyEntry("", "------", "");
    }
}
