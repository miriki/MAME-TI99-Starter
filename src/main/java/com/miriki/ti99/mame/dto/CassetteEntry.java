package com.miriki.ti99.mame.dto;

/**
 * Represents a cassette media entry.
 * <p>
 * Cassettes behave like simple file-based media without special path
 * handling. This class also provides a {@code none()} placeholder entry
 * used for UI selection lists.
 */
public class CassetteEntry extends MediaEntry {

    /**
     * Creates a new cassette entry.
     *
     * @param basePath the base directory
     * @param baseName the media name
     * @param baseExt  the file extension
     */
    public CassetteEntry(String basePath, String baseName, String baseExt) {
        super(basePath, baseName, baseExt);
    }

    /**
     * Creates the special placeholder entry used in UI selection lists.
     *
     * @return a new placeholder cassette entry
     */
    public static CassetteEntry none() {
        return new CassetteEntry("", "------", "");
    }
}
