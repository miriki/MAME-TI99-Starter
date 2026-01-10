package com.miriki.ti99.mame.domain;

import java.util.Set;

/**
 * Utility class for handling media file extensions.
 * <p>
 * Some media types (e.g. ZIP archives) do not represent a single file
 * with a meaningful path inside the emulator environment. This helper
 * provides a simple way to check whether an extension belongs to that
 * category.
 */
public final class MediaExtensions {

    /**
     * Extensions that represent media types without a meaningful internal path.
     * <p>
     * This set can be extended as additional formats are introduced.
     */
    private static final Set<String> PATHLESS_EXTENSIONS = Set.of(
        "zip"
    );

    /** Prevent instantiation. */
    private MediaExtensions() {}

    /**
     * Returns whether the given file extension represents a pathless media type.
     *
     * @param ext the file extension (without dot), case-insensitive
     * @return {@code true} if the extension is considered pathless, otherwise {@code false}
     */
    public static boolean isPathless(String ext) {
        if (ext == null) {
            return false;
        }
        return PATHLESS_EXTENSIONS.contains(ext.toLowerCase());
    }
}
