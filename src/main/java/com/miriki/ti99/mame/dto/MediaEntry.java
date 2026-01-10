package com.miriki.ti99.mame.dto;

import java.nio.file.Path;

/**
 * Base class for all media entry types (cartridge, cassette, floppy, harddisk).
 * <p>
 * A {@code MediaEntry} represents a single media file with a name, extension
 * and base directory. Subclasses may override path resolution behavior when
 * needed (e.g. for pathless formats).
 */
public abstract class MediaEntry {

    /** The media file name without extension. */
    protected final String mediaName;

    /** The media file extension (without dot). */
    protected final String mediaExt;

    /** The base directory where the media file resides. */
    protected final Path mediaPath;

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    /**
     * Creates a new media entry.
     *
     * @param basePath the base directory
     * @param baseName the media name (without extension)
     * @param baseExt  the file extension (without dot)
     */
    public MediaEntry(String basePath, String baseName, String baseExt) {
        this.mediaName = baseName;
        this.mediaExt  = baseExt;
        this.mediaPath = Path.of(basePath);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public String getMediaName() {
        return mediaName;
    }

    public String getMediaExt() {
        return mediaExt;
    }

    public Path getMediaPath() {
        return mediaPath;
    }

    // -------------------------------------------------------------------------
    // Path resolution
    // -------------------------------------------------------------------------

    /**
     * Returns the full path to the media file.
     *
     * @return the resolved path (never null unless overridden by subclasses)
     */
    public Path getFullPath() {
        return mediaPath.resolve(mediaName + "." + mediaExt);
    }

    /**
     * Returns the path to this media relative to a given base directory.
     * <p>
     * If the paths cannot be relativized (e.g. different drives), the absolute
     * full path is returned instead.
     *
     * @param basePath the base directory to relativize against
     * @return the relative path, or the absolute path if relativization fails
     */
    public Path getRelativePath(Path basePath) {
        Path full = getFullPath();
        if (full == null) {
            return null;
        }

        try {
            return basePath.relativize(full);
        } catch (IllegalArgumentException ex) {
            // Paths on different root volumes → fall back to absolute path
            return full;
        }
    }

    // -------------------------------------------------------------------------
    // Display helpers
    // -------------------------------------------------------------------------

    /**
     * Returns the display name in the form {@code name.ext}.
     */
    public String getDisplayName() {
        return mediaName + "." + mediaExt;
    }

    @Override
    public String toString() {
        return mediaName;
    }
}
